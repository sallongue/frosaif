package eu.agriapi.www.frosaif;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

// L'appli doit d'abord importer les marqueurs existants...
// ceux ci ne sont pas draggable et sont d'une couleur différente

// un seul marker est actif à la fois
// quand un markeur existe ul faut deux cliques pour effacer l'ancien et
// ajouter le nouveau
// on peut mettre un message qui indique ça

// ce qu'il faut maintenant c'est que l'on fasse un bouton "soumettre" pour envoyer les infos
// a la bd: position et inofs persos une nouvelle activité a laquelle on pass lat et lng

// quand on clique sur le marker affiche l'adresse?

// COMPORTEMENT:
// Si on clique sur "signalement" sans avoir positionné un marker on ne va pas plus loin...

// Si on clique sur une autre position alors qu'il existe déjà un autre marker, le clique
// d'après efface le marker courant et recrée un nouveau marker


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private UiSettings mUiSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private static final int MY_PERMISSIONS_REQUEST_GPS = 1;
    private static int MY_MARKER_DEL_CNT = 0;
    Marker myMarker = null;
    LatLng mylatLng = null;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mUiSettings = mMap.getUiSettings();
        // Add a marker in Paris Notre dame and move the camera
        LatLng notreDame = new LatLng(48.852950, 2.349899);
        mMap.addMarker(new MarkerOptions().position(notreDame).
                title(getString(R.string.default_marker)).
                snippet("center of the univers").
                draggable(true).
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                );
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(notreDame, 10));
        // les fonctions suivantes permettent d'ajouter le bouton GPS pour aller directement à maposition
        // a besoin d'être retravailler pour etre propre
        // la permission est obtenu une fois mais il faut relancer l'appli pour voir apparaitre
        // le boutton....
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Ask permission for mylocation button only once...
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GPS);
        }
        mUiSettings.setMapToolbarEnabled(false);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //Toast.makeText(getApplicationContext(), "Latitude : " + latLng.latitude + "\nLongitude : " + latLng.longitude, Toast.LENGTH_LONG).show();
                if (myMarker == null) {
                    myMarker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .draggable(true));
                } else {
                    if(MY_MARKER_DEL_CNT == 0) {
                        //Toast.makeText(getApplicationContext(), "Vous allez effacer votre ancien marqueur....", Toast.LENGTH_LONG).show();
                        MY_MARKER_DEL_CNT = 1;
                    } else {
                        MY_MARKER_DEL_CNT = 0;
                        myMarker.remove();
                        myMarker = mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .draggable(true));
                    }
                }
                mylatLng = latLng;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(), "Vous allez effacer votre ancien marqueur....", Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void registerEventInformation(View view) {
        if(mylatLng == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_marker_msg), Toast.LENGTH_LONG).show();
        } else {
            if(!isOnline()){
                Toast.makeText(getApplicationContext(), "Attention vous n'êtes pas connecté (data ou wifi),"
                        + " vos informations ne pourront pas être sauvegardée", Toast.LENGTH_LONG).show();
            } else {
                Bundle args = new Bundle();
                args.putParcelable("eventPosition", mylatLng);
                Intent intent = new Intent(this, DisplayreginfoActivity.class);
                intent.putExtra("bundle", args);
                startActivity(intent);
            }
        }
    }


}
