package eu.agriapi.www.frosaif;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

/**
 * Created by Seb on 22/03/2017.
 */
public class HelpActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.help);

        String versionName = "";

        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        TextView helpTextView = (TextView) findViewById(R.id.help_text_view);

        String helpText = String.valueOf(Html.fromHtml("<h1>Frosaif, Version " + versionName + "</h1>"
                + getString(R.string.about_text)));
        helpTextView.setText(helpText);
    }
}
