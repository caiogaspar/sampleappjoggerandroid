package br.com.jogger.jogger;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import lib.Utils;

public class CadEventoActivity extends MainActivity {

    private EditText mNomeEventoView;
    private EditText mDescEventoView;
    private EditText mEndLogradouroView;
    private EditText mDtInicioView;
    private EditText mDtFimView;
    private Spinner mCbModalidade;
    private View mCadEventoFormView;
    private String latitude;
    private String longitude;


    private static String url = "https://php-caiogaspar.c9users.io/cadastro_evento.php";
    private static String msgError = "Não foi possível efetuar o cadastro.";

    public class Modalidade {
        private String name;
        private String id;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cad_evento);

        mNomeEventoView = (EditText) findViewById(R.id.nome_evento);
        mDescEventoView = (EditText) findViewById(R.id.desc_evento);
        //mEndLogradouroView = (EditText) findViewById(R.id.end_logradouro);
        mDtInicioView = (EditText) findViewById(R.id.data_ini_evento);
        mDtFimView = (EditText) findViewById(R.id.data_fim_evento);

        Button mCadastrarButton = (Button) findViewById(R.id.cadastrar_button);
        mCadastrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCriarEvento();
            }
        });

        mCadEventoFormView = findViewById(R.id.form_cad_evento);

        // Popula Spinner
        mCbModalidade = (Spinner) findViewById(R.id.cb_modalidade);
        requestGetModalidades();

        final TextView txtEndereco = (TextView)findViewById(R.id.txtEndereco);
        txtEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapasActivity();
            }
        });

    }

    public void attemptCriarEvento() {

        // Reset errors.
        mNomeEventoView.setError(null);
        mDescEventoView.setError(null);
        mEndLogradouroView.setError(null);
        mDtInicioView.setError(null);
        mDtFimView.setError(null);

        // Store values at the time of the login attempt.
        String nome = mNomeEventoView.getText().toString();
        String desc = mDescEventoView.getText().toString();
        String end_logradouro = mEndLogradouroView.getText().toString();
        String dt_inicio = mDtInicioView.getText().toString();
        String dt_fim = mDtFimView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(nome)) {
            mNomeEventoView.setError(getString(R.string.error_field_required));
            focusView = mNomeEventoView;
            cancel = true;
        }

        if (TextUtils.isEmpty(desc)) {
            mDescEventoView.setError(getString(R.string.error_field_required));
            focusView = mDescEventoView;
            cancel = true;
        }

        if (TextUtils.isEmpty(end_logradouro)) {
            mEndLogradouroView.setError(getString(R.string.error_field_required));
            focusView = mEndLogradouroView;
            cancel = true;
        }

        if (TextUtils.isEmpty(dt_inicio)) {
            mDtInicioView.setError(getString(R.string.error_field_required));
            focusView = mDtInicioView;
            cancel = true;
        }

        if (TextUtils.isEmpty(dt_fim)) {
            mDtFimView.setError(getString(R.string.error_field_required));
            focusView = mDtFimView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            requestCriarEvento();
        }
    }


    public void requestCriarEvento() {
        String nome = "";
        String desc = "";
        String end_logradouro = "";
        String dt_inicio = "";
        String dt_fim = "";
        int modalidade = 0;

        try {
            nome   = URLEncoder.encode(mNomeEventoView.getText().toString(), "UTF-8");
            desc   = URLEncoder.encode(mDescEventoView.getText().toString(),"UTF-8");
            end_logradouro  = URLEncoder.encode(mEndLogradouroView.getText().toString(),"UTF-8");
            dt_inicio  = URLEncoder.encode(mDtInicioView.getText().toString(),"UTF-8");
            dt_fim   = URLEncoder.encode(mDtFimView.getText().toString(), "UTF-8");
            //modalidade   = URLEncoder.encode(mCbModalidade.indexOfChild(), "UTF-8");
        } catch (Exception e) {
            Utils.alert(this, msgError, "");
            //showProgress(false);
            return;
        }

        String finalUrl = url +"?nm_evento="+nome +"&ds_evento="+desc+"&ds_logradouro="+end_logradouro+"&dt_inicio"+dt_inicio+"&dt_fim"+dt_fim;
        Log.d("br.com.jogger.jogger", finalUrl);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(finalUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("br.com.jogger.jogger", response.toString());
                        try {
                            if (response.getString("ret").contentEquals("ok")) {
                                // TODO: calling home activity
                                homeActivity();
                            } else {
                                Utils.alert(CadEventoActivity.this, msgError, "");
                                //showProgress(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Utils.alert(CadEventoActivity.this, msgError, "");
                            //showProgress(false);
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.alert(CadEventoActivity.this, msgError, "");
                        //showProgress(false);
                    }
                }
        );
        queue.add(request);
    }

    public void requestGetModalidades() {
        String finalUrl = "https://php-caiogaspar.c9users.io/get_modalidades.php";
        Log.d("br.com.jogger.jogger", finalUrl);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(finalUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("br.com.jogger.jogger", response.toString());
                        try {
                            JSONArray modalidadesJson = new JSONArray(response.toString());
                            ArrayList<String> arrayModalidades = new ArrayList<String>();
                            for (int i = 0; i < modalidadesJson.length(); i++) {
                                /*Modalidade objetoModalidade = new Modalidade();
                                objetoModalidade.setId(response.getString("Id"));
                                objetoModalidade.setName(response.getString("Nome"));*/
                                JSONObject node = modalidadesJson.getJSONObject(i);
                                arrayModalidades.add(node.getString("nm_modalidade"));
                            }
                            // Creating adapter for spinner
                            ArrayAdapter<String> dataAdapter;
                            dataAdapter = new ArrayAdapter<String>(CadEventoActivity.this, android.R.layout.simple_spinner_item, arrayModalidades);

                            // Drop down layout style - list view with radio button
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // attaching data adapter to spinner
                            mCbModalidade.setAdapter(dataAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Utils.alert(CadEventoActivity.this, msgError, "");
                            //showProgress(false);
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.alert(CadEventoActivity.this, msgError, "");
                        //showProgress(false);
                    }
                }
        );
        queue.add(request);
    }

}
