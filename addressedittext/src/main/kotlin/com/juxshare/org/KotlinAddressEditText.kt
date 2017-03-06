package com.juxshare.org

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.juxshare.org.addressedittext.R
import kotlinx.android.synthetic.main.address_edit_text_view.view.*


/**
 * Created by TIDO on 3/5/2017.
 */

class KotlinAddressEditText : LinearLayout {



    object Code{
      @JvmStatic val REQUEST_CODE_AUTOCOMPLETE = 11313
    }

    constructor(context: Context) : super(context) {
        initialize(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context, attrs)
    }

    private fun initialize(context: Context, attributeSet: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.address_edit_text_view, this, true)

        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.address_edit_text)
        try {
            val hint: String = attributes.getString(R.styleable.address_edit_text_ae_hint)
            val drawableStart: Int = attributes.getResourceId(R.styleable.address_edit_text_ae_drawable_start,
                    R.drawable.ic_place)
            val locationIcon: Int = attributes.getResourceId(R.styleable.address_edit_text_ae_drawable_end,
                    R.drawable.ic_my_location)

            ed_address.hint = hint
            ed_address.setCompoundDrawablesWithIntrinsicBounds(drawableStart, 0, 0, 0)
            btn_location.setImageResource(locationIcon)
            ed_address.background.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            attributes.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        btn_location.setOnClickListener {
            showAddressSearchWidget()
        }
    }

    var addressText: String?
        get() = ed_address.text.toString()
        set(value) {
            if (!TextUtils.isEmpty(value))
                ed_address.setText(value)
            else
                return
        }

    var mActivity: AppCompatActivity? = null
        get() = field
        set(value) {
            if (value != null)
                field = value
        }


    private fun showAddressSearchWidget(): Unit {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            if (mActivity != null){
                val typeFilter = AutocompleteFilter.Builder()
                        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                        .setCountry("ZA")
                        .build()

                val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                        .setBoundsBias(LatLngBounds(LatLng(24.675, -28.483333),
                                LatLng(32.883333, -22.133333)))
                        .setFilter(typeFilter)
                        .build(mActivity)
                mActivity?.startActivityForResult(intent, Code.REQUEST_CODE_AUTOCOMPLETE)
            } else{
                throw IllegalArgumentException("Calling activity not found")
            }

        } catch (e: GooglePlayServicesRepairableException) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(mActivity, e.connectionStatusCode,
                    0 /* requestCode */).show()
        } catch (e: GooglePlayServicesNotAvailableException) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            val message = "Google Play Services is not available: " + GoogleApiAvailability.getInstance().getErrorString(e.errorCode)

            Log.e(KotlinAddressEditText::class.simpleName, message)
        }

    }
}
