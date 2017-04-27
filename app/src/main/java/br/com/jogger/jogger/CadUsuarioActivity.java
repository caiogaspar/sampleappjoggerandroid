package br.com.jogger.jogger;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import lib.Utils;

public class CadUsuarioActivity extends MainActivity {

    private EditText mNomeView;
    private EditText mDtNascView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mCadUsuarioFormView;

    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;


    private static String url = "https://php-caiogaspar.c9users.io/cadastro_usuario.php";
    private static String msgError = "Não foi possível efetuar o cadastro.";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_usuario);

        mNomeView = (EditText) findViewById(R.id.nome);
        mDtNascView = (EditText) findViewById(R.id.data_nascimento);
        mDtNascView.setInputType(InputType.TYPE_NULL);
        mEmailView = (EditText) findViewById(R.id.email_cadastro);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptCadastro();
                    return true;
                }
                return false;
            }
        });

        Button mCadastrarButton = (Button) findViewById(R.id.cadastrar_button);
        mCadastrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCadastro();
            }
        });

        mCadUsuarioFormView = findViewById(R.id.form_cadastro);
        //mProgressView = findViewById(R.id.login_progress);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        mDtNascView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setDateTimeField();
            }
        });
    }

    private void setDateTimeField() {
        //mDtNascView.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mDtNascView.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        fromDatePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    public void attemptCadastro() {
        // Reset errors.
        mNomeView.setError(null);
        mDtNascView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String nome = mNomeView.getText().toString();
        String dtnasc = mDtNascView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(nome)) {
            mNomeView.setError(getString(R.string.error_field_required));
            focusView = mNomeView;
            cancel = true;
        }

        if (TextUtils.isEmpty(dtnasc)) {
            mDtNascView.setError(getString(R.string.error_field_required));
            focusView = mDtNascView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !Utils.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!Utils.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
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
            requestCadastro();
        }
    }

    public void requestCadastro() {
        String nome   = "";
        String dtnasc = "";
        String email  = "";
        String senha  = "";
        try {
            nome   = URLEncoder.encode(mNomeView.getText().toString(),"UTF-8");
            dtnasc = URLEncoder.encode(mDtNascView.getText().toString(),"UTF-8");
            email  = URLEncoder.encode(mEmailView.getText().toString(),"UTF-8");
            senha  = URLEncoder.encode(mPasswordView.getText().toString(),"UTF-8");
        } catch (Exception e) {
            Utils.alert(this, msgError, "");
            //showProgress(false);
            return;
        }

        String finalUrl = url +"?nm_atleta="+nome +"&dt_nascimento"+dtnasc+"&ds_email_atleta=" + email + "&nm_senha=" + senha;
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
                                Utils.alert(CadUsuarioActivity.this, msgError, "");
                                //showProgress(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Utils.alert(CadUsuarioActivity.this, msgError, "");
                            //showProgress(false);
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.alert(CadUsuarioActivity.this, msgError, "");
                        //showProgress(false);
                    }
                }
        );
        queue.add(request);
    }

}