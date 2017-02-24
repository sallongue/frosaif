package eu.agriapi.www.frosaif;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

// Rajouter des propriétés sur les edittext pour que le clavier soit adapté au type
// de données

/**
 * Created by Seb on 16/02/2017.
 */
public class DisplayreginfoActivity extends Activity{

    //private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {

      //  return strAdd;
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_info);

        Bundle bundle = getIntent().getParcelableExtra("bundle");
        LatLng eventPosition = bundle.getParcelable("eventPosition");
        TextView enventLatT = (TextView)findViewById(R.id.eventLat);
        enventLatT.setText(String.valueOf(eventPosition.latitude));
        TextView enventLngT = (TextView)findViewById(R.id.eventLng);
        enventLngT.setText(String.valueOf(eventPosition.longitude));

        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(eventPosition.latitude, eventPosition.longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                //
            } else {
                //Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.w("My Current loction address", "Canont get Address!");
        }
        EditText enventAddrT = (EditText) findViewById(R.id.eventAddr);
        enventAddrT.setText(strAdd.replaceAll("[\r\n]+", " "));
        Toast.makeText(getApplicationContext(), strAdd.replaceAll("[\r\n]+", ""), Toast.LENGTH_LONG).show();

        Spinner spinnerRole = (Spinner) findViewById(R.id.role_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterRole = ArrayAdapter.createFromResource(this,
               R.array.role_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerRole.setAdapter(adapterRole);

        Spinner spinnerEvent = (Spinner) findViewById(R.id.event_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterEvent = ArrayAdapter.createFromResource(this,
                R.array.event_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterEvent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerEvent.setAdapter(adapterEvent);

    }

}



