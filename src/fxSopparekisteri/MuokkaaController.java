package fxSopparekisteri;
import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sopparekisteri.Ruoka;

/**
 * @author Jouni Ahola
 * @version 7.4.2019
 *
 */
public class MuokkaaController implements ModalControllerInterface<Ruoka>,Initializable  {
    
    @FXML private TextField editNimi;
    @FXML private TextArea editOhje;
    @FXML private Label labelVirhe;
          private Ruoka ruokaKohdalla;
          private TextField edits2[];
          private TextArea edits[];
          
          
    /**
     * Käsitellään tallennuskäsky
     */
    @FXML private void handleTallenna() {
        Dialogs.showMessageDialog("Tallennetetaan!");
        
    }
    
    /**
     * alustetaan ikkunan taulukot
     */
    private void alusta() {
        edits2 = new TextField[] {editNimi};
        
        edits = new TextArea[] {editOhje};
        int i = 0;
        int i2= 0;
        for (TextField edit : edits2) {
            final int k = ++i;
            edit.setOnKeyReleased( e -> kasitteleMuutosRuokaan(k, (TextField)(e.getSource())));
        }
        for (TextArea edit : edits) {
            final int k = ++i2;
            edit.setOnKeyReleased( e -> kasitteleMuutosRuokaan(k, (TextArea)(e.getSource())));
        }
     }

    
    /**
     * asettaa mihin kenttaan oletuksena keskittyy
     */
    @Override
    public void handleShown() {
        editNimi.requestFocus();
        
    }
    
    @FXML private void handleCancel() {
        ruokaKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }
    
    @FXML private void handleOK() {
        if ( ruokaKohdalla != null && ruokaKohdalla.getNimi().trim().equals("") ) {
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
            
            /*
        }
       
         if(edits[0].getText().contains("\n")) {
            Dialogs.showMessageDialog("Rivinvaihto ei ole sallittu ohjeessa nyt! :(");
            return;
            */
        }
        ModalController.closeStage(labelVirhe);
        
    }

    
    /**
     * Käsitellään peruutuskäsky
     */
    @FXML private void handlePeruuta() {
        Dialogs.showMessageDialog("Peruutus ei onnistu vielä hetkeen");
    }

    /**
     * Luodaan ruuan kysymisdialogi ja palautetaan muutettuna tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä näytetään oletuksena
     * @return null jos painetaan cancel, muuten täytetty tietue.
     */
    public static Ruoka kysyRuoka(Stage modalityStage, Ruoka oletus) {
       return ModalController.showModal(
               MuokkaaController.class.getResource("LisaaView.fxml"),
               "Ruuan muokkaus",
               modalityStage,oletus, null);
        
    }
    /**
     * asetetaan oletusruoka
     */
    @Override
    public void setDefault(Ruoka oletus) {
        ruokaKohdalla = oletus;
        naytaRuoka(edits, edits2,ruokaKohdalla);
    }
    
    /** näytetään ruuan tiedot kenttiin
     * @param edits taulukko, jossa on textArea-kenttä
     * @param edits2 taulukko, jossa on edit-kentät
     * @param ruoka jonka tiedot näytetään
     */
    public static void naytaRuoka(TextArea[] edits, TextField[] edits2, Ruoka ruoka) {
       if(ruoka == null) return;
       edits[0].setText(ruoka.getOhje());
       edits2[0].setText(ruoka.getNimi());
       
       
    }
    

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
        
    }

    


    /**
     * käsitellään muutos ruokaan, tässä tapauksessa textAreaan
     * @param k indeksi
     * @param edit muuttunut kenttä
     */
    private void kasitteleMuutosRuokaan(int k, TextArea edit) {
        if (ruokaKohdalla == null) return;
        String s = edit.getText();
       
        String virhe = null;
        switch (k) {
        case 1 : virhe = ruokaKohdalla.setOhje(s); break;
        default:
            
        }
        if (virhe == null) {
            Dialogs.setToolTipText(edit, "");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        }else {
            Dialogs.setToolTipText(edit,virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);   
        }
    }


    /**
     * käsitellään muutos ruokaan, tässä tapauksessa textFieldiin
     * @param k kenttä
     * @param edit muuttunut lenttä
     */
    private void kasitteleMuutosRuokaan(int k, TextField edit) {
        if (ruokaKohdalla == null) return;
        String s = edit.getText();
        String virhe = null;
        switch (k) {
            case 1 : virhe = ruokaKohdalla.setNimi(s); break;
            default: 
            }     
            if (virhe == null) {
            Dialogs.setToolTipText(edit, "");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        }else {
            Dialogs.setToolTipText(edit,virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        } 
        
    }

    /**
     * naytetaan mahdollinen virhe
     * @param virhe virheteksti
     */
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }



    @Override
    public Ruoka getResult() {
        return ruokaKohdalla;
    }
    /**
     * Tyhjentään tekstikentät 
     * @param edits tauluko jossa tyhjennettäviä tekstikenttiä
     */
    public static void tyhjenna(TextField[] edits) {
        for (TextField edit : edits)
            edit.setText("");
    }
    

}
