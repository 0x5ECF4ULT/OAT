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

    public static OATApplicationException forLibraryError(String library, Exception thrownException) {
        return new OATApplicationException("The following library seems to no longer support a required feature: " + library, thrownException);
    }

    public static OATApplicationException forNoSMSSubscriptionFound() {
        return new OATApplicationException("No Subscription could be obtained to send a SMS!");
    }
}
