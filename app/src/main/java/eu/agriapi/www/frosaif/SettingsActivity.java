package eu.agriapi.www.frosaif;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Created by Seb on 21/03/2017.
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfos);


        // creation des menus
        Spinner spinnerRole = (Spinner) findViewById(R.id.uIrole_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterRole = ArrayAdapter.createFromResource(this,
                R.array.role_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerRole.setAdapter(adapterRole);

    }


    public void commitUserInfos(View view){
        Spinner spinnerRole = (Spinner) findViewById(R.id.uIrole_spinner);
        String uIRoleStr = spinnerRole.getSelectedItem().toString();
        TextView eventPhoneNb = (TextView)findViewById(R.id.uIPhoneNb);
        String uIPhoneStr = eventPhoneNb.getText().toString();
        TextView eventIdStr = (TextView)findViewById(R.id.uIIdentification);
        String uIIdStr = eventIdStr.getText().toString();
        TextView eventEMStr = (TextView)findViewById(R.id.uIEmailAddr);
        String uIEmailStr = eventEMStr.getText().toString();
        /*Toast.makeText(getApplicationContext(), uIIdStr+"\n"+uIEmailStr+"\n"+uIPhoneStr+"\n"
        *        +uIRoleStr, Toast.LENGTH_LONG).show();
        */
        String filename = "frosaif.txt";
        String string = "ID:"+uIIdStr+";EMAIL:"+uIEmailStr+";TEL:"+uIPhoneStr+";ROLE:"+uIRoleStr+";";
        //Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();

        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
        this.finish();
    }

}
