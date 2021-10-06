
package sopparekisteri;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Jouni Ahola
 * @version 4.4.2019
 * Toimii ns. "yksikköluokkana" Yhdistaa-luokalle
 */
public class Yhdista {
    
    private int id;
    private int ruokaid;
    private int aineid;
    private static int seuraavaNumero = 1;

    /** testipääohjelma
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Yhdista yhd = new Yhdista(1, 1); // makaronilaatikko, jauheliha
        yhd.tulosta(System.out);
        yhd.rekisteroi();
        Yhdista yhd2 = new Yhdista(1,2); // makaronilaatikko, suola
        yhd2.rekisteroi();
        yhd2.tulosta(System.out);
    
    }
    
    /**
     * id alustus
     * @param ruokanumero ruua numero
     * @param ainenumero aineen numero
     */
    public Yhdista(int ruokanumero, int ainenumero) {
      this.id = seuraavaNumero;
      this.ruokaid= ruokanumero;
      this.aineid = ainenumero;
    }
    
    /** alustetaan taulukkoon ruoka
     * @param ruokanumero ruuan numero
     */
    public Yhdista(int ruokanumero) {
        this.id = seuraavaNumero;
        this.ruokaid = ruokanumero;
    }
    
    /**
     * oletusmuodostaja, ei käyttöä ainakaan nyt
     */
    public Yhdista() {
        // ei sisältöä
    }

    /**
     * antaa id:lle seuraavan numeron
     * @return id:n uusi id-numero
     */
    public int rekisteroi() {
        id = seuraavaNumero;
        seuraavaNumero++;
        return id;
    }
    
    /**
     * palauttaa id:n numeron
     * @return palautettava id
     * @example
     * <pre name="test">
     * Yhdista id = new Yhdista();
     * id.getId() === 0;
     * </pre>
     */
    public int getId() {
        return id;
    }
    /**
     * @param os tietovirta johon tulostetaan.
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
           
          
       }
    /**
     * tulostaa raaka-aineen tiedot
   * @param out tietovirta mihin tulostetaan
   */
  public void tulosta(PrintStream out) {
        out.println(+ruokaid +" " + aineid);
    }

    /** palauttaa ruuan numeron
     * @return palauttaa ruuan id-numeron
     */
    public int getRuokaId() {
       return ruokaid;
    }


    
    /** palauttaa raaka-aineen numeron
     * @return raaka-aineen numeron
     */
    public int getAineId() {
        return aineid;
    }
    /**
     * asettaa tunnusnumeron ja huolta pitää siitä että seuraava numero on isompi kuin tunnusnumero
     * @param numero asetettava tunnusnumero
     */
    private void setTunnusNumero(int numero) {
        id = numero;
        if(id >= seuraavaNumero)
            seuraavaNumero = id++;
    }
    
    @Override
    public String toString() {
        return "" + 
                getId() + "|" +
                ruokaid +"|" + 
                aineid;
                
                
    }
    
    /** luetaan taulukkoon tiedostosta ja muodostetaan tiedot
     * @param rivi mistä luetaan
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        setTunnusNumero(Mjonot.erota(sb,'|',getId()));
        ruokaid = Mjonot.erota(sb,'|',ruokaid);
        aineid = Mjonot.erota(sb,'|',aineid);
       
    }

}
