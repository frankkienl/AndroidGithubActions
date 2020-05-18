package nl.frankkie.androidgithubactions

class Util {

    companion object {
        fun validateOnlyNonCapital(text: String): Boolean {
            return (text == text.toLowerCase())
        }
    }

}