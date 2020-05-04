package at.tacticaldevc.oat.exceptions;

public class OATApplicationException extends RuntimeException {

    private OATApplicationExceptionType exceptionType;

    private OATApplicationException(String msg, Exception ex, OATApplicationExceptionType exceptionType) {
        super(msg, ex);
        this.exceptionType = exceptionType;
    }

    private OATApplicationException(String msg, OATApplicationExceptionType exceptionType) {
        super(msg);
        this.exceptionType = exceptionType;
    }

    public static OATApplicationException forNoPasswordSet() {
        return new OATApplicationException("No password has been set!", OATApplicationExceptionType.NoPasswordSet);
    }

    public static OATApplicationException forPasswordMismatch() {
        return new OATApplicationException("The supplied old password did not match the saved password. The password was not updated.", OATApplicationExceptionType.PasswordMismatch);
    }

    public static OATApplicationException forCorruptedPasswordHash() {
        return new OATApplicationException("The password hash was corrupted. The password was deleted and a new password has to be set in the Application Settings.", OATApplicationExceptionType.CorruptedPasswordHash);
    }

    public static OATApplicationException forLibraryDeprecatedError(String library, Exception thrownException) {
        return new OATApplicationException("The following library seems to no longer support a required feature: " + library, thrownException, OATApplicationExceptionType.LibraryDeprecated);
    }

    public static OATApplicationException forLibraryError(String library, Exception thrownException) {
        return new OATApplicationException("The following library has thrown an error: " + library, thrownException, OATApplicationExceptionType.LibraryError);
    }

    public static OATApplicationException forFeatureNotSupported(String name) {
        return new OATApplicationException("The following feature can not be executed because of unsatisfied requirements: " + name, OATApplicationExceptionType.FeatureNotSupported);
    }

    public static OATApplicationException forSecurityException(String name, Exception e) {
        return new OATApplicationException("The following library has thrown a SecurityException: " + name, e, OATApplicationExceptionType.SecurityException);
    }

    public static OATApplicationException forOther(String name, String msg) {
        return new OATApplicationException(name + " has thrown an unknown Exception: " + msg, OATApplicationExceptionType.Other);
    }

    public static OATApplicationException forOther(String name, String msg, Exception e) {
        return new OATApplicationException(name + " has thrown an unknown Exception: " + msg, e, OATApplicationExceptionType.Other);
    }

    public static OATApplicationException forNoSMSSubscriptionFound() {
        return new OATApplicationException("No Subscription could be obtained to send a SMS!", OATApplicationExceptionType.NoSMSSubscriptionFound);
    }

    public OATApplicationExceptionType getExceptionType() {
        return exceptionType;
    }
}