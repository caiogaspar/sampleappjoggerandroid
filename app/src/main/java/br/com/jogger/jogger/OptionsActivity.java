package br.com.jogger.jogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class OptionsActivity extends MainActivity {

    public ArrayList<ItemCheckListView> itensOptions ;
    private ListView listOptions;
    private AdapterListView adapterListViewOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itensOptions = (ArrayList<ItemCheckListView>) getIntent().getSerializableExtra("mylist");
        setContentView(R.layout.activity_options);

        adapterListViewOptions = new AdapterListView(this, itensOptions);

        listOptions = (ListView)findViewById(R.id.listOptions);
        //Define o Adapter
        listOptions.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listOptions.setAdapter(adapterListViewOptions);

        listOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("br.com.jogger.jogger", "Ao menos entrou!");
                ItemCheckListView item = (ItemCheckListView)listOptions.getItemAtPosition(i);

                if (item.getCheckbox()) {
                    item.setCheckbox(false);
                } else {
                    item.setCheckbox(true);
                }
                itensOptions.set(i, item);

                adapterListViewOptions = new AdapterListView(OptionsActivity.this, itensOptions);
                adapterListViewOptions.notifyDataSetChanged();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        String itensOptionsString = "";
        String codigoItensOptions = "";

        ArrayList<ItemCheckListView> q1 = adapterListViewOptions.getItens();
        for (ItemCheckListView it : q1) {
            Log.d("br.com.jogger.jogger", it.getCheckbox().toString());
             if (itensOptionsString != "" && it.getCheckbox()) {
                itensOptionsString = itensOptionsString + "," + it.getTexto1();
                codigoItensOptions = codigoItensOptions + "," + it.getCodigo();
             } else if (it.getCheckbox()) {
                 itensOptionsString = it.getTexto1();
                 codigoItensOptions = it.getCodigo();
             }
        }
        //Log.d("br.com.jogger.jogger", itensOptionsString);
        //Log.d("br.com.jogger.jogger", codigoItensOptions);
        returnIntent.putExtra("itensOptions", itensOptionsString);
        returnIntent.putExtra("codigosItensOptions", codigoItensOptions);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

        super.onBackPressed();
    }

}
