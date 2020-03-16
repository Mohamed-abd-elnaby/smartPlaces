# smartGooglePlayPlaces
Setup google maps places with smart way


	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

	dependencies {
	        implementation 'com.github.Mohamed-abd-elnaby:smartPlaces:1.0.2'
    	}


 Using

 #initialize lib with google map key and Context

        SmartPlaces.initialize(getString(R.string.google_api_key),this)


 #Start lib with Context and FragmentManger

         SmartPlaces.start(this, supportFragmentManager, { result ->
                     result.takeIf { it != null }?.let {
                         lat = it.geometry.location.lat
                         lng = it.geometry.location.lng
                     }
                 },{
                 //onFinish call back
                 }
                 )



