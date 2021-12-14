# smartPlaces

    google maps places with smart way

LTS 1.0.21

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

	  SmartPlacesInitialize.startSmartPlaces(Require Context) {
                        //using result here
                    }
