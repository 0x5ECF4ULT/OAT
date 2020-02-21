package at.tacticaldevc.oat.utils;

import org.junit.jupiter.api.Test;

import at.tacticaldevc.oat.exceptions.OATApplicationException;

import static org.assertj.core.api.Assertions.assertThat;

public class OATApplicationExceptionTest {
    @Test
    void testForLibraryError() {
        // test
        OATApplicationException ex = OATApplicationException.forLibraryError("test library", new Exception("test Exception"));
        // assert
        assertThat(ex).isInstanceOf(OATApplicationException.class);
        assertThat(ex.getMessage()).contains("test library");
        assertThat(ex.getCause()).hasMessage("test Exception");
    }

    @Test
    void testForNoPasswordSet() {
        // test
        OATApplicationException ex = OATApplicationException.forNoPasswordSet();
        // assert
        assertThat(ex).isInstanceOf(OATApplicationException.class);
        assertThat(ex).hasMessage("No password has been set!");
    }
}