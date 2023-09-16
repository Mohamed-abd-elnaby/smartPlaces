package Fahmy.placesproject

import Fahmy.placesproject.databinding.ActivityMainBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smartplaces.features.home.SmartPlaces

internal class MainActivity : AppCompatActivity() {
    private lateinit var bind: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.button.setOnClickListener {
            SmartPlaces.start(this, findNearbyPlaces = true, mustChoseLocation = true, maxZoomLevel = 19F, successCallback = {
                println("result from $it")
            })
        }

    }
}
