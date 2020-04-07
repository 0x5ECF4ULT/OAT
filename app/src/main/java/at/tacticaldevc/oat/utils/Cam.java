package at.tacticaldevc.oat.utils;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Environment;
import android.util.Size;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

import at.tacticaldevc.oat.exceptions.OATApplicationException;

import static at.tacticaldevc.oat.utils.Ensurer.ensureNotNull;

/**
 * A helper class for the camera
 */
public class Cam {
    private static CameraManager cm = null;
    private static CameraDevice cam;
    private static CaptureRequest cr;

    /**
     * Initializes the Camera API. If it fails an Exception is raised. This is the first method to be called.
     *
     * @throws at.tacticaldevc.oat.exceptions.OATApplicationException in case the camera could not be connected
     */
    public static void init(Context context) {
        ensureNotNull(context, "Context");

        cm = context.getSystemService(CameraManager.class);

        try {
            for (String identifier : cm.getCameraIdList()) {
                if (cm.getCameraCharacteristics(identifier).get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    cm.openCamera(identifier, new Cam.StateCallBackListener(), null);
                }
            }
        } catch (SecurityException e) {
            throw OATApplicationException.forSecurityException("Camera2", e);
        } catch (CameraAccessException e) {
            throw OATApplicationException.forLibraryError("Camera2", e);
        }
    }


    /**
     * Initializes the opened camera. This method should be called after Cam.init()
     *
     * @param characteristics the characteristics to use
     */
    public static void configure(CameraCharacteristics characteristics) {
        ensureNotNull(cm, "CameraManager");
        ensureNotNull(cam, "Camera");
        ensureNotNull(characteristics, "Camera characteristics");

        //StreamConfigurationMap.getOutputFormats() -> JPEG
        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        int chosenFormat = ImageFormat.JPEG;
        //StreamConfigurationMap.getOutputSizes() -> sort
        Size[] sizes = map.getOutputSizes(chosenFormat);
        Arrays.sort(sizes, (a, b) -> Integer.compare(a.getWidth(), b.getWidth()));

        ImageReader ir = ImageReader.newInstance(sizes[0].getWidth(), sizes[0].getHeight(), chosenFormat, 1);
        CaptureRequest.Builder crb;
        try {
            crb = cam.createCaptureRequest(CameraDevice.TEMPLATE_ZERO_SHUTTER_LAG);

            crb.addTarget(ir.getSurface());
            cr = crb.build();

            cam.createCaptureSession(Collections.singletonList(ir.getSurface()), new CameraCaptureSessionStateCallbackListener(), null);
        } catch (CameraAccessException e) {
            throw OATApplicationException.forLibraryError("Camera2", e);
        }
    }

    /**
     * If the camera has been opened and configured call this method to take a picture
     *
     * @return Image object representation or null if a camera session could not be acquired
     */
    private static void takePicture(CameraCaptureSession ccs) {
        try {
            ccs.capture(cr, null, null);
        } catch (CameraAccessException e) {
            throw OATApplicationException.forLibraryError("Camera2", e);
        }
    }

    private static Uri saveImage(ImageReader reader) {
        Image image = reader.acquireLatestImage();
        ByteBuffer buf = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buf.capacity()];
        buf.get(bytes);

        try {
            File f = new File(Environment.getExternalStorageDirectory() + String.format("/cap_%S.jpg", Calendar.getInstance().getTime()));
            FileOutputStream fos = new FileOutputStream(f);
            f.createNewFile();

            fos.write(bytes);
            fos.flush();
            fos.close();

            return Uri.fromFile(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void sendImage(Uri imageUri) {

    }

    public static void close() {
        cam.close();
    }

    private static class StateCallBackListener extends CameraDevice.StateCallback {

        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cam = camera;
            try {
                CameraCharacteristics characteristics = cm.getCameraCharacteristics(cam.getId());
                configure(characteristics);
            } catch (CameraAccessException e) {
                throw OATApplicationException.forLibraryError("Camera2", e);
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cr = null;
            cam = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            throw OATApplicationException.forOther("Camera2", "StateCallBackListener.onError() returned " + error);
        }
    }

    private static class CameraCaptureSessionStateCallbackListener extends CameraCaptureSession.StateCallback {

        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            takePicture(session);

            session.close();
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            throw OATApplicationException.forOther("Camera2", "CameraCaptureSessionStateCallbackListener.onConfigureFailed()");
        }
    }

    private static class ImageReaderPictureAvailableCallback implements ImageReader.OnImageAvailableListener {

        @Override
        public void onImageAvailable(ImageReader reader) {
            Uri imageUri = saveImage(reader);
            sendImage(imageUri);
        }
    }
}
