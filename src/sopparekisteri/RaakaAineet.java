
package sopparekisteri;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author Jouni Ahola
 * @version 4.4.2019
 * Monikko-luokka, joka tekee raaka-aineet listaan
 */
public class RaakaAineet implements Iterable<RaakaAine>  {
    
    private String tiedostonNimi ="aineet"; 
    private  ArrayList<RaakaAine> alkiot = new ArrayList<RaakaAine>();
    private boolean muutettu = false;
   
    
    /**
     * raaka-aineen alustus
     */
    public RaakaAineet() {
        // ei käyttöä
    }
    
    /**
     * Lisätään uusi raaka-aine tietorakenteeseen.
     * @param raa lisättävä raaka-aine
     *@example
     * <pre name="test">
     * RaakaAine aine = new RaakaAine();
     * RaakaAineet aineet = new RaakaAineet();
     * aine.vastaaRaakaAine();
     * aineet.lisaa(aine);
     * </pre>
     */
    public void lisaa(RaakaAine raa) {
        alkiot.add(raa);
        muutettu = true;
        
    }
    

    /**
     * 
     * @param tiedosto tiedoston nimi
     * @throws SailoException virhe
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.File;
     * #import java.util.Iterator;
     * RaakaAineet aineet = new RaakaAineet();
     * RaakaAine aine = new RaakaAine();
     * RaakaAine aine2 = new RaakaAine();
     * RaakaAine aine3 = new RaakaAine();
     * RaakaAine aine4 = new RaakaAine();
     * RaakaAine aine5 = new RaakaAine();
     * aine.vastaaRaakaAine();
     * aine2.vastaaRaakaAine();
     * aine3.vastaaRaakaAine();
     * aine4.vastaaRaakaAine();
     * aine5.vastaaRaakaAine();
     * String tiedNimi = "ainetesti";
     * File ftied = new File(tiedNimi+".dat");
     * ftied.delete();
     * aineet.lueTiedostoa(tiedNimi); #THROWS SailoException
     * aineet.lisaa(aine);
     * aineet.lisaa(aine2);
     * aineet.lisaa(aine3);
     * aineet.lisaa(aine4);
     * aineet.lisaa(aine5);
     * aineet.tallenna();
     * aineet = new RaakaAineet();
     * aineet.lueTiedostoa(tiedNimi);
     * Iterator<RaakaAine> i = aineet.iterator();
     * i.next().toString() === aine.toString();
     * i.next().toString() === aine2.toString();
     * i.next().toString() === aine3.toString();
     * i.next().toString() === aine4.toString();
     * i.next().toString() === aine5.toString();
     * i.hasNext() === false;
     * aineet.lisaa(aine5);
     * aineet.tallenna();
     * ftied.delete() === true;
     * File fbak = new File(tiedNimi+".bak");
     * fbak.delete() === true;
     * </pre>
     */
    public void lueTiedostoa(String tiedosto) throws SailoException  {
        setTiedostonNimi(tiedosto);
        try(BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi()))){
            String rivi = "";
            while((rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                RaakaAine aine = new RaakaAine();
                aine.parse(rivi);
                lisaa(aine);
            }
            muutettu = false;
            
        } catch (FileNotFoundException e) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch (IOException e) {
           throw new SailoException ("Ongelmia tiedoston luvussa" + e.getMessage());
        }
    }
    
    /**
     * asettaa tiedostonnimen
     * @param tiedosto nimi
     */
    private void setTiedostonNimi(String tiedosto) {
        tiedostonNimi = tiedosto;
        
    }

    /**
     * @return palauttaa tiedoston perusnimen
     */
    public String getTiedostonPerusnimi() {
        return tiedostonNimi;
    }
    
    /**
     * tallennetaan muuttuneet tiedot raaka-aine tiedostoon
     * @throws SailoException virhe
     */
    public void tallenna() throws SailoException {
        if(!muutettu) return;
        
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);
        
        try (PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath()))){
            for(RaakaAine raa : this) {
                fo.println(raa.toString());
            }
        } catch (IOException e) {
            throw new SailoException ("Ongelmia tiedostoon kirjoittamisessa" + e.getMessage());
        }
        muutettu = false;
    }
   
    
    
    /**
     * @return palauttaa varmuuskopiotiedoston nimen
     */
    public String getBakNimi() {
        return tiedostonNimi +".bak";
    }
    
    /**
     * @return palauttaa tiedoston nimen
     */
    public String getTiedostonNimi() {
        return tiedostonNimi +".dat";
    }
    
       
    

    @Override
    public Iterator<RaakaAine> iterator() {
       return alkiot.iterator();
    }

    

    
    /** etsii raaka-aineista tietyn aineen
     * @param lista aineista
     * @return palauttaa listan löytyneistä raaka-aineista
     */
    public List<RaakaAine> anna(List<Integer> lista) {
        List<RaakaAine> loytyneet = new ArrayList<RaakaAine>();
        int i = 0;
        
        for(RaakaAine aine : alkiot ) {
           i = 0;
            for(int j = 0; j < lista.size(); j++) {
                if(aine.getTunnusNumero() == lista.get(i))  {
                    loytyneet.add(aine);
                    i++;
                    continue;
            }
            i++;
        }
            
           }
               
        
        return loytyneet;
    }

 /** lisätään tai korvataan listaan raaka-aine
 * @param aine korvattava tai lisättävä aine
 */
    public void korvaaTaiLisaa(RaakaAine aine) {
      int id = aine.getTunnusNumero();
      for( int i = 0; i < alkiot.size(); i++) {
          if(alkiot.get(i).getTunnusNumero()== id) {
             alkiot.set(i, aine);
             muutettu = true;
             return;
          }
      }
      lisaa(aine);   
  }

/** poistaa valitun raaka-aineen
 * @param aine poistettava raaka-aine
 */
    public void poista(RaakaAine aine) {
    alkiot.remove(aine);
    muutettu = true;
}

    
    /**
     *  luetaan tiedostoa
     * @throws SailoException virhe
     */
    public void lueTiedostoa() throws SailoException {
    lueTiedostoa(getTiedostonPerusnimi());
    
}

   
    
}
