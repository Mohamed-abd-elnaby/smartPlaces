# #smartPlaces
 google maps places with smart way

# lastversion 1.0.6

# Setup

	    allprojects {
         		repositories {
         			...
         			maven { url 'https://jitpack.io' }
         		}
         	}

        dependencies {
         	        implementation 'com.github.Mohamed-abd-elnaby:smartPlaces:{lastversion}'
             	}


# Using

 # initialize lib with google map key and Context

          SmartPlacesInitialize.INSTANCE.apiKey = getString(R.string.apiKey)


 # Start lib 

          SmartPlacesInitialize.INSTANCE.startSmartPlaces(this) {
                        //using result here
                    }
