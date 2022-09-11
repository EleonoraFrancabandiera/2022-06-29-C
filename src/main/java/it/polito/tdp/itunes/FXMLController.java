/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.AlbumBilancio;
import it.polito.tdp.itunes.model.Model;
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

    @FXML // fx:id="btnAdiacenze"
    private Button btnAdiacenze; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA2"
    private ComboBox<Album> cmbA2; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doCalcolaAdiacenze(ActionEvent event) {
    	this.txtResult.clear();
    	
    	//devo controllare che il grafo sia stato creato
    	if(!this.model.grafoCreato()) {
    		this.txtResult.appendText("Crea prima il grafo!");
    		return;
    	}
    	
    	Album a = this.cmbA1.getValue();
    	if(a==null) {
    		this.txtResult.appendText("Seleziona un album!");
    		return;
    	}
    	
    	for(AlbumBilancio ab : this.model.getAdiacenze(a)) {
    		this.txtResult.appendText(ab.toString() + "\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	this.txtResult.clear();
    	
    	//devo controllare che il grafo sia stato creato
    	if(!this.model.grafoCreato()) {
    		this.txtResult.appendText("Crea prima il grafo!");
    		return;
    	}
    	
    	Album a1 = this.cmbA1.getValue();
    	if(a1==null) {
    		this.txtResult.appendText("Seleziona un album di partenza!");
    		return;
    	}
    	
    	Album a2 = this.cmbA2.getValue();
    	if(a2==null) {
    		this.txtResult.appendText("Seleziona un album di arrivo!");
    		return;
    	}
    	if(a2.equals(a1)) {
    		this.txtResult.appendText("Seleziona un album di arrivo diverso da quello di partenza!");
    		return;
    	}
    	
    	double soglia;
    	try {
    		soglia = Double.parseDouble(this.txtX.getText());
    	}catch(NumberFormatException e) 
    	{
    		this.txtResult.appendText("Inserisci un valore numerico per la soglia");
    		return;
    	}
    	
    	List<Album> percorso=this.model.calcolaPercorso(a1, a2, soglia);
    	if(percorso.size()!=0) {
    		this.txtResult.appendText("Percorso migliore:\n");
    		for(Album a : percorso) {
    			this.txtResult.appendText(a.toString() + "\n");
    		}
    	}else {
    		this.txtResult.appendText("Non esiste nessun percorso da a1 ad a2.\n");
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	
    	double prezzo;
    	try {
    		prezzo = Double.parseDouble(this.txtN.getText());
    	}catch(NumberFormatException e) 
    	{
    		this.txtResult.appendText("Inserisci un valore numerico per il prezzo massimo");
    		return;
    	}
    	
    	this.model.creaGrafo(prezzo);
    	this.txtResult.appendText("Grafo creato!\n");
    	this.txtResult.appendText("# Vertici : " + this.model.nVertici() + "\n");
    	this.txtResult.appendText("# Archi : " + this.model.nArchi() + "\n");
    	
    	this.cmbA1.getItems().clear();
    	this.cmbA1.getItems().addAll(this.model.getVertici());
    	
    	this.cmbA2.getItems().clear();
    	this.cmbA2.getItems().addAll(this.model.getVertici());
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenze != null : "fx:id=\"btnAdiacenze\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA2 != null : "fx:id=\"cmbA2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }

    
    public void setModel(Model model) {
    	this.model = model;
    }
}
