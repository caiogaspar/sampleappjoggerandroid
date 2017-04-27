package br.com.jogger.jogger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lib.Utils;


public class HomeActivity extends MainActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private FragmentManager fragmentManager;
    private String txtEnderecoMapa;
    private String txtLatitude;
    private String txtLongitude;
    private String modalidadeSelecionadas;
    private String codigosModalidadesSelecionadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        final Button btnAdicionarEventos = (Button)findViewById(R.id.btnAdicionarEvento);
        btnAdicionarEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNavigationDrawerItemSelected(1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                if (data.getStringExtra("txtLatitude") != null) {
                    txtEnderecoMapa = data.getStringExtra("txtEnderecoMapa");
                    txtLatitude = data.getStringExtra("txtLatitude");
                    txtLongitude = data.getStringExtra("txtLongitude");
                } else if (data.getStringExtra("itensOptions") != null) {
                    modalidadeSelecionadas = data.getStringExtra("itensOptions");
                    codigosModalidadesSelecionadas = data.getStringExtra("codigosItensOptions");
                }

                List<Fragment> allFragments = getSupportFragmentManager().getFragments();
                if (allFragments != null) {
                    for (Fragment fragment : allFragments) {

                        if (fragment.getClass().equals(CadastroEventoFragment.class)) {
                            CadastroEventoFragment f1 = (CadastroEventoFragment)fragment;
                            f1.setEndereco(txtEnderecoMapa, txtLatitude, txtLongitude);
                        } else if (fragment.getClass().equals(PlaceholderFragment.class)) {
                            PlaceholderFragment f2 = (PlaceholderFragment)fragment;
                            f2.setModalidades(modalidadeSelecionadas, codigosModalidadesSelecionadas);
                        }
                    }
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        fragmentManager = getSupportFragmentManager();
        if (position == 0) {
            Drawable d= getResources().getDrawable(R.drawable.background_busca_eventos);
            getSupportActionBar().setBackgroundDrawable(d);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                    .commit();
        } else if (position == 1) {
            Drawable d= getResources().getDrawable(R.drawable.background_cad_evento);
            getSupportActionBar().setBackgroundDrawable(d);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, CadastroEventoFragment.newInstance(position))
                    .commit();
        } else if (position == 2) {
            /*fragmentManager.beginTransaction()
                    .replace(R.id.container, ThridFragment.newInstance())
                    .commit();*/
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        private ArrayList<ItemCheckListView> e;
        private ArrayList<ItemCheckListView> e2;
        private ArrayList<ItemCheckListView> e3;
        private ArrayList<ItemCheckListView> aModalidades;

        private TextView txtCodigosModalidadeSelecionadas;
        private TextView txtModalidadeSelecionadas;
        public void setModalidades(String Modalidades, String ModalidadeSelecionadas) {
            txtCodigosModalidadeSelecionadas.setText(ModalidadeSelecionadas);
            txtModalidadeSelecionadas.setText(Modalidades);
        }

        public void requestGetModalidades() {
            String finalUrl = "https://php-caiogaspar.c9users.io/get_modalidades.php";
            Log.d("br.com.jogger.jogger", finalUrl);
            RequestQueue queue = Volley.newRequestQueue(PlaceholderFragment.this.getActivity());
            JsonObjectRequest request = new JsonObjectRequest(finalUrl, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("br.com.jogger.jogger", response.toString());
                            try {
                                JSONArray modalidadesJson =response.getJSONArray("modalidades");
                                ArrayList<String> arrayModalidades = new ArrayList<String>();
                                aModalidades = new ArrayList<ItemCheckListView>();
                                for (int i = 0; i < modalidadesJson.length(); i++) {
                                /*Modalidade objetoModalidade = new Modalidade();
                                objetoModalidade.setId(response.getString("Id"));
                                objetoModalidade.setName(response.getString("Nome"));*/
                                    JSONObject node = modalidadesJson.getJSONObject(i);
                                    arrayModalidades.add(node.getString("nm_modalidade"));

                                    aModalidades.add(new ItemCheckListView(node.getString("id_modalidade"), node.getString("nm_modalidade"), false));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Utils.alert(PlaceholderFragment.this.getActivity(), "Não foi possível pegar as modalidades", "");
                                //showProgress(false);
                            }
                        }
                    },

                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utils.alert(PlaceholderFragment.this.getActivity(), "Não foi possível pegar as modalidades", "");
                            //showProgress(false);
                        }
                    }
            );
            queue.add(request);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);

            final TextView lblDistancia = (TextView)rootView.findViewById(R.id.lblDistancia);
            final SeekBar seekBar = (SeekBar)rootView.findViewById(R.id.seekBar);

            seekBar.setProgress(20);
            lblDistancia.setText("Distância (" + Integer.toString(seekBar.getProgress()) + "km)");

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    lblDistancia.setText("Distância (" + Integer.toString(i) + "km)");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            txtCodigosModalidadeSelecionadas = (TextView)rootView.findViewById(R.id.txtCodigosModalidades);
            txtModalidadeSelecionadas = (TextView)rootView.findViewById(R.id.lblModalidadesEscolhidas);

            e3 = new ArrayList<ItemCheckListView>();
            e3.add(new ItemCheckListView("1", "Futebol de Salão", false));
            e3.add(new ItemCheckListView("2", "Voleibol", false));
            e3.add(new ItemCheckListView("3", "Handebol", false));
            e3.add(new ItemCheckListView("4'", "Rúgbi", false));
            e3.add(new ItemCheckListView("5", "Basquetebol", false));
            final LinearLayout lineModalidade = (LinearLayout)rootView.findViewById(R.id.lineModalidades);
            lineModalidade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HomeActivity)getActivity()).optionsActivity(aModalidades);
                }
            });

            final LinearLayout lineDiaSemana = (LinearLayout)rootView.findViewById(R.id.lineDiaSemana);
            e = new ArrayList<ItemCheckListView>();
            e.add(new ItemCheckListView(null, "Domingo", false));
            e.add(new ItemCheckListView(null, "Segunda-Feira", false));
            e.add(new ItemCheckListView(null, "Terça-Feira", false));
            e.add(new ItemCheckListView(null, "Quarta-Feira", false));
            e.add(new ItemCheckListView(null, "Quinta-Feira", false));
            e.add(new ItemCheckListView(null, "Sexta-Feira", false));
            e.add(new ItemCheckListView(null, "Sábado", false));
            lineDiaSemana.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HomeActivity)getActivity()).optionsActivity(e);
                }
            });

            e2 = new ArrayList<ItemCheckListView>();
            e2.add(new ItemCheckListView(null, "00h00", false));
            e2.add(new ItemCheckListView(null, "00h15", false));
            e2.add(new ItemCheckListView(null, "00h30", false));
            e2.add(new ItemCheckListView(null, "00h45", false));
            e2.add(new ItemCheckListView(null, "01h00", false));
            e2.add(new ItemCheckListView(null, "01h15", false));
            e2.add(new ItemCheckListView(null, "01h30", false));
            e2.add(new ItemCheckListView(null, "01h45", false));
            e2.add(new ItemCheckListView(null, "02h00", false));
            e2.add(new ItemCheckListView(null, "02h15", false));
            e2.add(new ItemCheckListView(null, "02h30", false));
            e2.add(new ItemCheckListView(null, "02h45", false));
            e2.add(new ItemCheckListView(null, "03h00", false));
            e2.add(new ItemCheckListView(null, "03h15", false));
            e2.add(new ItemCheckListView(null, "03h30", false));
            e2.add(new ItemCheckListView(null, "03h45", false));
            e2.add(new ItemCheckListView(null, "04h00", false));
            e2.add(new ItemCheckListView(null, "04h15", false));
            e2.add(new ItemCheckListView(null, "04h30", false));
            e2.add(new ItemCheckListView(null, "04h45", false));
            e2.add(new ItemCheckListView(null, "05h00", false));
            e2.add(new ItemCheckListView(null, "05h15", false));
            e2.add(new ItemCheckListView(null, "05h30", false));
            e2.add(new ItemCheckListView(null, "05h45", false));
            e2.add(new ItemCheckListView(null, "06h00", false));
            e2.add(new ItemCheckListView(null, "06h15", false));
            e2.add(new ItemCheckListView(null, "06h30", false));
            e2.add(new ItemCheckListView(null, "06h45", false));
            e2.add(new ItemCheckListView(null, "07h00", false));
            e2.add(new ItemCheckListView(null, "07h15", false));
            e2.add(new ItemCheckListView(null, "07h30", false));
            e2.add(new ItemCheckListView(null, "07h45", false));
            e2.add(new ItemCheckListView(null, "08h00", false));
            e2.add(new ItemCheckListView(null, "08h15", false));
            e2.add(new ItemCheckListView(null, "08h30", false));
            e2.add(new ItemCheckListView(null, "08h45", false));
            e2.add(new ItemCheckListView(null, "09h00", false));
            e2.add(new ItemCheckListView(null, "09h15", false));
            e2.add(new ItemCheckListView(null, "09h45", false));
            e2.add(new ItemCheckListView(null, "10h00", false));
            e2.add(new ItemCheckListView(null, "10h15", false));
            e2.add(new ItemCheckListView(null, "10h30", false));
            e2.add(new ItemCheckListView(null, "10h45", false));
            e2.add(new ItemCheckListView(null, "11h00", false));
            e2.add(new ItemCheckListView(null, "11h15", false));
            e2.add(new ItemCheckListView(null, "11h30", false));
            e2.add(new ItemCheckListView(null, "11h45", false));
            e2.add(new ItemCheckListView(null, "12h00", false));
            e2.add(new ItemCheckListView(null, "12h15", false));
            e2.add(new ItemCheckListView(null, "12h30", false));
            e2.add(new ItemCheckListView(null, "12h45", false));
            e2.add(new ItemCheckListView(null, "13h00", false));
            e2.add(new ItemCheckListView(null, "13h15", false));
            e2.add(new ItemCheckListView(null, "13h30", false));
            e2.add(new ItemCheckListView(null, "13h45", false));
            e2.add(new ItemCheckListView(null, "14h00", false));
            e2.add(new ItemCheckListView(null, "14h15", false));
            e2.add(new ItemCheckListView(null, "14h30", false));
            e2.add(new ItemCheckListView(null, "14h45", false));
            e2.add(new ItemCheckListView(null, "15h00", false));
            e2.add(new ItemCheckListView(null, "15h15", false));
            e2.add(new ItemCheckListView(null, "15h30", false));
            e2.add(new ItemCheckListView(null, "15h45", false));
            e2.add(new ItemCheckListView(null, "16h00", false));
            e2.add(new ItemCheckListView(null, "16h15", false));
            e2.add(new ItemCheckListView(null, "16h30", false));
            e2.add(new ItemCheckListView(null, "16h45", false));
            e2.add(new ItemCheckListView(null, "17h00", false));
            e2.add(new ItemCheckListView(null, "17h15", false));
            e2.add(new ItemCheckListView(null, "17h30", false));
            e2.add(new ItemCheckListView(null, "17h45", false));
            e2.add(new ItemCheckListView(null, "18h00", false));
            e2.add(new ItemCheckListView(null, "18h15", false));
            e2.add(new ItemCheckListView(null, "18h30", false));
            e2.add(new ItemCheckListView(null, "18h45", false));
            e2.add(new ItemCheckListView(null, "19h00", false));
            e2.add(new ItemCheckListView(null, "19h15", false));
            e2.add(new ItemCheckListView(null, "19h30", false));
            e2.add(new ItemCheckListView(null, "19h45", false));
            e2.add(new ItemCheckListView(null, "20h00", false));
            e2.add(new ItemCheckListView(null, "20h15", false));
            e2.add(new ItemCheckListView(null, "20h30", false));
            e2.add(new ItemCheckListView(null, "20h45", false));
            e2.add(new ItemCheckListView(null, "21h00", false));
            e2.add(new ItemCheckListView(null, "21h15", false));
            e2.add(new ItemCheckListView(null, "21h30", false));
            e2.add(new ItemCheckListView(null, "21h45", false));
            e2.add(new ItemCheckListView(null, "22h00", false));
            e2.add(new ItemCheckListView(null, "22h15", false));
            e2.add(new ItemCheckListView(null, "22h30", false));
            e2.add(new ItemCheckListView(null, "22h45", false));
            e2.add(new ItemCheckListView(null, "23h00", false));
            e2.add(new ItemCheckListView(null, "23h15", false));
            e2.add(new ItemCheckListView(null, "23h30", false));
            e2.add(new ItemCheckListView(null, "23h45", false));


            final RelativeLayout lineHorario = (RelativeLayout)rootView.findViewById(R.id.lineHorario);
            lineHorario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HomeActivity)getActivity()).optionsActivity(e2);
                }
            });

            final TextView txtDtInicio = (TextView)rootView.findViewById(R.id.lblDtInicioEscolhido);
            final RelativeLayout lineDtInicio = (RelativeLayout)rootView.findViewById(R.id.lineDtInicio);

            final Calendar newCalendar = Calendar.getInstance();
            final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
            lineDtInicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog fromDatePickerDialog = new DatePickerDialog(PlaceholderFragment.this.getActivity(), new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);

                            txtDtInicio.setText(dateFormatter.format(newDate.getTime()));
                            TimePickerDialog mTimePickerInicio = new TimePickerDialog(PlaceholderFragment.this.getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                                    String hora = String.valueOf(hourOfDay);
                                    if (hourOfDay < 10) {
                                        hora = "0" + hora;
                                    }
                                    String minuto = String.valueOf(minute);
                                    if (minute < 10) {
                                        minuto = "0" + minuto;
                                    }
                                    txtDtInicio.setText(txtDtInicio.getText().toString() + " " + hora + ":" + minuto +":00");
                                }
                            }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
                            mTimePickerInicio.show();
                        }

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                    fromDatePickerDialog.show();
                }
            });

            final TextView txtDtFim = (TextView)rootView.findViewById(R.id.lblDtFimEscolhido);
            final RelativeLayout lineDtFim = (RelativeLayout)rootView.findViewById(R.id.lineDtFim);

            lineDtFim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog fromDatePickerDialog = new DatePickerDialog(PlaceholderFragment.this.getActivity(), new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);

                            txtDtFim.setText(dateFormatter.format(newDate.getTime()));
                            TimePickerDialog mTimePickerInicio = new TimePickerDialog(PlaceholderFragment.this.getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                                    String hora = String.valueOf(hourOfDay);
                                    if (hourOfDay < 10) {
                                        hora = "0" + hora;
                                    }
                                    String minuto = String.valueOf(minute);
                                    if (minute < 10) {
                                        minuto = "0" + minuto;
                                    }
                                    txtDtFim.setText(txtDtFim.getText().toString() + " " + hora + ":" + minuto +":00");
                                }
                            }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
                            mTimePickerInicio.show();
                        }

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                    fromDatePickerDialog.show();
                }
            });

            final RadioGroup rg = (RadioGroup)rootView.findViewById(R.id.radioGroupFiltroBusca);

            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int op = rg.getCheckedRadioButtonId();

                    if(op==R.id.rbFrequenciaSemanal) {
                        lineHorario.setVisibility(View.VISIBLE);
                        lineDiaSemana.setVisibility(View.VISIBLE);
                        lineDtInicio.setVisibility(View.GONE);
                        lineDtFim.setVisibility(View.GONE);
                    } else if(op== R.id.rbEventoProximo) {
                        lineDtInicio.setVisibility(View.GONE);
                        lineDtFim.setVisibility(View.GONE);
                        lineDiaSemana.setVisibility(View.GONE);
                        lineHorario.setVisibility(View.GONE);
                    } else if (op == R.id.rbPeriodoEspecifico) {
                        lineDtInicio.setVisibility(View.VISIBLE);
                        lineDtFim.setVisibility(View.VISIBLE);
                        lineDiaSemana.setVisibility(View.GONE);
                        lineHorario.setVisibility(View.GONE);
                    }
                }
            });

            requestGetModalidades();

            final Button btnBuscarEventos = (Button)rootView.findViewById(R.id.btnBuscarEventos);
               btnBuscarEventos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tipoEvento = "";

                    int bt = rg.getCheckedRadioButtonId();
                    if (bt ==  R.id.rbEventoProximo) {
                        tipoEvento = "3";
                    } else if (bt == R.id.rbPeriodoEspecifico) {
                        tipoEvento = "1";
                    } else if (bt == R.id.rbFrequenciaSemanal) {
                        tipoEvento = "2";
                    }
                    ((HomeActivity) getActivity()).mapasBuscaEventosActivity(txtDtInicio.getText().toString(), txtDtFim.getText().toString(), txtCodigosModalidadeSelecionadas.getText().toString(), String.valueOf(seekBar.getProgress()), tipoEvento);
                }
            });
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((HomeActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CadastroEventoFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        private EditText mNomeEventoView;
        private EditText mDescEventoView;
        private TextView mEndLogradouroView;
        private EditText mDtInicioView;
        private EditText mDtFimView;
        private EditText txtLatitude;
        private EditText txtLongitude;
        private Spinner mCbModalidade;
        private View mCadEventoFormView;

        private JSONArray modalidadesJson;

        private static String url = "https://php-caiogaspar.c9users.io/cadastro_evento.php";
        private static String msgError = "Não foi possível efetuar o cadastro.";

        public static CadastroEventoFragment newInstance(int sectionNumber) {
            CadastroEventoFragment fragment = new CadastroEventoFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public CadastroEventoFragment() {
        }

        public void setEndereco(String endereco, String latitude, String longitude) {
            txtLongitude.setText(longitude);
            txtLatitude.setText(latitude);
            mEndLogradouroView.setText(endereco);
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
            String ds_latitude = "";
            String ds_longitude = "";
            int modalidade = 0;

            try {
                nome   = URLEncoder.encode(mNomeEventoView.getText().toString(), "UTF-8");
                desc   = URLEncoder.encode(mDescEventoView.getText().toString(),"UTF-8");
                end_logradouro  = URLEncoder.encode(mEndLogradouroView.getText().toString(),"UTF-8");
                dt_inicio  = URLEncoder.encode(mDtInicioView.getText().toString(),"UTF-8");
                dt_fim   = URLEncoder.encode(mDtFimView.getText().toString(), "UTF-8");
                ds_latitude = URLEncoder.encode(txtLatitude.getText().toString(), "UTF-8");
                ds_longitude = URLEncoder.encode(txtLongitude.getText().toString(), "UTF-8");

                JSONObject node = modalidadesJson.getJSONObject(mCbModalidade.getSelectedItemPosition());
                modalidade   = Integer.parseInt(URLEncoder.encode(node.getString("id_modalidade"), "UTF-8"));
            } catch (Exception e) {
                Utils.alert(CadastroEventoFragment.this.getActivity(), this.msgError, "");
                //showProgress(false);
                return;
            }

            String finalUrl = url +"?nm_evento="+nome +"&ds_evento="+desc+"&ds_logradouro="+end_logradouro+"&dt_inicio="+dt_inicio+"&dt_fim="+dt_fim
                    + "&ds_latitude=" + ds_latitude + "&ds_longitude=" + ds_longitude + "&cd_modalidade=" + String.valueOf(modalidade);
            Log.d("br.com.jogger.jogger", finalUrl);
            RequestQueue queue = Volley.newRequestQueue(CadastroEventoFragment.this.getActivity());
            JsonObjectRequest request = new JsonObjectRequest(finalUrl, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("br.com.jogger.jogger", response.toString());
                            try {
                                if (response.getString("ret").contentEquals("ok")) {
                                    // TODO: calling home activity
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CadastroEventoFragment.this.getActivity());
                                    builder.setMessage(response.getString("msg"))
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            ((HomeActivity) getActivity()).homeActivity();
                                        }
                                    });
                                    builder.create().show();
                                    Utils.alert(CadastroEventoFragment.this.getActivity(), response.getString("msg"), "Sucesso");

                                } else {
                                    Utils.alert(CadastroEventoFragment.this.getActivity(), msgError, "");
                                    //showProgress(false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Utils.alert(CadastroEventoFragment.this.getActivity(), msgError, "");
                                //showProgress(false);
                            }
                        }
                    },

                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utils.alert(CadastroEventoFragment.this.getActivity(), msgError, "");
                            //showProgress(false);
                        }
                    }
            );
            queue.add(request);
        }

        public void requestGetModalidades() {
            String finalUrl = "https://php-caiogaspar.c9users.io/get_modalidades.php";
            Log.d("br.com.jogger.jogger", finalUrl);
            RequestQueue queue = Volley.newRequestQueue(CadastroEventoFragment.this.getActivity());
            JsonObjectRequest request = new JsonObjectRequest(finalUrl, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("br.com.jogger.jogger", response.toString());
                            try {

                                modalidadesJson = response.getJSONArray("modalidades");
                                ArrayList<String> arrayModalidades = new ArrayList<String>();
                                for (int i = 0; i < modalidadesJson.length(); i++) {
                                    JSONObject node = modalidadesJson.getJSONObject(i);
                                    arrayModalidades.add(node.getString("nm_modalidade"));
                                }
                                // Creating adapter for spinner
                                ArrayAdapter<String> dataAdapter;
                                dataAdapter = new ArrayAdapter<String>(CadastroEventoFragment.this.getActivity(), android.R.layout.simple_spinner_item, arrayModalidades);

                                Log.d("br.com.jogger.jogger", arrayModalidades.toString());
                                // Drop down layout style - list view with radio button
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
                                mCbModalidade.setAdapter(dataAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Utils.alert(CadastroEventoFragment.this.getActivity(), msgError, "");
                                //showProgress(false);
                            }
                        }
                    },

                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utils.alert(CadastroEventoFragment.this.getActivity(), msgError, "");
                            //showProgress(false);
                        }
                    }
            );
            queue.add(request);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_cad_evento, container, false);

            mNomeEventoView = (EditText) rootView.findViewById(R.id.nome_evento);
            mDescEventoView = (EditText) rootView.findViewById(R.id.desc_evento);
            mEndLogradouroView = (TextView) rootView.findViewById(R.id.txtEndereco);
            txtLatitude = (EditText)rootView.findViewById(R.id.txtLatitude);
            txtLongitude = (EditText)rootView.findViewById(R.id.txtLongitude);
            mDtInicioView = (EditText) rootView.findViewById(R.id.data_ini_evento);
            mDtFimView = (EditText) rootView.findViewById(R.id.data_fim_evento);

            Button mCadastrarButton = (Button) rootView.findViewById(R.id.cadastrar_button);
            mCadastrarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptCriarEvento();
                }
            });

            mCadEventoFormView = rootView.findViewById(R.id.form_cad_evento);

            // Popula Spinner
            mCbModalidade = (Spinner) rootView.findViewById(R.id.cb_modalidade);
            requestGetModalidades();

            mEndLogradouroView = (TextView) rootView.findViewById(R.id.txtEndereco);
            mEndLogradouroView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HomeActivity)getActivity()).mapasActivity();
                }
            });

            final Calendar newCalendar = Calendar.getInstance();
            final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
            mDtInicioView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatePickerDialog fromDatePickerDialog = new DatePickerDialog(CadastroEventoFragment.this.getActivity(), new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);

                            mDtInicioView.setText(dateFormatter.format(newDate.getTime()));
                            TimePickerDialog mTimePickerInicio = new TimePickerDialog(CadastroEventoFragment.this.getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                                    String hora = String.valueOf(hourOfDay);
                                    if (hourOfDay < 10) {
                                        hora = "0" + hora;
                                    }
                                    String minuto = String.valueOf(minute);
                                    if (minute < 10) {
                                        minuto = "0" + minuto;
                                    }
                                    mDtInicioView.setText(mDtInicioView.getText().toString() + " " + hora + ":" + minuto +":00");
                                }
                            }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
                            mTimePickerInicio.show();
                        }

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                    fromDatePickerDialog.show();
                }
            });

            mDtFimView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog fromDatePickerDialog = new DatePickerDialog(CadastroEventoFragment.this.getActivity(), new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);

                            mDtFimView.setText(dateFormatter.format(newDate.getTime()));
                            TimePickerDialog mTimePickerFim = new TimePickerDialog(CadastroEventoFragment.this.getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                                    String hora = String.valueOf(hourOfDay);
                                    if (hourOfDay < 10) {
                                        hora = "0" + hora;
                                    }
                                    String minuto = String.valueOf(minute);
                                    if (minute < 10) {
                                        minuto = "0" + minuto;
                                    }
                                    mDtFimView.setText(mDtFimView.getText().toString() + " " + hora + ":" + minuto +":00");
                                }
                            }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
                            mTimePickerFim.show();
                        }

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                    fromDatePickerDialog.show();
                }
            });

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((HomeActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
