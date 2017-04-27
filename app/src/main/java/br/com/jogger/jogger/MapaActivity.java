package br.com.jogger.jogger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MapaActivity extends MainActivity implements LocationListener {

    private GoogleMap googleMap;
    private LocationManager mLocationManager;
    private EditText txtEnderecoMapa;
    private EditText txtLatitude;
    private EditText txtLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_cadastro_evento);

        txtEnderecoMapa = (EditText)findViewById(R.id.txtEnderecoMapa);
        txtLatitude = (EditText)findViewById(R.id.txtLatitudeMapa);
        txtLongitude = (EditText)findViewById(R.id.txtLongitudeMapa);
        initializeMap();
        //getCurrentLocation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("txtEnderecoMapa",txtEnderecoMapa.getText().toString());
        returnIntent.putExtra("txtLatitude",txtLatitude.getText().toString());
        returnIntent.putExtra("txtLongitude",txtLongitude.getText().toString());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

        super.onBackPressed();
    }

    private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            googleMap.setMyLocationEnabled(true);

            googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location arg0) {
                    // TODO Auto-generated method stub

                    AddMarkerMapa(arg0.getLatitude(), arg0.getLongitude(), "Local do Evento", "Arraste o ponto para onde sera  o local do evento.");
                    googleMap.setMyLocationEnabled(false);
                }
            });
            googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker arg0) {
                    // TODO Auto-generated method stub
                    Log.d("System out", "onMarkerDragStart..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
                }

                @SuppressWarnings("unchecked")
                @Override
                public void onMarkerDragEnd(Marker arg0) {
                    // TODO Auto-generated method stub
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(MapaActivity.this, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(arg0.getPosition().latitude, arg0.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                        Log.d("System out", "onMarkerDragEnd..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
                        txtLatitude.setText(String.valueOf(arg0.getPosition().latitude));
                        txtLongitude.setText(String.valueOf(arg0.getPosition().longitude));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(arg0.getPosition(), 17.0f));
                        Toast.makeText(MapaActivity.this, address + " - " + city + " - " + state + " - " + country, Toast.LENGTH_LONG).show();
                        txtEnderecoMapa.setText(address + " - " + city + " - " + state + " - " + country);
                    } catch (Exception e) {


                    }


                }

                @Override
                public void onMarkerDrag(Marker arg0) {
                    // TODO Auto-generated method stub
                    Log.i("System out", "onMarkerDrag...");
                }
            });


            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Desculpe, o app não pôde criar mapa no momento :(", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    void getCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
            // Do something with the recent location fix
            //  otherwise wait for the update below
            Log.d("br.com.jogger.jogger","getLastKnowLocation");
            AddMarkerMapa(location.getLatitude(), location.getLongitude(), "Local do Evento", "Arraste o ponto para onde sera  o local do evento.");
        }
        else {
            Log.d("br.com.jogger.jogger","Request location updates");
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

    }

    public void onLocationChanged(Location location) {
        Log.d("br.com.jogger.jogger","Entrou location changed");
        if (location != null) {
            AddMarkerMapa(location.getLatitude(), location.getLongitude(), "Local do Evento", "Arraste o ponto para onde sera  o local do evento.");
            mLocationManager.removeUpdates(this);
        }
    }

    // Required functions
    public void onProviderDisabled(String arg0) {}
    public void onProviderEnabled(String arg0) {}
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}

    protected void AddMarkerMapa(double latitude, double longitude, String title, String snippet)
    {
        if (googleMap != null)
        {
            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(title).snippet(snippet).draggable(true);
            googleMap.addMarker(marker);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(latitude, longitude)).zoom(12).build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
}
