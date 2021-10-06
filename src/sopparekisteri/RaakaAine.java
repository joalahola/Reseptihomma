/**
 * 
 */
package sopparekisteri;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Jouni Ahola
 * @version 4.4.2019
 * Raaka-aine, joka osaa esimerkiksi itse pitää huolta tunnusnumerostaan
 */
public class RaakaAine implements Cloneable {
    private int tunnusNumero;
    
    private String nimi = "";
    private double maara;
    private String suure ="";
    private static int seuraavaNumero =1;
    
    
    /**
     * raaka-aineen alustus
     */
    public RaakaAine() {
        // ei käyttöä tässäkään vaiheessa
    }
    
    /**
     * @return palautetaan kenttien lukumaara
     */
    public int getKenttia() {
        return 4;
    }
    
    /** palautetaan ekakentta
     * @return ensimmainen kentta
     */
    public int ekaKentta() {
        return 1;
    }
    
    /**
     * @param k minkä kentän kysymys halutaan
     * @return valitun kentän kysymysteksti
     */
    public String getKysymys(int k) { 
        switch (k) {
        
           case 0:
            return "tunnusNumero";
           case 1: 
            return "nimi";
           case 2:
               return "maara";
           case 3:
               return "suure";
           default:
               return "???";
            
        }
    }
    
    /**
     * @param k minkä kentän sisältö halutaan
     * @return valitun kentän sisältö
     */
    public String anna(int k) { 
        switch (k) { 
        case 0:
            return "" + tunnusNumero;
        case 1:
            return nimi;
        case 2:
            return "" + maara;
        case 3:
            return ""+ suure;
        default:
                return "???";
        }
    }
    
    /**
     * @param k minkä kentän sisältö asetetaan
     * @param s asetettava sisältö merkkijonona
     * @return palauttaa nullin jos onnistuu, muuten virheen tekstina
     */
    public String aseta(int k, String s) {
        String st = s.trim();
        StringBuilder sb = new StringBuilder(st); 
        switch (k) {
        case 0:
            setTunnusNumero(Mjonot.erota(sb,'$',getTunnusNumero()));
            return null;
        case 1:
            nimi = st;
            return null;
        case 2:
            maara = Mjonot.erotaEx(sb,'§',maara);
            return null;
        case 3:
            suure = st;
            return null;
        default:
           return "Väärä kentan indeksi";
            
        }
    }
    
    @Override
    public RaakaAine clone() throws CloneNotSupportedException {
        return (RaakaAine) super.clone();
    }

    /**
     * Tulostetaan raaka-aineen tiedot
     * @param os tietovirta johon tulostetaan
      */
      public void tulosta(OutputStream os) {
             tulosta(new PrintStream(os));
        }
      /**
       * Tehdään esimerkkiraaka-aine esimerkkitiedoilla.
       * @example
       * <pre name="test">
       * RaakaAine aine = new RaakaAine();
       * aine.vastaaRaakaAine();
       * aine.getNimi() === "jauheliha";
       * </pre>
       */
      public void vastaaRaakaAine() {
         
          nimi = "jauheliha";
          maara = 400;
          suure = "g";
          
      }
       /**
        * antaa raaka-aineelle seuraavan rekisterinumeron.
        * @return raaka-aineen uusi tunnusnumero* 
        * @example
        * <pre name="test">
        * RaakaAine m1 = new RaakaAine();
        * m1.getTunnusNumero() === 0;
        * m1.rekisteroi();
        * RaakaAine m2 = new RaakaAine();
        * m2.rekisteroi();
        * int n1 = m1.getTunnusNumero();
        * int n2 =  m2.getTunnusNumero();
        * n1 === n2-1;
        * </pre>
       */
      public int rekisteroi() {
      tunnusNumero = seuraavaNumero;
      seuraavaNumero++;
      return tunnusNumero;
       
   }
      /**
       * palauttaa tunnusnumeron
       * @return tunnusnumero
       * RaakaAine aine = new RaakaAine();
       * aine.getTunnusNumero() === 0;
       * aine.rekisteroi();
       * RaakaAine aine2 = new RaakaAine();
       * aine2.getTunnusNumero() === 1;
       */
      public int getTunnusNumero() {
          return tunnusNumero;
      }
      
      
      
      /**
    * @return palauttaa raaka-aineen nimen
    * @example
    * <pre name="test">
    *  RaakaAine aine = new RaakaAine();
    *  aine.rekisteroi();
    *  aine.vastaaRaakaAine();
    *  aine.getNimi() === "jauheliha";
    * </pre>
    */
      public String getNimi() {
          return nimi;
      }
      
      /**
       * tulostaa raaka-aineen tiedot
     * @param out tietovirta mihin tulostetaan
     */
    public void tulosta(PrintStream out) {
          out.println(nimi +" " +maara +" " + suure);
      }
    
    /**
     * asettaa tunnusnumeron ja huolta pitää siitä että seuraava numero on isompi kuin tunnusnumero
     * @param numero asetettava tunnusnumero
     */
    private void setTunnusNumero(int numero) {
        tunnusNumero = numero;
        if(tunnusNumero >= seuraavaNumero)
            seuraavaNumero = tunnusNumero + 1;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        String erotin = "";
        for (int k = 0; k < getKenttia(); k++) { 
            sb.append(erotin);
            sb.append(anna(k));
            erotin = "|"; 
        }
        return sb.toString(); 
                
    }
    
    /** luetaan raaka-aineen tiedot ja asetetaan ne
     * @param rivi käsiteltävä rivi
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        for (int k = 0; k < getKenttia(); k++) {
            aseta(k, Mjonot.erota(sb, '|'));
        }
    }
    
   

    /**
     * @return palauttaa määrän
     * @example
     * <pre name="test">
     * RaakaAine aine = new RaakaAine();
     * aine.vastaaRaakaAine();
     * aine.getMaara() ~~~ 400;
     * </pre>
     */
    public double getMaara() {
       return maara;
    }

    /**
     * @return palauttaa suureen
     * @example
     * <pre name="test">
     * RaakaAine aine = new RaakaAine();
     * aine.vastaaRaakaAine();
     * aine.getSuure() === "g";
     * </pre>
     */
    public String getSuure() {
        return suure;
    }

    /** asettaa raaka-aineelle nimen
     * @param s asetettava merkkijono
     * @return palauttaa null, jos onnistuu
     * @example
     * <pre name="test">
     *  RaakaAine aine = new RaakaAine();
     *  aine.setNimi("makkara");
     *  aine.getNimi() === "makkara";
     * </pre>
     */
    public String setNimi(String s) {
        nimi = s;
        return null;
    }

    /** asettaa raaka-aineelle maaran
     * @param s asetettava merkkijono
     * @return palauttaa null onnistuessaan
     * @example
     * <pre name="test">
     * RaakaAine aine = new RaakaAine();
     * aine.setMaara("400");
     * aine.getMaara() ~~~ 400;
     * </pre>
     */
    public String setMaara(String s) {
        maara = Mjonot.erotaDouble(s, 0);
        return null;
    }

    /** asettaa raaka-aineelle suureen
     * @param s asetettava merkkijono
     * @return palauttaa nullin onnistuessaan
     * @example
     * <pre name="test">
     *  RaakaAine aine = new RaakaAine();
     *  aine.setSuure("g");
     *  aine.getSuure() === "g";
     * </pre>
     */
    public String setSuure(String s) {
        suure = s;
        return null;
    }
}
