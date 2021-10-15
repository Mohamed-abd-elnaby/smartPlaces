#smartPlaces
    google maps places with smart way

 LTS 1.0.15

 Setup

	    allprojects {
         		repositories {
         			...
         			maven { url 'https://jitpack.io' }
         		}
         	}

        dependencies {
         	        implementation 'com.github.Mohamed-abd-elnaby:smartPlaces:{lastversion}'
			implementation 'com.android.volley:volley:{lastversion}'
             	}
		
	Google Maps Key define Strings.xml with name "google_maps_key"
	


 Using

  initialize lib with google map key and Context in Application Class

          SmartPlacesInitialize.INSTANCE.apiKey = getString(R.string.google_maps_key)
          SmartPlacesInitialize.INSTANCE.context = context

  Start lib
          SmartPlacesInitialize.INSTANCE.startSmartPlaces(this) {
                        //using result here
                    }
