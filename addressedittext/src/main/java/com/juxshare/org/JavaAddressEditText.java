package com.juxshare.org;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.juxshare.org.addressedittext.R;

/**
 * Created by TIDO on 3/6/2017.
 */

public class JavaAddressEditText extends LinearLayout {


    private static final int INVALID_RESOURCE_ID = -1;
    public static final int REQUEST_CODE_AUTOCOMPLETE = 11313;
    EditText address;
    ImageButton imageButton;

    int addressDrawableStart = INVALID_RESOURCE_ID;
    int addressDrawableEnd = INVALID_RESOURCE_ID;
    String addressHint;
    Context parent;


    public JavaAddressEditText(Context context) {
        super(context);
    }

    public JavaAddressEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);
        initialize();
    }

    public JavaAddressEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(attrs);
        initialize();
    }

    private void setAttributes(AttributeSet attributes) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributes,
                R.styleable.address_edit_text);

        addressDrawableEnd = typedArray.getResourceId(R.styleable.address_edit_text_ae_drawable_end, INVALID_RESOURCE_ID);
        addressDrawableStart = typedArray.getResourceId(R.styleable.address_edit_text_ae_drawable_start, INVALID_RESOURCE_ID);
        addressHint = typedArray.getString(R.styleable.address_edit_text_ae_hint);

        typedArray.recycle();

    }

    void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.address_edit_text_view, this, true);
        address = (EditText) findViewById(R.id.ed_address);
        imageButton = (ImageButton) findViewById(R.id.btn_location);
        if (addressDrawableStart != INVALID_RESOURCE_ID){
            address.setCompoundDrawablesWithIntrinsicBounds(addressDrawableStart,0 ,0,0);
        }else
            throw new IllegalArgumentException("DrawableStart resource is required");

        if( addressDrawableEnd != INVALID_RESOURCE_ID){
            imageButton.setImageResource(addressDrawableEnd);
        }else
            throw new IllegalArgumentException("DrawableEnd resource is required");

        address.setHint(addressHint);
        address.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity();
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();


    }

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .setCountry("ZA")
                    .build();

            if (parent instanceof AppCompatActivity) {
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                        .setBoundsBias(new LatLngBounds(new LatLng(24.675, -28.483333),
                                new LatLng(32.883333, -22.133333)))
                        .setFilter(typeFilter)
                        .build((AppCompatActivity)parent);
                ((AppCompatActivity)parent).startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            } else if(parent instanceof FragmentActivity) {
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                        .setBoundsBias(new LatLngBounds(new LatLng(24.675, -28.483333),
                                new LatLng(32.883333, -22.133333)))
                        .setFilter(typeFilter)
                        .build((FragmentActivity)parent);
                ((FragmentActivity)parent).startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            }

        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            if (parent instanceof AppCompatActivity){
                GoogleApiAvailability.getInstance().getErrorDialog((AppCompatActivity)parent, e.getConnectionStatusCode(),
                        0 /* requestCode */).show();
            }else if(parent instanceof FragmentActivity){
                GoogleApiAvailability.getInstance().getErrorDialog((FragmentActivity)parent, e.getConnectionStatusCode(),
                        0 /* requestCode */).show();
            }
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(JavaAddressEditText.class.getSimpleName(), message);
        }
    }

    /**
     * Sets calling parent AppCompatActivity/ FragmentActivity
     * @param context
     */
    public void setCallingActivity(@NonNull Context context){
        this.parent = context;
    }

    public String getAddressText() {
        return address.getText().toString().trim();
    }

    public void setAddressText(@NonNull String result){
        address.setText(result);
    }
}
