# smartPlaces

    google maps places with smart way

LTS 1.0.33

Setup

        dependencies { 
             implementation 'com.github.Mohamed-abd-elnaby:smartPlaces:{lastversion}' 
            }
		
	    Define Configurations in values xml as below just
            
            <!-- Google Map Key with Android SDK && Android Places -->
            <string name="google_maps_key">Your Map Key</string>
            <!-- RecycleView height size -->
            <dimen name="locationsRecycleViewHeight"> </dimen>
            <!-- Chose Location Button Text Color -->
            <color name="choseLocationButtonTextColor">Your Color</color>
            <!-- Chose Location Button Background Color -->
            <color name="choseLocationButtonBackgroundColor">Your Color</color>
            <!-- RecycleView Adapter Item TextView Color -->
            <color name="recycleViewAdapterItemTextViewColor">Your Color</color>
            <!-- RecycleView Adapter Item Background Color -->
            <color name="recycleViewAdapterItemBackgroundColor">Your Color</color>
            <!-- RecycleView Background Color -->
            <color name="recycleViewBackgroundColor">Your Color</color>
            <!-- Loading Animation Color -->
            <color name="loadingColor">Your Color</color>
            <!-- Chose Location Button Text String -->
            <string name="choseLocationButtonText">Your Text</string>

Using
      
	  SmartPlacesInitialize.fun start(
            activity: Activity,
            findNearbyPlaces: Boolean = false,
            mustChoseLocation: Boolean = false,
            maxZoomLevel: Float = 16F,
            minZoomLevel: Float = 9F,
            autoLocationZoomLevel: Float = 15F,
            useProgressView: Boolean = true,
            successCallback: (Result?) -> Unit,
            errorCallback: ((String) -> Unit)? = null
        )
      //using findNearbyPlaces with true you will using google places APi with fees and must be enable
      //using mustChoseLocation with true will prevent user to close page without chose location
      //using maxZoomLevel,minZoomLevel,autoLocationZoomLevel to control map behaviour
      //using useProgressView with false to stop using progress dailog
      //using successCallback recieve result
      //using errorCallback recieve error message
