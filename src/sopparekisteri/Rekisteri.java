/**
 * 
 */
package sopparekisteri;

import java.util.*;

/**
 * Rekisteri-luokka, jolla on pääasiassa välittäjämetodeja
 * @author Jouni Ahola
 * @version 4.4.2019
 *
 */
public class Rekisteri {
    private  Ruuat ruuat = new Ruuat();
    private  RaakaAineet raakaAineet = new RaakaAineet();
    private  Yhdistaa idTaulukko = new Yhdistaa();

    
    /** testipääohjelma, jolla ei ole nyt tässä tapauksesa mitään käyttöä
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        // tyhjää täynnä
    
    }
    
    /**
     * palauttaa rekisterissä olevien ruokien lukumäärän.
     * @return ruokien lukumäärä
     * @example
     * <pre name="test">
     * Rekisteri rekisteri = new Rekisteri();
     * Ruoka laatikko = new Ruoka();
     * laatikko.rekisteroi();
     * rekisteri.getRuuat() === 0;
     * rekisteri.lisaa(laatikko);
     * rekisteri.getRuuat() === 1;
     * </pre>
     */
    public int getRuuat() {
        return ruuat.getLkm();
    }
    
    /**
     * lisää uuden ruuan rekisteriin
     * @param ruoka lisättävä ruoka
     * @example
     * <pre name="test">
     * Rekisteri rekisteri = new Rekisteri();
     * Ruoka laatikko = new Ruoka();
     * laatikko.rekisteroi();
     * 
     * </pre>
     */
    public void lisaa(Ruoka ruoka) {
        ruuat.lisaa(ruoka);
    }

    /**
     * palauttaa i:n ruuan
     * @param i monesko ruoka palautetaan
     * @return viite i:n ruokaan
     */
    public Ruoka annaRuoka(int i) {
        return ruuat.anna(i);
    }
    
    /**
     * lisää uuden raaka-aineen rekisteriin
     * @param raa lisättävä raaka-aine
     */
    public void lisaa(RaakaAine raa) {
        raakaAineet.lisaa(raa);
    }
    
    /**
     * lisää uuden id:n rekisteriin
     * @param yhd lisättävä id
     */
    public void lisaa(Yhdista yhd) {
        idTaulukko.lisaa(yhd);
    }
    
    /**
     * palauttaa kaikki ruuan raaka-aineet
     * @param ruoka ruuan kaikki raaka-aineet
     * @return raaka-aineet
     */
    public List<RaakaAine> annaRaakaAineet(Ruoka ruoka) {
       List <Integer> lista = idTaulukko.aineet(ruoka.getTunnusNumero());
       if(raakaAineet == null) return null;
       return raakaAineet.anna(lista);
    }
    /**
     * palauttaa yhden id:n tiedot
     * @param yhd yhidstämi
     * @return idtaulukko, jossa on tietoja id:eistä
     */
    public List<Yhdista> AnnaTiedot(Yhdista yhd){
        return idTaulukko.AnnaTiedot(yhd.getId());
    }
    
   
    
    /**
     * @param hakuehto minkä perusteella haetaan
     * @param k kenttä
     * @return palauttaa hakuehtoa vastaavat ruuat
     */
    public Collection <Ruoka> etsi (String hakuehto, int k){
        return ruuat.etsi(hakuehto, k);
    }
    
    /** luetaan rekisterin tiedot tiedostoista
     * @throws SailoException virhe
     */
    public void lueTiedostosta() throws SailoException {
        ruuat = new Ruuat();
        raakaAineet= new RaakaAineet();
        idTaulukko= new Yhdistaa();
        
        ruuat.lueTiedostoa();
        raakaAineet.lueTiedostoa();
        idTaulukko.lueTiedostoa();
        
    }
    
    /** tallennetaan rekisterin tiedot tiedostoihin
     * @throws SailoException virhe
     */
    public void tallenna() throws SailoException {
        ruuat.tallenna();
        raakaAineet.tallenna();
        idTaulukko.tallenna();
    }

    /**
     * @param ruoka mitä muokataan tai lisätään
     * @throws SailoException virhe
     */
    public void korvaaTaiLisaa(Ruoka ruoka) throws SailoException { 
        ruuat.korvaaTaiLisaa(ruoka); 
    }

    /**
     * @param aine mitä muokataan tai lisätään
     */
    public void korvaaTaiLisaa(RaakaAine aine) {
        raakaAineet.korvaaTaiLisaa(aine);
        
    }

    /** poistetaan ruoka ruuista sekä yhdistä-luokasta
     * @param ruoka poistettava ruoka
     */
    public void poistaRuoka(Ruoka ruoka) {
        if(ruoka == null) return;
        idTaulukko.poista(ruoka.getTunnusNumero());
        ruuat.poista(ruoka.getTunnusNumero());
       
    }

    /** poistetaan haluttu raaka-aine.
     * @param aine poistettava raaka-aine
     */
    public void poistaAine(RaakaAine aine) {
       raakaAineet.poista(aine);
        
    }

}
