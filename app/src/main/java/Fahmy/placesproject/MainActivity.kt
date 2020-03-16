package Fahmy.placesproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fahmy.smartplaces.features.home.SmartPlaces
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {

            SmartPlaces.start(this, supportFragmentManager, {
                println(it)
            },{
                finish()
            })
        }

    }
}
