package Fahmy.placesproject

import Fahmy.placesproject.databinding.FragmentMainBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smartplaces.features.home.SmartPlaces

internal class PlaceFragment : Fragment() {
    private lateinit var bind: FragmentMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bind = FragmentMainBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onResume() {
        super.onResume()
        bind.button.setOnClickListener {
            SmartPlaces.start(requireActivity(), mustChoseLocation = true, successCallback = {
                println("result from $it")
            })

        }
    }
}