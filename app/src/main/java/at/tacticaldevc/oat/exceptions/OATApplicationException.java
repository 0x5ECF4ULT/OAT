package at.tacticaldevc.oat.exceptions;

public class OATApplicationException extends RuntimeException {
    private OATApplicationException(String msg, Exception ex) {
        super(msg, ex);
    }

    private OATApplicationException(String msg) {
        super(msg);
    }

    public static OATApplicationException forNoPasswordSet() {
        return new OATApplicationException("No password has been set!");
    }

    public static OATApplicationException forLibraryDeprecatedError(String library, Exception thrownException) {
        return new OATApplicationException("The following library seems to no longer support a required feature: " + library, thrownException);
    }

    public static OATApplicationException forLibraryError(String library, Exception thrownException) {
        return new OATApplicationException("The following library has thrown an error: " + library, thrownException);
    }

    public static OATApplicationException forFeatureNotSupported(String name) {
        return new OATApplicationException("The following feature can not be executed because of unsatisfied requirements: " + name);
    }

    public static OATApplicationException forSecurityException(String name, Exception e) {
        return new OATApplicationException("The following library has thrown a SecurityException: " + name, e);
    }

    public static OATApplicationException forOther(String name, String msg) {
        return new OATApplicationException(name + " has thrown an unknown Exception: " + msg);
    }

    public static OATApplicationException forOther(String name, String msg, Exception e) {
        return new OATApplicationException(name + " has thrown an unknown Exception: " + msg, e);
    }
}
