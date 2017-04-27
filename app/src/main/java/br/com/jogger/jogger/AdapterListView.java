package br.com.jogger.jogger;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterListView extends BaseAdapter
{
    private LayoutInflater mInflater;
    private ArrayList<ItemCheckListView> itens;
 
    public AdapterListView(Context context, ArrayList<ItemCheckListView> itens)
    {
        //Itens que preencheram o listview
        this.itens = itens;
        //responsavel por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
    }

    public ArrayList<ItemCheckListView> getItens() {
        return this.itens;
    }
 
    /**
     * Retorna a quantidade de itens
     *
     * @return
     */
    public int getCount()
    {
        return itens.size();
    }
 
    /**
     * Retorna o item de acordo com a posicao dele na tela.
     *
     * @param position
     * @return
     */
    public ItemCheckListView getItem(int position)
    {
        return itens.get(position);
    }
 
    /**
     * Sem implementação
     *
     * @param position
     * @return
     */
    public long getItemId(int position)
    {
        return position;
    }
 
    public View getView(final int position, View view, ViewGroup parent)
    {
        final ItemCheckListView item = itens.get(position);
        //infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.checkrow, parent, false);
        ((TextView) view.findViewById(R.id.texto1)).setText(item.getTexto1());

       final CheckBox check1 = (CheckBox) view.findViewById(R.id.checkbox1);
       check1.setChecked(item.getCheckbox());

        check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("br.com.jogger.jogger", "Ao menos entrou 2!");
                item.setCheckbox(check1.isChecked());
                itens.set(position, item);

                Log.d("br.com.jogger.jogger", itens.get(position).getCheckbox().toString());
            }
        });
        
        return view;
    }
}

