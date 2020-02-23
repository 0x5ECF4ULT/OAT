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
  
    public static OATApplicationException forPasswordMismatch() {
        return new OATApplicationException("The supplied old password did not match the saved password. The password was not updated.");
    }

    public static OATApplicationException forCorruptedPasswordHash() {
        return new OATApplicationException("The password hash was corrupted. The password was deleted and a new password has to be set in the Application Settings.");
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
  
    public static OATApplicationException forLibraryError(String library, Exception thrownException) {
        return new OATApplicationException("The following library seems to no longer support a required feature: " + library, thrownException);
    }

    public static OATApplicationException forNoSMSSubscriptionFound() {
        return new OATApplicationException("No Subscription could be obtained to send a SMS!");
    }
}
