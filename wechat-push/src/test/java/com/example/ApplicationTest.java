package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    @Test
    void testApplication() {
        Application application = new Application();
        assertNotNull(application, "Application should be instantiated");
    }

    @Test
    void testMainMethod() {
        // Test that main method can be called without exceptions
        assertDoesNotThrow(() -> {
            Application.main(new String[]{});
        }, "Main method should execute without exceptions");
    }
}