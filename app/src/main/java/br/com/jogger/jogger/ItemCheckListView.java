package br.com.jogger.jogger;

import java.io.Serializable;

public class ItemCheckListView implements Serializable {
	private String codigo;
    private String texto1;
    private Boolean checkbox;

    public ItemCheckListView() {
    }

    public ItemCheckListView(String codigo1, String texto1, Boolean checkbox) {
    	this.codigo = codigo1;
        this.texto1 = texto1;
        this.checkbox = checkbox;
    }
    
    public String getCodigo() {
    	return codigo;
    }
    
    public void setCodigo(String codigo1) {
    	this.codigo = codigo1;
    }

    public String getTexto1() {
        return texto1;
    }
 
    public void setTexto1(String texto) {
        this.texto1 = texto;
    }

    public Boolean getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(Boolean checkbox) {
        this.checkbox = checkbox;
    }
}
