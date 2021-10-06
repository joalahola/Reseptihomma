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
 * Rekisterin Ruoka, joka osaa huolehtia omasta tunnusnumerostaan
 */
public class Ruoka implements Cloneable, Comparable <Ruoka> {
    private int tunnusnumero;
    private String nimi ="";
    private String ohje ="";
    
    private static int seuraavaNumero =1;
   
    /**
     * testipääohjelma
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Ruoka makaronilaatikko = new Ruoka();
        Ruoka makaronilaatikko2 = new Ruoka();
        
        makaronilaatikko.rekisteroi();
        makaronilaatikko2.rekisteroi();
        
        makaronilaatikko.vastaaRuoka();
        makaronilaatikko.tulosta(System.out);
        
        makaronilaatikko2.vastaaRuoka();
        makaronilaatikko2.tulosta(System.out);
    
    }
      /**
      * Tulostetaan ruuan tiedot
      * @param os tietovirta johon tulostetaan
      * 
      */
       public void tulosta(OutputStream os) {
              tulosta(new PrintStream(os));
         }
     /**
     * @param out tietovirta johon tulostetaan.
     */
    public void tulosta(PrintStream out) {
           out.println(tunnusnumero +" "+nimi);
           out.println(ohje);
       }
    
       /**
     * Tehdään esimerkkiruoka esimerkkitiedoilla.
     * @example
     * <pre name="test">
     * Ruoka ruoka = new Ruoka();
     * ruoka.vastaaRuoka();
     * ruoka.getNimi() === "Makaronilaatikko";
     * </pre>
     */
    public void vastaaRuoka() {
        nimi = "Makaronilaatikko";
        ohje="Keitä jauhelihaa ja makaroneja. Sitten laita uuniin 30 minuutiksi hautumaan.";
        
    }
       /**
        * antaa ruualle seuraavan rekisterinumeron.
        * @return ruuan uusi tunnusnumero
        * @example
        * <pre name="test">
        * Ruoka m1 = new Ruoka();
        * m1.getTunnusNumero() === 0;
        * m1.rekisteroi();
        * Ruoka m2 = new Ruoka();
        * m2.rekisteroi();
        * int n1 = m1.getTunnusNumero();
        * int n2 =  m2.getTunnusNumero();
        * n1 === n2-1;
        * </pre>
        */
       public int rekisteroi() {
       tunnusnumero = seuraavaNumero;
       seuraavaNumero++;
       return tunnusnumero;
        
    }
       /**
        * palauttaa tunnusnumeron
        * @return tunnusnumero
        * @example
        * <pre name="test">
        * Ruoka laatikko = new Ruoka();
        * laatikko.getTunnusNumero() === 0;
        * laatikko.rekisteroi();
        * Ruoka laatikko2 = new Ruoka();
        * laatikko2.getTunnusNumero() === 0;
        * </pre>
        */
       public int getTunnusNumero() {
           return tunnusnumero;
       }
       
       /**
     * @return palauttaa ruuan nimen
     * @example
     * <pre name="test">
     * Ruoka makaronilaatikko = new Ruoka();
     * makaronilaatikko.vastaaRuoka();
     * makaronilaatikko.getNimi() =R= "Makaronilaatikko";
     * </pre>
     */
    public String getNimi() {
           return nimi;
       }
    /**
     * asettaa tunnusnumeron ja huolta pitää siitä että seuraava numero on isompi kuin tunnusnumero
     * @param numero asetettava tunnusnumero
     */
    private void setTunnusNumero(int numero) {
        tunnusnumero = numero;
        if(tunnusnumero >= seuraavaNumero)
            seuraavaNumero = tunnusnumero + 1;
    }
    
    @Override
    public String toString() {
        return "" + 
                getTunnusNumero() + "|" +
                nimi +"|" + 
                ohje;
                
    }
    
    /** käsitellään rivi ja luetaan siitä tiedot ruokaan
     * @param rivi jota käsitellään
     * @example
     * <pre name="test">
     * Ruoka uusi = new Ruoka();
     * uusi.parse("   1  |  Jauhelihakeitto  | Keitetään");
     * </pre>
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        setTunnusNumero(Mjonot.erota(sb,'|',getTunnusNumero()));
        nimi = Mjonot.erota(sb,'|',nimi);
        ohje = Mjonot.erota(sb,'|',ohje);
    }
    
    @Override
    public boolean equals(Object ruoka) {
        if(ruoka == null) return false;
        return this.toString().equals(ruoka.toString());
    }
    
    @Override
    public int hashCode() {
        return tunnusnumero;
    }
    /**
     * palauttaa ruuan ohjeen
     * @return ohje
     * @example
     * <pre name="test">
     * Ruoka uusi = new Ruoka();
     * uusi.vastaaRuoka();
     * uusi.getOhje() ==="Keitä jauhelihaa ja makaroneja. Sitten laita uuniin 30 minuutiksi hautumaan.";
     * </pre>
     */
    public String getOhje() {
        return ohje;
    }
    
    @Override
    public Ruoka clone() throws CloneNotSupportedException  {
        Ruoka uusi;
        uusi = (Ruoka) super.clone();
        return uusi;
    }
    
    
    /**
     * asettaa ruualle nimen
     * @param s ruualle annettava nimi
     * @return palauttaa virheen, nullin jos onnistuu
     * @example
     * <pre name="test">
     * Ruoka uusi = new Ruoka();
     * uusi.setNimi("Perulaatikko");
     * uusi.getNimi() === "Perulaatikko";
     * </pre>
     */
    public String setNimi(String s) {
        nimi = s;
        return null;
    }
    
    /**asettaa ruualle ohjeen
     * @param s annettava ohje
     * @return palauttaa virheilmoituksen, onnistuessaan nullin
     * @example
     * <pre name="test">
     * Ruoka uusi = new Ruoka();
     * uusi.setNimi("Perulaatikko");
     * uusi.setOhje("Keitetään");
     * uusi.getOhje() === "Keitetään";
     * </pre>
     */
    public String setOhje(String s) {
        ohje = s;
        return null;
    }
    
    /**
     * Antaa k:n kentän sisällön merkkijonona
     * @param k monenenko kentän sisältö palautetaan
     * @return kentän sisältö merkkijonona
     */
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + tunnusnumero;
        case 1: return "" + nimi;
        default: return "Jaahas";
        }
    }
    
    /**
     * tämän avulla lajitellaan ruuat aakkosjärjestykseen
     */
    @Override
    public int compareTo(Ruoka ruoka) {
       return nimi.compareToIgnoreCase(ruoka.nimi);
    }

}
