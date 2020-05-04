package at.tacticaldevc.oat.utils;

import org.junit.jupiter.api.Test;

import at.tacticaldevc.oat.exceptions.OATApplicationException;
import at.tacticaldevc.oat.exceptions.OATApplicationExceptionType;

import static org.assertj.core.api.Assertions.assertThat;

public class OATApplicationExceptionTest {
    @Test
    void forLibraryError() {
        // test
        OATApplicationException ex = OATApplicationException.forLibraryError("test library", new Exception("test Exception"));
        // assert
        assertThat(ex).isInstanceOf(OATApplicationException.class);
        assertThat(ex.getMessage()).contains("test library");
        assertThat(ex.getCause()).hasMessage("test Exception");
        assertThat(ex.getExceptionType()).isEqualTo(OATApplicationExceptionType.LibraryError);
    }

    @Test
    void forNoPasswordSet() {
        // test
        OATApplicationException ex = OATApplicationException.forNoPasswordSet();
        // assert
        assertThat(ex).isInstanceOf(OATApplicationException.class);
        assertThat(ex).hasMessage("No password has been set!");
        assertThat(ex.getExceptionType()).isEqualTo(OATApplicationExceptionType.NoPasswordSet);
    }

    @Test
    void forNoSMSSubscriptionFound() {
        // test
        OATApplicationException ex = OATApplicationException.forNoSMSSubscriptionFound();
        // assert
        assertThat(ex).isInstanceOf(OATApplicationException.class);
        assertThat(ex).hasMessage("No Subscription could be obtained to send a SMS!");
        assertThat(ex.getExceptionType()).isEqualTo(OATApplicationExceptionType.NoSMSSubscriptionFound);
    }

    @Test
    void forPasswordMismatch() {
        // test
        OATApplicationException ex = OATApplicationException.forPasswordMismatch();
        // assert
        assertThat(ex).isInstanceOf(OATApplicationException.class);
        assertThat(ex).hasMessage("The supplied old password did not match the saved password. The password was not updated.");
        assertThat(ex.getExceptionType()).isEqualTo(OATApplicationExceptionType.PasswordMismatch);
    }

    @Test
    void forCorruptedPasswordHash() {
        // test
        OATApplicationException ex = OATApplicationException.forCorruptedPasswordHash();
        // assert
        assertThat(ex).isInstanceOf(OATApplicationException.class);
        assertThat(ex).hasMessage("The password hash was corrupted. The password was deleted and a new password has to be set in the Application Settings.");
        assertThat(ex.getExceptionType()).isEqualTo(OATApplicationExceptionType.CorruptedPasswordHash);
    }

    @Test
    void forLibraryDeprecated() {
        // prepare
        String testLibrary = "Test library";
        Exception testException = new Exception(("Test exception"));
        // test
        OATApplicationException ex = OATApplicationException.forLibraryDeprecatedError(testLibrary, testException);
        // assert
        assertThat(ex).isInstanceOf(OATApplicationException.class);
        assertThat(ex).hasMessage("The following library seems to no longer support a required feature: " + testLibrary);
        assertThat(ex.getExceptionType()).isEqualTo(OATApplicationExceptionType.LibraryDeprecated);
    }

    @Test
    void forFeatureNotSupported() {
        // prepare
        String testFeature = "Test feature";
        // test
        OATApplicationException ex = OATApplicationException.forFeatureNotSupported(testFeature);
        // assert
        assertThat(ex).isInstanceOf(OATApplicationException.class);
        assertThat(ex).hasMessage("The following feature can not be executed because of unsatisfied requirements: " + testFeature);
        assertThat(ex.getExceptionType()).isEqualTo(OATApplicationExceptionType.FeatureNotSupported);
    }

    @Test
    void forSecurityException() {
        // prepare
        String testLibrary = "Test library";
        Exception testException = new Exception(("Test exception"));
        // test
        OATApplicationException ex = OATApplicationException.forSecurityException(testLibrary, testException);
        // assert
        assertThat(ex).isInstanceOf(OATApplicationException.class);
        assertThat(ex).hasMessage("The following library has thrown a SecurityException: " + testLibrary);
        assertThat(ex.getExceptionType()).isEqualTo(OATApplicationExceptionType.SecurityException);
    }
}
