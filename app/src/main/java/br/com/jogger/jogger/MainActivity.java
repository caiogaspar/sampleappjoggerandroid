package br.com.jogger.jogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;

/**
 * Created by Caio Gaspar on 17/11/2015.
 */
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void homeActivity() {
        Intent trocatela = new Intent(this, HomeActivity.class);
        startActivity(trocatela);
        //Toast.makeText(this, "Eventos", Toast.LENGTH_SHORT).show();
    }

    public void cadastroActivity() {
        Intent trocatela = new Intent(this, CadUsuarioActivity.class);
        startActivity(trocatela);
    }

    public void optionsActivity(ArrayList<ItemCheckListView> e) {
        OptionsActivity ops = new OptionsActivity();
        Intent trocatela = new Intent(this, ops.getClass());
        trocatela.putExtra("mylist", e);
        startActivityForResult(trocatela, 1);
    }

    public void cadastroEvento() {
        Intent trocatela = new Intent(this, CadEventoActivity.class);
        startActivity(trocatela);
    }

    public void mapasActivity() {
        Intent trocatela = new Intent(this, MapaActivity.class);
        startActivityForResult(trocatela,1);
    }

    public void mapasBuscaEventosActivity(String dtInicio, String dtFim, String modalidades, String distancia, String tipoEvento) {
        Intent trocatela = new Intent(this, MapaBuscaEvento.class);
        trocatela.putExtra("dtInicio", dtInicio);
        trocatela.putExtra("dtFim", dtFim);
        trocatela.putExtra("modalidades", modalidades);
        trocatela.putExtra("distancia", distancia);
        trocatela.putExtra("tipoEvento", tipoEvento);
        startActivity(trocatela);
    }
}
