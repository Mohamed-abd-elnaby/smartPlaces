#smartPlaces
    google maps places with smart way

 LTS 1.0.11

 Setup

	    allprojects {
         		repositories {
         			...
         			maven { url 'https://jitpack.io' }
         		}
         	}

        dependencies {
         	        implementation 'com.github.Mohamed-abd-elnaby:smartPlaces:{lastversion}'
             	}


 Using

  initialize lib with google map key and Context

          SmartPlacesInitialize.INSTANCE.apiKey = getString(R.string.apiKey)
          SmartPlacesInitialize.INSTANCE.context = context


  Start lib
          SmartPlacesInitialize.INSTANCE.startSmartPlaces(this) {
                        //using result here
                    }
