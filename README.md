# AddressEditText
Android address edittext is a combination of custom edittext view and google place piker widget developed using kotlin and java

To use simply add to project

      compile project(':addressedittext')
      
Then add to xml 

for those who want kotlin version add this
        
	
	<com.juxshare.org.KotlinAddressEditText
        android:id="@+id/my_address"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginEnd="16dp"
        android:layout_marginTop="10dp"
        android:background="@drawable/edit_text_bg"
        android:padding="4dp"
        address:ae_drawable_end="@drawable/ic_my_location"
        address:ae_drawable_start="@drawable/ic_place"
        address:ae_hint="Enter your address" />
        
 if you want java version you can add this
 
        <com.juxshare.org.JavaAddressEditText
        android:id="@+id/my_addre"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginEnd="16dp"
        android:layout_marginTop="10dp"
        android:background="@null"
        android:padding="4dp"
        address:ae_drawable_end="@drawable/ic_my_location"
        address:ae_drawable_start="@drawable/ic_place"
        address:ae_hint="Enter your address" />
	
In your activity add this code

	//add reference to the address view
        addressView = (KotlinAddressEditText) findViewById(R.id.my_address);
        addressEditText = (JavaAddressEditText) findViewById(R.id.my_addre);
        //set the calling activity to the address view
        addressView.setMActivity(this);
        addressEditText.setCallingActivity(this);
	
	
To get user results add onActivityResult in your activity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == KotlinAddressEditText.Code.getREQUEST_CODE_AUTOCOMPLETE()){
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);

                //set the address to the address edit text
                addressView.setAddressText(place.getAddress().toString());

                //getting the address
                addressView.getAddressText();

                Log.i(MainActivity.class.getSimpleName(), "Place Selected: " + place.getName());
            }
        }

        if (requestCode == JavaAddressEditText.REQUEST_CODE_AUTOCOMPLETE){
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);

                //set the address to the address edit text
                addressEditText.setAddressText(place.getAddress().toString());

                //getting the address
                addressEditText.getAddressText();

                Log.i(MainActivity.class.getSimpleName(), "Place Selected: " + place.getName());
            }
        }
    }


lastly add your google api key to manifest

	<meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="YOUR_GOOGLE_API_KEY"/>

Attributes

	address:ae_drawable_end="@drawable/ic_my_location"
        address:ae_drawable_start="@drawable/ic_place"
        address:ae_hint="Enter your address"
	


