package nl.frankkie.androidgithubactions

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("nl.frankkie.androidgithubactions", appContext.packageName)
    }

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testValidationSuccessful() {
        InstrumentationTestRobot().apply {
            goToSecondFragment()
            fillEditText("abcdefghijk")
            pressValidateButton()
            checkValidationSuccessful()
        }
    }

    @Test
    fun testValidationUnsuccessful() {
        InstrumentationTestRobot().apply {
            goToSecondFragment()
            fillEditText("ASDFGYVHFJNADUF")
            pressValidateButton()
            checkValidationSuccessful()
        }
    }

    class InstrumentationTestRobot : BaseRobot() {
        fun goToSecondFragment() {
            Espresso.onView(ViewMatchers.withId(R.id.btnGoToSecondFragment))
                .perform(ViewActions.click())
        }

        fun fillEditText(text: String) {
            Espresso.onView(ViewMatchers.withId(R.id.edNonCapital))
                .perform(ViewActions.typeText(text))
        }

        fun pressValidateButton() {
            Espresso.onView(ViewMatchers.withId(R.id.btnValidate)).perform(ViewActions.click())
        }

        fun checkValidationSuccessful() {
            assertFalse(hasErrorText("error").matches(withId(R.id.edNonCapital)))
        }

        fun checkValidationUnsuccessful() {
            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
            val expectedErrorMessage =
                appContext.getString(R.string.error_text_contains_capital_letters)
            Espresso.onView(ViewMatchers.withId(R.id.edNonCapital))
                .check(ViewAssertions.matches(ViewMatchers.hasErrorText(expectedErrorMessage)))
        }
    }
}