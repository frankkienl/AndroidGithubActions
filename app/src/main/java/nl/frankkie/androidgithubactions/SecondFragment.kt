package nl.frankkie.androidgithubactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_second.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnValidate.setOnClickListener {
            val text = edNonCapital.text.toString()
            if (Util.validateOnlyNonCapital(text)){
                Toast.makeText(activity, "Validation successful", Toast.LENGTH_LONG).show()
                edNonCapital.error = null
            } else {
                Toast.makeText(activity, "Validation unsuccessful", Toast.LENGTH_LONG).show()
                edNonCapital.error = getString(R.string.error_text_contains_capital_letters)
            }
        }
    }
}


