package at.tacticaldevc.oat.exceptions;

public enum OATApplicationExceptionType {
    Other,
    NoPasswordSet,
    PasswordMismatch,
    CorruptedPasswordHash,
    LibraryDeprecated,
    LibraryError,
    FeatureNotSupported,
    SecurityException,
    NoSMSSubscriptionFound
}
