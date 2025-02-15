/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import it.polito.tdp.imdb.model.Vicini;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	boxRegista.getItems().clear();
    	Integer anno = boxAnno.getValue();
    	if(anno == null) {
    		txtResult.appendText("Seleziona un anno");
    		return;
    	}
    	String msg = model.creaGrafo(anno);
    	txtResult.appendText(msg);
    	boxRegista.getItems().addAll(model.getVertici());
    	

    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {

    	txtResult.clear();
    	Director d = boxRegista.getValue();
    	if(d == null) {
    		txtResult.appendText("Seleziona un regista");
    		return;
    	}
    	List<Vicini> v = new ArrayList<>();
    	v = model.getVicini(d);
    	for(Vicini t: v) {
    		txtResult.appendText(t.toString());
    	}
    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	txtResult.clear();
    	Director d = boxRegista.getValue();
    	int c;
    	List<Director> best = new ArrayList<Director>();
    	if(d == null) {
    		txtResult.appendText("Seleziona un regista");
    		return;
    	}
    	try {
    		c = Integer.parseInt(txtAttoriCondivisi.getText());
    	}catch(NumberFormatException e) {
    		e.printStackTrace();
    		txtResult.appendText("Devi inserire un numero intero");
    		return;
    	}
    	best = model.calcolaPercorsoMigliore(d, c);
    	for(Director d1: best) {
    		txtResult.appendText(d.toString());
    	}
    	txtResult.appendText("Totale Attori condivisi: "+model.totAttoriCondivisi);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	for(int i=2004; i<2007; i++)
    		boxAnno.getItems().addAll(i);
    }
    
}
