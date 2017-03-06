package org.juxshare.com.sampleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.juxshare.org.KotlinAddressEditText;
import com.juxshare.org.JavaAddressEditText;

public class MainActivity extends AppCompatActivity {

    private KotlinAddressEditText addressView;
    private JavaAddressEditText addressEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add reference to the address view
        addressView = (KotlinAddressEditText) findViewById(R.id.my_address);
        addressEditText = (JavaAddressEditText) findViewById(R.id.my_addre);
        //set the calling activity to the address view
        addressView.setMActivity(this);
        addressEditText.setCallingActivity(this);
    }


    //Important override onActivity results to get user selected results
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
}
