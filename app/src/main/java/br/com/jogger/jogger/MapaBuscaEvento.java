package br.com.jogger.jogger;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import lib.Utils;


public class MapaBuscaEvento extends MainActivity  {

    private GoogleMap googleMap;
    private LocationManager mLocationManager;
    private EditText txtEnderecoMapa;
    private EditText txtLatitude;
    private EditText txtLongitude;
    private String dtInicio;
    private String dtFim;
    private String modalidades;
    private String latitude;
    private String longitude;
    private String distancia;
    private String tipoEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_eventos);

        txtEnderecoMapa = (EditText)findViewById(R.id.txtEnderecoMapaBusca);
        txtLatitude = (EditText)findViewById(R.id.txtLatitudeMapaBusca);
        txtLongitude = (EditText)findViewById(R.id.txtLongitudeMapaBusca);

        modalidades = getIntent().getStringExtra("modalidades");
        distancia = getIntent().getStringExtra("distancia");
        dtInicio = getIntent().getStringExtra("dtInicio");
        dtFim = getIntent().getStringExtra("dtFim");
        tipoEvento = getIntent().getStringExtra("tipoEvento");
        initializeMap();
        //getCurrentLocation();
    }

    public void requestGetEventos(String dtInicio, String dtFim, String modalidades, String latitude, String longitude, String distancia, String tipoEvento) {
        try {
            dtInicio = URLEncoder.encode(dtInicio, "UTF-8");
            dtFim = URLEncoder.encode(dtFim, "UTF-8");
            modalidades = URLEncoder.encode(modalidades, "UTF-8");
            latitude = URLEncoder.encode(latitude, "UTF-8");
            longitude = URLEncoder.encode(longitude, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String finalUrl = "https://php-caiogaspar.c9users.io/busca_eventos.php?dt_inicio=" +
                dtInicio +
                "&dt_fim=" + dtFim +
                "&modalidades=" + modalidades +
                "&ds_latitude=" + latitude +
                "&ds_longitude=" + longitude +
                "&distancia=" + distancia +
                "&tipo_evento=" + tipoEvento;
        Log.d("br.com.jogger.jogger", finalUrl);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(finalUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("br.com.jogger.jogger", response.toString());
                        try {
                            JSONArray eventosJson = response.getJSONArray("eventos");
                            for (int i = 0; i < eventosJson.length(); i++) {
                                JSONObject node = eventosJson.getJSONObject(i);
                                AddMarkerMapa(node.getDouble("ds_latitude"), node.getDouble("ds_longitude"), node.getString("nm_evento"), node.getString("ds_logradouro") + "\n - De:" + node.getString("dt_inicio") + " até " + node.getString("dt_fim"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Utils.alert(MapaBuscaEvento.this, "Não foi possível retornar os eventos.", "");
                            //showProgress(false);
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.alert(MapaBuscaEvento.this, "Não foi possível retornar os eventos.", "");
                        //showProgress(false);
                    }
                }
        );
        queue.add(request);
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
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

        super.onBackPressed();
    }

    private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.mapBuscaEvento)).getMap();

            googleMap.setMyLocationEnabled(true);

            googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location arg0) {
                    // TODO Auto-generated method stub
                    requestGetEventos(dtInicio, dtFim, modalidades, String.valueOf(arg0.getLatitude()), String.valueOf(arg0.getLongitude()), distancia, tipoEvento);
                    googleMap.setMyLocationEnabled(false);
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

    // Required functions
    public void onProviderDisabled(String arg0) {}
    public void onProviderEnabled(String arg0) {}
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}

    protected void AddMarkerMapa(double latitude, double longitude, String title, String snippet)
    {
        if (googleMap != null)
        {
            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(title).snippet(snippet);
            googleMap.addMarker(marker);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(latitude, longitude)).zoom(12).build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
}
