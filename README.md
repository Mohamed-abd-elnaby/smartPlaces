# #smartPlaces
 google maps places with smart way

# lastversion 1.0.4

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

        SmartPlaces.initialize(getString(R.string.google_api_key),this)


 # Start lib with (Context and FragmentManger) from activity

         SmartPlaces.start(this, supportFragmentManager, { result ->
                     result.takeIf { it != null }?.let {
                         lat = it.geometry.location.lat
                         lng = it.geometry.location.lng
                     }
                 },{
                 //onFinish call back
                 }
                 )

 # Start lib with (Context and FragmentManger) from fragment
          SmartPlaces.start(this, requireActivity().supportFragmentManager, { result ->
                               result.takeIf { it != null }?.let {
                                   lat = it.geometry.location.lat
                                   lng = it.geometry.location.lng
                               }
                           },{
                           //onFinish call back
                           }
                           )

