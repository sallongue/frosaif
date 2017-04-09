package eu.agriapi.www.frosaif;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.drive.internal.StringListResponse;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// Rajouter un toast au cas ou le réseau ne soit pas dispo et indiquer qu'il faut le rsx
// en pluson pourrait avoir une sauvegarde d'un point en cas de zone hors connexion
// pour un enregistrement plus tard

/**
 * Created by Seb on 16/02/2017.
 */
public class DisplayreginfoActivity extends AppCompatActivity {

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
    private String evEventCode= new String("");

    RequestQueue requestQueue;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.userInfos:
                //Toast.makeText(getApplicationContext(), "Info user", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.about:
                //Toast.makeText(getApplicationContext(), "Help", Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(this, HelpActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // in inStr string search return toto where str:toto;
    public String searchKW(String str, String inStr) {
        int idxStart = inStr.indexOf(str);
        int idxStop = inStr.indexOf(";", idxStart + str.length() + 1);
        String ret = inStr.substring(idxStart + str.length() + 1, idxStop);
        //Toast.makeText(getApplicationContext(), ret, Toast.LENGTH_LONG).show();
        return ret;
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        String filename = "frosaif.txt";

        try {
            FileInputStream inStream = openFileInput(filename);
            InputStreamReader inputStrReader = new InputStreamReader(inStream);
            BufferedReader bufReader = new BufferedReader(inputStrReader);

            StringBuilder finalString = new StringBuilder();
            String oneLine;

            while ((oneLine = bufReader.readLine()) != null) {
                finalString.append(oneLine);
            }

            bufReader.close();
            inStream.close();
            inputStrReader.close();
            String id = searchKW("ID",finalString.toString());
            String email = searchKW("EMAIL", finalString.toString());
            String tel = searchKW("TEL",finalString.toString());
            String role = searchKW("ROLE",finalString.toString());
            Spinner spinnerRole = (Spinner) findViewById(R.id.role_spinner);
            EditText enventIdT = (EditText) findViewById(R.id.eventIdentification);
            enventIdT.setText(id);
            EditText enventEADT = (EditText) findViewById(R.id.eventEmailAddr);
            enventEADT.setText(email);
            EditText enventPhoneT = (EditText) findViewById(R.id.eventPhoneNb);
            enventPhoneT.setText(tel);
            //Toast.makeText(getApplicationContext(), "id: "+ id + " email: "+ email + " tel: "+ tel +" role: "+role, Toast.LENGTH_LONG).show();
            if(role.contains("Referent") == true) {
                spinnerRole.setSelection(1);
            } else if(role.contains("Societe 3D") == true){
                spinnerRole.setSelection(2);
            } else if(role.contains("Particulier") == true){
                spinnerRole.setSelection(3);
            } else if(role.contains("Service Public") == true){
                spinnerRole.setSelection(4);
            } else if(role.contains("Autre") == true){
                spinnerRole.setSelection(5);
            } else spinnerRole.setSelection(0);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_info);


        
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);

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

        // TEMPORAIRE
        //EditText enventIdT = (EditText) findViewById(R.id.eventIdentification);
        //enventIdT.setText("Alain Goulnik");
        //EditText enventEADT = (EditText) findViewById(R.id.eventEmailAddr);
        //enventEADT.setText("alain.goulnik@frosaif.fr");
        //EditText enventPhoneT = (EditText) findViewById(R.id.eventPhoneNb);
        //enventPhoneT.setText("0634353637");
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
        // One HAVE to create async to send information



        // translate event into code

        switch (evEventStr) {
            case "Signalement Nid primaire":
                evEventCode = "SNP"; break;
            case"Signalement Nid secondaire":
                evEventCode = "SNS"; break;
            case"Destruction Nid primaire":
                evEventCode = "DNP"; break;
            case"Destruction Nid secondaire":
                evEventCode = "DNS"; break;
            case"Descente du nid apres destruction":
                evEventCode = "DNAD"; break;
            case"Incident":
                evEventCode = "INC"; break;
            case"Attaque de rucher":
                evEventCode = "ADR"; break;
            case"Attaque de personne":
                evEventCode = "ADP"; break;
            case"Frelon isolé":
                evEventCode = "FRI"; break;
            case"Information":
                evEventCode = "INF"; break;
        }

        String addUrl = "http://www.frosaif.fr/zandroid.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                addUrl, null,  new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response){
                    try{
                        String status = response.getString("status");
                        String total = response.getString("total");
                        Toast.makeText(getApplicationContext(),status + "   " + total, Toast.LENGTH_LONG).show();
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {

                }
        });
        //requestQueue.add(jsonObjectRequest);

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://www.frosaif.fr/doitnext.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        if (response.contains("success")){
                            Toast.makeText(getApplicationContext(), "Evènement enregistré avec succès", Toast.LENGTH_LONG).show();
                            int ind = response.indexOf("tag");
                            int tagLength = 6;
                            // json response is "tag":"DDD444"
                            // TODO use json object to get the tag value
                            String tagStr = response.substring(ind+6, ind+6+tagLength);
                            Log.d("mytag ", tagStr);
                        } else {
                            Toast.makeText(getApplicationContext(), "Erreur dans l'enregistrement", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("lon", evPosLngStr);
                params.put("lat", evPosLatStr);
                params.put("address",evAddrStr);
                params.put("event", evEventCode);
                params.put("role", evRoleStr);
                params.put("email",evEmailStr);
                params.put("nom", evIdStr);
                params.put("tel", evPhoneStr);
                params.put("details",evDetailStr);
                params.put("android","1");

                return params;
            }
        };
        requestQueue.add(postRequest);



    }

}



