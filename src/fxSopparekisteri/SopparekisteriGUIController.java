package fxSopparekisteri;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import sopparekisteri.RaakaAine;
import sopparekisteri.Rekisteri;
import sopparekisteri.Ruoka;
import sopparekisteri.SailoException;
import sopparekisteri.Yhdista;
import javafx.scene.control.TableView; 
import java.io.PrintStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.*;
/**
 * @author Jouni Ahola
 * @version 7.4.2019
 * Ohjelma toimii pääperiaatteitan hyvin, mutta pieniä ongelmia esiintyy, mitkä saattavat vaikuttaa käyttökokemukseen
 * Pieniä ongelmia, joita ohjelmasta löytyy: nämä voisi korjata jos mahdollista ja on aikaa että jaksamista
 * ainakin nämä löydetty:
 * 
 * raaka-aineiden muokkaus ei päivity heti // korjattu
 * ruuan ohjeen muotoilu textAreaan voisi olla parempi
 * ohjetta kirjoittaessa täytyy olla erittäin varovainen // ei tarvitse enää olla     
 */
public class SopparekisteriGUIController implements Initializable{
    @FXML private ListChooser<Ruoka> chooserRuuat;
    @FXML private ScrollPane panelRuoka;
    @FXML private TextField editNimi; 
    @FXML private TextArea editOhje; 
    @FXML private StringGrid <RaakaAine> tableAineet;
    @FXML private TextField hakuehto;
    @FXML private ComboBoxChooser<String> cbKentat;
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
       alusta();
    }

    /**
     * Käsitellään uuden raaka-aineen lisääminen
     */
    @FXML private void handleUusiAine() {
        uusiRaakaAine();
    }
    

    /**
     * Käsitellään tallennuskäsky
     * @throws SailoException virhe
     */
    @FXML private void handleTallenna() throws SailoException {
        tallenna();
    }
    /**
     * käsitellään hakuehdon käsitteleminen
     */
    @FXML private void handleHakuehto() {
        hae(0);
    }
    
    
    /**
     * Käsitellään lopetuskäsky
     * @throws SailoException 
     */
    @FXML private void handleLopeta() throws SailoException {
        tallenna();
        Platform.exit();
    }

    
    /**
     * Tietojen tallennus
     * @return null jos onnistuu
     * @throws SailoException virhe
     */
    protected String tallenna() throws SailoException {
        rekisteri.tallenna();
        return null;
    }
    /**
     * Käsitellään muokkauskäsky
     * @throws SailoException 
     * @throws CloneNotSupportedException 
     */
    @FXML private void handleMuokkaa() throws CloneNotSupportedException, SailoException {
        muokkaa();
    }
    /**
     * Näytetään tietoa erillisessa ikkunassa
     */
	@FXML private void handleTietoa() {
	    ModalController.showModal(SopparekisteriGUIController.class.getResource("TiedotView.fxml"),"Tieto", null,"");
	}
	/**
     * Käsitellään poistokäsky
     */
	@FXML private void handlePoista() {
        poistaRuoka();
    }
	/**
	 * käsitellään uuden ruuan lisääminen
	 */
	@FXML private void handleUusiRuoka() {
        uusiRuoka();
    }

	/**
	 * käsitelleen raaka-aineen poistaminen
	 */
	@FXML private void handlePoistaAine() {
        poistaRuokaAine();
    }
    
   
        
        
        
        
    
  //===========================================================================================   
   // Tästä eteenpäin ei käyttöliittymään suoraan liittyvää koodia  


    private Rekisteri rekisteri;
    private Ruoka ruokaKohdalla;
   
    private TextArea areaRuoka = new TextArea();
    private static RaakaAine apuaine = new RaakaAine();  
    private TextArea [] edits;
    private TextField [] edits2;
    
    
    /**
     * @param rekisteri itse rekisteri, jota näytetään käyttöliittymässä
     */
    public void setRekisteri(Rekisteri rekisteri) {
               this.rekisteri = rekisteri;
                naytaRuoka();
            }
    
    /**
     * Tekee tarvittavan alustuksen. Asettaa ison tekstikentän, johon voidaan tulostella ruokien tietoja
     * lisäksi alustaa ruokalistan kuuntelijan.
     */
    protected void alusta() {
        panelRuoka.setContent(areaRuoka);
        areaRuoka.setFont(new Font("Courier New", 12));
        areaRuoka.setWrapText(true); // tekee sen ettei oo horisontaalista scrollbaria
        
        panelRuoka.setFitToHeight(true);
        panelRuoka.autosize();
       
        chooserRuuat.clear(); 
       
        cbKentat.clear(); 
        cbKentat.add("nimi",null);
        cbKentat.getSelectionModel().select(0);
        edits = new TextArea[] {areaRuoka};
        edits2= new TextField[] {editNimi};
        chooserRuuat.addSelectionListener(e -> naytaRuoka());
     // alustetaan ainetaulukon otsikot
        int eka = apuaine.ekaKentta();  
        int lkm = apuaine.getKenttia();  
        String[] headings = new String[lkm-eka];  
        for (int i=0, k=eka; k<lkm; i++, k++) headings[i] = apuaine.getKysymys(k);
        tableAineet.initTable(headings);
        tableAineet.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
        tableAineet.setEditable(false);
        tableAineet.setPlaceholder(new Label("Ei vielä raaka-aineita"));
        tableAineet.setColumnWidth(2, 60);
        tableAineet.setOnMouseClicked( e -> { if ( e.getClickCount() > 1 )
            try {
                muokkaaAinetta();
            } catch (CloneNotSupportedException e1) {
                e1.printStackTrace();
            } } );
        
        tableAineet.setColumnSortOrderNumber(1);
        tableAineet.setColumnSortOrderNumber(2); 
        tableAineet.setColumnWidth(1, 60);  
    }

    /**
     * muokataan raaka-ainetta, valinta on hiirellä klikatun aineen kohdalla
     * @throws CloneNotSupportedException mahdollinen virhe
     */
    private void muokkaaAinetta() throws CloneNotSupportedException {
        int r = tableAineet.getRowNr(); 
        if ( r < 0 ) return; // klikattu ehkä otsikkoriviä 
        RaakaAine aine = tableAineet.getObject();
        if ( aine == null ) return;
        // int k = tableAineet.getColumnNr()+aine.ekaKentta(); 
        aine = AineController.kysyAine(null, aine.clone());
        if(aine == null) return;
        rekisteri.korvaaTaiLisaa(aine);
        naytaAineet(ruokaKohdalla);
    }

    /**
     * Näyttää listasta valitun ruuan ohjeen tekstikenttään
     */
    protected void naytaRuoka() {
        ruokaKohdalla = chooserRuuat.getSelectedObject();
        if (ruokaKohdalla == null) return;
        
       MuokkaaController.naytaRuoka(edits,edits2,ruokaKohdalla); 
       naytaAineet(ruokaKohdalla);
       
           }
    /**
     * näytetään tietyn ruuan raaka-aineet kenttään
     * @param ruoka minkä aineet näytetään
     */
    private void naytaAineet(Ruoka ruoka) {
        tableAineet.clear();
        if(ruoka == null) return;
        List <RaakaAine> aineet = rekisteri.annaRaakaAineet(ruoka);
        if(aineet.size() == 0) return;
        for(RaakaAine aine : aineet)
            naytaAine(aine);
        
    }
    /**
     * alustetaan raaka-aineen kentät StringGridiin
     * @param aine raaka-aine, josta kentät lisätään
     */
    private void naytaAine(RaakaAine aine) {
        int kenttia = aine.getKenttia(); 
        String[] rivi = new String[kenttia-aine.ekaKentta()];
        for (int i=0, k=aine.ekaKentta(); k < kenttia; i++, k++) 
            rivi[i] = aine.anna(k);
        tableAineet.add(aine,rivi);
        
    }

    /**
     * Lisätään uusi ruoka rekisteriin käyttäjän täyttämillä tiedoilla
     */
    protected void uusiRuoka() {
        Ruoka uusi = new Ruoka();
        
        uusi = MuokkaaController.kysyRuoka(null, uusi);  
        if ( uusi == null ) return;
        uusi.rekisteroi();
        Yhdista yhd = new Yhdista(uusi.getTunnusNumero());
        yhd.rekisteroi();
        rekisteri.lisaa(yhd);
        rekisteri.lisaa(uusi);
        hae(uusi.getTunnusNumero());
    }
    
    /**
     * hakee ruuat listaan sekä lajittelee ne.
     * @param tunnusnumero ruuan numero, mikä aktivoidaan haun jälkeen
     */
    protected void hae(int tunnusnumero) {
        int rnro = tunnusnumero;
        if(rnro <= 0) {
            Ruoka kohdalla = ruokaKohdalla;
            if ( kohdalla != null)rnro = kohdalla.getTunnusNumero();
        }
        
        int k = cbKentat.getSelectionModel().getSelectedIndex();
        String ehto = hakuehto.getText();
        if(ehto.indexOf('*') < 0) ehto = "*" + ehto + "*"; 
        chooserRuuat.clear();
        int index = 0;
        Collection<Ruoka> ruuat;
        ruuat = rekisteri.etsi(ehto, k);
        int i = 0;
        for (Ruoka ruoka:ruuat) {
            if (ruoka.getTunnusNumero() == rnro) index = i;
            chooserRuuat.add(ruoka.getNimi(),ruoka);
            i++;
        }
        chooserRuuat.setSelectedIndex(index);
    }
    
    
        /**
          * Tulostaa listassa olevat ruuat tekstialueeseen
          * @param text alue johon tulostetaan
          */
         public void tulostaValitut(TextArea text) {
             try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
                 os.println("Tulostetaan kaikki ruuat");
                 for (int i = 0; i < rekisteri.getRuuat(); i++) {
                    Ruoka ruoka = rekisteri.annaRuoka(i);
                     tulosta(os, ruoka);
                     os.println("\n\n");
                 }
             }
         }

    /**
     * tulostaa ruuan tiedot
     * @param os tietovirta
     * @param ruoka tulostaa tietyn ruuan tiedot
     */
    public void tulosta(PrintStream os, final Ruoka ruoka) {
        os.println("----------------------------------------------");
        ruoka.tulosta(os);
        os.println("----------------------------------------------");
        List<RaakaAine> aineet = rekisteri.annaRaakaAineet(ruoka);
        for(RaakaAine raa:aineet)
            raa.tulosta(os);
        
    }
        
/**
 * lisätään uusi raaka-aine käyttäjän täyttämillä tiedoilla.
 */
    public void uusiRaakaAine() {
        if( ruokaKohdalla == null) return;
        RaakaAine uusi = new RaakaAine();
        uusi = AineController.kysyAine(null, uusi);
       
        if ( uusi == null ) return;
        uusi.rekisteroi();
        Yhdista yhd = new Yhdista(ruokaKohdalla.getTunnusNumero(),uusi.getTunnusNumero());
        yhd.rekisteroi();
        rekisteri.lisaa(yhd);
        rekisteri.lisaa(uusi);
       naytaAineet(ruokaKohdalla);
       tableAineet.selectRow(1000);
}
    
    /**
     * luetaan tiedosto, jossa on rekisterin tiedot ja sitten haetaan tiedot listaan
     * @throws SailoException virhe
     */
    protected void lueTiedostoa() throws SailoException {
      rekisteri.lueTiedostosta();
      hae(0);
      
    }
    
    /**
     * ruuan muokkaus, tehdään ruuasta klooni, jota muokataan, lopuksi haetaan muuttuneet tiedot listaan
     * @throws CloneNotSupportedException cirhe
     * @throws SailoException virhe
     */
    private void muokkaa() throws CloneNotSupportedException, SailoException {
        if(ruokaKohdalla == null) return;
        Ruoka ruoka = MuokkaaController.kysyRuoka(null, ruokaKohdalla.clone());
        if(ruoka == null) return;
        rekisteri.korvaaTaiLisaa(ruoka);
        hae(ruoka.getTunnusNumero());
    }
    
    /**
     * poistetaan valittu ruoka. 
     */
    private void poistaRuoka() {
        Ruoka ruoka = ruokaKohdalla;
        if ( ruoka == null) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko ruoka: " + ruoka.getNimi(), "Kyllä", "Ei") )
            return;
        List <RaakaAine> aineet = rekisteri.annaRaakaAineet(ruoka);
        rekisteri.poistaRuoka(ruoka);
        for(RaakaAine raa:aineet)
            rekisteri.poistaAine(raa);
        int index = chooserRuuat.getSelectedIndex();
        hae(0);
        chooserRuuat.setSelectedIndex(index);
    }
    
    /**
     * poistetaan ainetaulukosta valittu raaka-aine
     */
    private void poistaRuokaAine() {
        int rivi = tableAineet.getRowNr();
        if ( rivi < 0 ) return;
        RaakaAine aine = tableAineet.getObject();
        if ( aine == null ) return;
        rekisteri.poistaAine(aine);
        naytaAineet(ruokaKohdalla);
        int aineita = tableAineet.getItems().size(); 
        if ( rivi >= aineita ) rivi = aineita -1;
        tableAineet.getFocusModel().focus(rivi);
        tableAineet.getSelectionModel().select(rivi);
    }
}