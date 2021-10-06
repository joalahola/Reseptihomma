/**
 * 
 */
package fxSopparekisteri;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sopparekisteri.RaakaAine;

/**
 * @author Jouni Ahola
 * @version 4.4.2019
 *
 */
public class AineController implements ModalControllerInterface<RaakaAine>,Initializable {
    private RaakaAine aineKohdalla;
    @FXML private TextField editNimi;
    @FXML private TextField editMaara;
    @FXML private TextField editSuure;
    private TextField edits[];
    @FXML private Label labelVirhe;
    
    /**
     * alustetaan tekstikentat,seka asetetaan kuuntelija
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        edits = new TextField[] {editNimi,editMaara,editSuure};
        int i = 0;
        for (TextField edit : edits) {
            final int k = ++i;
            edit.setOnKeyReleased( e -> kasitteleMuutosAineeseen(k, (TextField)(e.getSource())));
        }
    }

    @Override
    public RaakaAine getResult() {
        return aineKohdalla;
        //
    }

    @Override
    public void handleShown() {
        // tahan ei mitaa
        
    }

    @Override
    public void setDefault(RaakaAine oletus) {
        aineKohdalla = oletus;
        naytaAine(edits,aineKohdalla);
        
    }
    /**
     * käsitellään peruutuskäsky
     */
    @FXML private void handleCancel() {
        aineKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }
    
    /**
     * käsitellään mitä tapahtuu kun painaa ok, virhe jos nimessä ei ole mitään
     */
    @FXML private void handleOK() {
        if ( aineKohdalla != null && aineKohdalla.getNimi().trim().equals("") ) {
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
        }
        ModalController.closeStage(labelVirhe);
        
    }
    
    /** asettaa kenttiin aineen tiedot.
     * @param edits taulukko edit-kenttiä
     * @param aine näytettävä raaka-aine
     */
    public static void naytaAine(TextField[] edits, RaakaAine aine) {
        if(aine == null) return;
        edits[0].setText(aine.getNimi());
        edits[1].setText(Double.toString(aine.getMaara()));
        edits[2].setText(aine.getSuure());
        
        
     }
    
    /** Luodaan ruuan kysymisdialogi ja palautetaan muutettuna tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä näytetään oletuksena
     * @return null jos painetaan cancel, muuten täytetty tietue.
     */
    public static RaakaAine kysyAine(Stage modalityStage, RaakaAine oletus) {
        return ModalController.showModal(
                MuokkaaController.class.getResource("RaakaAineView.fxml"),
                "Raaka-aineen muokkaus",
                modalityStage,oletus, null);
         
     }
    
    /**
     * käsitellään muutos raaka-aineeseen
     * @param k indeksi
     * @param edit kenttä
     */
    private void kasitteleMuutosAineeseen(int k, TextField edit) {
        if (aineKohdalla == null) return;
        String s = edit.getText();
        String virhe = null;
        switch (k) {
            case 1 : virhe = aineKohdalla.setNimi(s); break;
            case 2 : virhe = aineKohdalla.setMaara(s); break;
            case 3 : virhe = aineKohdalla.setSuure(s); break;
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
     * näytettävä virhe, jos on
     * @param virhe mahdollinen virheteksti
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

    

}
