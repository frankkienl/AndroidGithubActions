package nl.frankkie.androidgithubactions

import android.app.Activity
import android.view.View
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import java.util.concurrent.TimeoutException

open class BaseRobot {
    var useSleeps = true
    fun sleep(mills: Long) {
        if (useSleeps) {
            Thread.sleep(mills)
        }
    }

    companion object {
        const val SLEEP_SHORT = 150L
        const val SLEEP_LONG = 500L
    }


    /**
     * Perform action of waiting for a specific view id.
     * @param viewId The id of the view to wait for.
     * @param millis The timeout of until when to wait for.
     * https://stackoverflow.com/q/49796132/1398449
     */
    fun waitId(viewId: Int, millis: Long): ViewAction? {
        return object : ViewAction {

            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isRoot()
            }

            override fun getDescription(): String {
                return "wait for a specific view with id <$viewId> during $millis millis."
            }

            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + millis
                val viewMatcher: Matcher<View> = ViewMatchers.withId(viewId)
                do {
                    for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return
                        }
                    }
                    uiController.loopMainThreadForAtLeast(50)
                } while (System.currentTimeMillis() < endTime)
                throw PerformException.Builder()
                    .withActionDescription(description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(TimeoutException())
                    .build()
            }
        }
    }

    /**
     * Perform action of waiting for a specific view with text.
     * @param viewTest The text of the view to wait for.
     * @param millis The timeout of until when to wait for.
     * https://stackoverflow.com/q/49796132/1398449
     */
    fun waitText(viewText: String, millis: Long): ViewAction? {
        return object : ViewAction {

            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isRoot()
            }

            override fun getDescription(): String {
                return "wait for a specific view with text <$viewText> during $millis millis."
            }

            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + millis
                val viewMatcher: Matcher<View> = ViewMatchers.withText(viewText)
                do {
                    for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return
                        }
                    }
                    uiController.loopMainThreadForAtLeast(50)
                } while (System.currentTimeMillis() < endTime)
                throw PerformException.Builder()
                    .withActionDescription(description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(TimeoutException())
                    .build()
            }
        }
    }

    /**
     * Check if a number is at least some other number
     * @param numberOrHigher this or higher
     */
    fun atLeast(numberOrHigher: Int): Matcher<Int?> {
        return object : TypeSafeMatcher<Int>() {
            override fun describeTo(description: Description?) {
                description?.appendText(" at least this ($numberOrHigher) number ")
            }

            override fun matchesSafely(item: Int?): Boolean {
                if (item == null) {
                    return false
                }
                return item >= numberOrHigher
            }

        }
    }

    /**
     * Get current activity, could be different from the Rule.
     * https://stackoverflow.com/a/58684943/1398449
     */
    fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        InstrumentationRegistry.getInstrumentation()
            .runOnMainSync { run { currentActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(
                Stage.RESUMED).elementAtOrNull(0) } }
        return currentActivity
    }

    /**
     * Wait for a certain condition to be true
     * @param millis timeout in milliseconds
     * @param predicate assertion
     * @throws TimeoutException when not true after millis
     */
    fun waitPredicate(millis: Long = 9000, predicate: () -> Boolean) {
        val startTime = System.currentTimeMillis()
        val endTime = startTime + millis
        do {
            if (predicate.invoke()) {
                return
            }
            sleep(SLEEP_SHORT)
        } while (System.currentTimeMillis() < endTime)
        throw TimeoutException("waited for $millis but predicate still false")
    }
}