package Fahmy.placesproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fahmy.smartplaces.features.home.SmartPlacesInitialize
import kotlinx.android.synthetic.main.activity_main.*

class PlaceFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_main, container, false)

        return view
    }

    override fun onResume() {
        super.onResume()
        button.setOnClickListener {
            SmartPlacesInitialize.INSTANCE.startSmartPlaces(requireContext()) {
                println(it)
            }

        }
    }
}