package eu.agriapi.www.frosaif;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.drive.internal.StringListResponse;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

// Rajouter un toast au cas ou le réseau ne soit pas dispo et indiquer qu'il faut le rsx
// en pluson pourrait avoir une sauvegarde d'un point en cas de zone hors connexion
// pour un enregistrement plus tard

/**
 * Created by Seb on 16/02/2017.
 */
public class DisplayreginfoActivity extends Activity{

    //private class myEvent2reg {

    //}
    // one declare the different information corresponding to the event
    private String evPosLatStr= new String("");
    private String evPosLngStr= new String("");
    private String evAddrStr= new String("");
    private String evPhoneStr = new String("");
    private String evEmailStr= new String("");
    private String evIdStr= new String("");
    private String evRoleStr= new String("");
    private String evEventStr= new String("");
    private String evDetailStr= new String("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_info);

        Bundle bundle = getIntent().getParcelableExtra("bundle");
        LatLng eventPosition = bundle.getParcelable("eventPosition");
        TextView enventLatT = (TextView)findViewById(R.id.eventLat);
        evPosLatStr = String.valueOf(eventPosition.latitude);
        enventLatT.setText(evPosLatStr);
        TextView enventLngT = (TextView)findViewById(R.id.eventLng);
        evPosLngStr = String.valueOf(eventPosition.longitude);
        enventLngT.setText(evPosLngStr);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(eventPosition.latitude, eventPosition.longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                evAddrStr = strReturnedAddress.toString().replaceAll("[\r\n]+", " ");
                //
            } else {
                //Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.w("My Current loction address", "Canont get Address!");
        }
        // On reformatte l'adresse pour remplacer les retours chariot par des espaces
        EditText enventAddrT = (EditText) findViewById(R.id.eventAddr);
        enventAddrT.setText(evAddrStr);
        //Toast.makeText(getApplicationContext(), strAdd.replaceAll("[\r\n]+", ""), Toast.LENGTH_LONG).show();

        // creation des menus
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

    public void commitEventInformation(View view){
        // one get the information from the user and verify that the field are not empty
        Spinner spinnerEvent = (Spinner) findViewById(R.id.event_spinner);
        evEventStr = spinnerEvent.getSelectedItem().toString();
        Spinner spinnerRole = (Spinner) findViewById(R.id.role_spinner);
        evRoleStr = spinnerRole.getSelectedItem().toString();
        TextView eventPhoneNb = (TextView)findViewById(R.id.eventPhoneNb);
        evPhoneStr = eventPhoneNb.getText().toString();
        TextView eventIdStr = (TextView)findViewById(R.id.eventIdentification);
        evIdStr = eventIdStr.getText().toString();
        TextView eventEMStr = (TextView)findViewById(R.id.eventEmailAddr);
        evEmailStr = eventEMStr.getText().toString();
        TextView eventDetStr = (TextView)findViewById(R.id.eventDetails);
        evDetailStr = eventDetStr.getText().toString();
        Toast.makeText(getApplicationContext(), evPosLatStr+"\n"+evPosLngStr+"\n"+evAddrStr+"\n"+
                evIdStr+"\n"+evEmailStr+"\n"+evPhoneStr+"\n"+evRoleStr+"\n"+evEventStr+"\n"+
                evDetailStr, Toast.LENGTH_LONG).show();
    }

}



