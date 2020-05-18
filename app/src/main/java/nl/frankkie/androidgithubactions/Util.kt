package nl.frankkie.androidgithubactions

class Util {

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        fun validateOnlyNonCapital(text: String): Boolean {
            return (text == text.toLowerCase())
        }

        init {
            System.loadLibrary("native-lib")
        }
    }
}