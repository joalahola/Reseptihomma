
package sopparekisteri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;

/** osaa lisätä muuan muuassa uuden ruuan.
 * @author Jouni Ahola
 * @version 4.4.2019
 *
 */
public class Ruuat implements Iterable <Ruoka> {
    private static int  MAXRUOKIA = 8;
    private int lkm = 0;
    private String tiedostonNimi ="ruuat";
    private Ruoka alkiot[]= new Ruoka[MAXRUOKIA];
    private boolean muutettu = false;
    
    /**
     * oletusmuodostaja
     */
    public Ruuat() {
        // tälle ei ole nyt käyttöä
    }
    /**
     * testipääohjelma
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Ruuat ruuat = new Ruuat();
        
        Ruoka makaronilaatikko= new Ruoka();
        makaronilaatikko.rekisteroi();
        makaronilaatikko.vastaaRuoka();
        
        ruuat.lisaa(makaronilaatikko);
        ruuat.lisaa(makaronilaatikko);
        ruuat.lisaa(makaronilaatikko);
        ruuat.lisaa(makaronilaatikko);
        ruuat.lisaa(makaronilaatikko);
        ruuat.lisaa(makaronilaatikko);
        ruuat.lisaa(makaronilaatikko);
        ruuat.lisaa(makaronilaatikko);
        ruuat.lisaa(makaronilaatikko);
        ruuat.lisaa(makaronilaatikko);
        ruuat.lisaa(makaronilaatikko);
        
  
    }
    /**
     * lisää ruuan ruokiin sekä luo taulukkoon tarvittaessa lisää tilaa 
     * @param ruoka mikä ruoka lisätään
     * @example
     * <pre name="test">
     * Ruuat ruuat = new Ruuat();
     * Ruoka r1 = new Ruoka();
     * Ruoka r2 = new Ruoka();
     * ruuat.getLkm() === 0;
     * ruuat.lisaa(r1); ruuat.getLkm() === 1;
     * ruuat.lisaa(r2); ruuat.getLkm() === 2;
     * ruuat.lisaa(r1); ruuat.getLkm() === 3;
     * ruuat.anna(0) === r1;
     * ruuat.anna(1) === r2;
     * ruuat.anna(2) === r1;
     * </pre>
     */
    public void lisaa(Ruoka ruoka) {
        if(lkm >=alkiot.length) {
            Ruoka valiTaulukko[] = new Ruoka[alkiot.length * 2];
            for(int i =0;i < alkiot.length; i++)
                valiTaulukko[i] = alkiot[i];
            alkiot = valiTaulukko;
            MAXRUOKIA = getLkm() * 2;
        }
        alkiot[lkm] = ruoka;
        lkm++;
        muutettu = true;
        
        
    }
    
    /**
     * palauttaa rekisterissä olevien ruokien lukumäärän
     * @return ruokien lukumäärän
     * @example
     * <pre name="test">
     * Ruuat ruokia = new Ruuat();
     * Ruoka peru = new Ruoka();
     * Ruoka peru2 = new Ruoka();
     * ruokia.getLkm() === 0;
     * ruokia.lisaa(peru);
     * ruokia.lisaa(peru2);
     * ruokia.getLkm() === 2;
     * </pre>
     */
    public int getLkm() {
        return lkm;
    }
    
   
    /**
     * 
     * @param i indeksi
     * @return palauttaa taulukon i:n arvon
     */
    public Ruoka anna(int i) {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    /** luetaan ruokien tiedot tiedostosta
     * @param tiedosto luettava tiedosto
     * @throws SailoException virhe
     */
    public void lueTiedostoa(String tiedosto) throws SailoException {
        try(BufferedReader fi = new BufferedReader(new FileReader(tiedosto))){
            String rivi = "";
            while((rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Ruoka ruoka = new Ruoka();
                ruoka.parse(rivi);
                lisaa(ruoka);
            }
            muutettu = false;
        } catch (IOException e) {
            throw new SailoException ("Ongelmia tiedoston luvussa" + e.getMessage());
        }
    }
    
    /** palautetaan tiedoston perusnimi
     * @return tiedoston perusnimi
     */
    public String getTiedostonPerusnimi() {
        return tiedostonNimi;
    }
    
    /** tallennetaan ruokien tiedot tiedostoon
     * @throws SailoException virhe
     * muodostetaan myös samalla varatiedosto
     */
    public void tallenna() throws SailoException {
        if(!muutettu) return;
        
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);
        
        try (PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath()))){
            for(Ruoka ruoka : this) {
                fo.println(ruoka.toString());
            }
        } catch (IOException e) {
            throw new SailoException ("Ongelmia tiedostoon kirjoittamisessa" + e.getMessage());    
        }
        muutettu = false;
    }
   
    
    
    /** palautetaan varatiedoston nimi
     * @return varatiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonNimi +".bak";
    }
    
    /** palautetaan tiedoston varsinainen nimi
     * @return tiedostonnimi
     */
    public String getTiedostonNimi() {
        return tiedostonNimi +".dat";
    }
    
    /**
     * 
     * luokka ruokien iteroimiseksi
     *
     */
    public class RuuatIterator implements Iterator<Ruoka> {
        private int kohdalla = 0;
        
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }
        
        @Override
        public Ruoka next() {
            if(!hasNext()) throw new NoSuchElementException("Ei ole");
            return anna(kohdalla++);
            
        }
        
        @Override
        public void remove() throws UnsupportedOperationException{
            throw new UnsupportedOperationException();
        }
    }
        
        @Override
        public Iterator <Ruoka>iterator(){
            return new RuuatIterator();
        }
        
        /**haetaan hakuehdon täyttämien ruokien tiedot listaan ja lajitellaan ne samalla
         * @param hakuehto minkä perusteella haetaan
         * @param k minkä kentän perusteella haetaan, tässä tapauksesssa nimen perusteella
         * @return loytyneet ruuat, jotka täyttivät hakuehdot
         */
        public Collection <Ruoka>etsi(String hakuehto, int k) {
            String ehto = "";
            if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto;
            int hk = k; 
            if ( hk <= 0 ) hk = 1;
            List<Ruoka> loytyneet = new ArrayList<Ruoka>();
            
            for (Ruoka ruoka : this) {
                if (WildChars.onkoSamat(ruoka.anna(hk), ehto)) loytyneet.add(ruoka); 
            }
            Collections.sort(loytyneet);
            return loytyneet;
        }
        
        /**
         * tiedoston lukeminen
         * @throws SailoException virhe
         */
        public void lueTiedostoa() throws SailoException {
           lueTiedostoa(getTiedostonNimi());
            
        }
        
        /**
         * @param ruoka lisättäva tai korvattava ruoka
         * @throws SailoException  heittää tämän
         */
        public void korvaaTaiLisaa(Ruoka ruoka) throws SailoException {
            int id = ruoka.getTunnusNumero();
            for (int i = 0; i < lkm; i++) {
                if ( alkiot[i].getTunnusNumero() == id ) {
                    alkiot[i] = ruoka;
                    muutettu = true;
                    return;
                }
            }
            lisaa(ruoka);
        }
        
        /** poistetaan ruoka tunnusnumeron perusteella. Asetetaan alkion kohdalle null
         * @param tunnusNumero poistettava ruoka
         */
        public void poista(int tunnusNumero) {
           int ind = tunnusNumero;
           if (ind < 0) return ; 
           lkm--;
           for (int i = ind; i < lkm; i++) 
               alkiot[i] = alkiot[i + 1];
           alkiot[lkm] = null; 
           muutettu = true; 
        }
       
    }


