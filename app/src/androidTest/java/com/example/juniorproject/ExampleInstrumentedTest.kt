package com.example.juniorproject

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.juniorproject.ui.MainActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
//@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.example.juniorproject", appContext.packageName)
//    }

    @Test
    fun serverDataTest(){
        val main = Mockito.mock(MainActivity::class.java)
        Mockito.`when`(main.getServerData())
//        assertEquals("success")
    }
}