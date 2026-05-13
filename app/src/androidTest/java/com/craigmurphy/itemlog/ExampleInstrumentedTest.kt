package com.craigmurphy.itemlog

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

// Instrumented test example.
// Runs on an Android device or emulator.
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    // Verifies that the application context is correct.
    @Test
    fun useAppContext() {

        // Gets the app context from the running Android device/emulator.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // Confirms the package name matches the expected value.
        assertEquals("com.craigmurphy.itemlog", appContext.packageName)
    }
}