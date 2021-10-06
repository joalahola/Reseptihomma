
package sopparekisteri;

import java.io.BufferedReader;
import java.io.File;
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
 * Toimii niin sanottuna "monikkoluokkana".
 */
public class Yhdistaa implements Iterable <Yhdista> {
    
    private String tiedostonNimi ="taulukko";
    private boolean muutettu = false;
    
    private  ArrayList<Yhdista> alkiot = new ArrayList<Yhdista>();

   
    
    /**
     * lisää uuden id:n tietorakenteeseen.
     * @param yhd lisättävä id
     * @example
     * <pre name="test">      
     * Yhdistaa taulukko = new Yhdistaa();
     * Yhdista yhd1 = new Yhdista(); taulukko.lisaa(yhd1);
     * Yhdista yhd2 = new Yhdista(); taulukko.lisaa(yhd2);
    
     * </pre>
     */
    public void lisaa (Yhdista yhd) {
        alkiot.add(yhd);
        muutettu = true;
    }
    
    /**
     * @param id ruuan indeksi
     * @return palauttaa taulukon löytyneistä raaka-aineista
     */
    public List<Yhdista> AnnaTiedot(int id){
        List<Yhdista> loytyneet = new ArrayList<Yhdista>();
        for(Yhdista raa : alkiot )
            if(raa.getId()== id) loytyneet.add(raa);
        return loytyneet;      
    }
    /** testipääohjelma
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Yhdista yhd = new Yhdista(1,1);
        yhd.rekisteroi();
        Yhdistaa taulukko = new Yhdistaa();
        Yhdista yhd2 = new Yhdista(2,1);
        yhd2.rekisteroi();
        Yhdista yhd3 = new Yhdista(1,2);
        yhd3.rekisteroi();
        Yhdista yhd4= new Yhdista(3);
        yhd4.rekisteroi();
        taulukko.lisaa(yhd);
        taulukko.lisaa(yhd2);
        taulukko.lisaa(yhd3);
        taulukko.lisaa(yhd4);
        System.out.println("============= lista testi =================");
        List<Yhdista> raakaaineet2 = taulukko.AnnaTiedot(2);
        for (Yhdista raa : raakaaineet2) {
            System.out.print(raa.getId() + " ");
            raa.tulosta(System.out);
    }
    


}

    /** palauttaa id-taulukon koon
     * @return id taulukon koko
     */
    public int getLkm() {
        return alkiot.size();
    }

    /** palauttaa tietyn kohdan tiedot
     * @param id minkä tiedot halutaan
     * @return palauttaa tietyn id:n tiedot
     */
    public  int anna(int id) {
       return alkiot.get(id).getAineId();
    }
    
    /** luetaan taulukon tiedot tiedostosta
     * @param tiedosto josta luetaan
     * @throws SailoException virhe
     */
    public void lueTiedostoa(String tiedosto) throws SailoException {
        try(BufferedReader fi = new BufferedReader(new FileReader(tiedosto))){
            String rivi = "";
            while((rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Yhdista yhd = new Yhdista();
                yhd.parse(rivi);
                lisaa(yhd);
            }
            muutettu = false;
        } catch (IOException e) {
            throw new SailoException ("Ongelmia tiedoston luvussa" + e.getMessage());
        }
    }
    
    /** palauttaa tiedoston nimen
     * @return  tiedostonnimen
     */
    public String getTiedostonPerusnimi() {
        return tiedostonNimi;
    }
    
    /**
     * tallennetaan muuttuneet tiedot tiedostoon
     * @throws SailoException virhe
     */
    public void tallenna() throws SailoException {
        if(!muutettu) return;
        
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);
        
        try (PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath()))){
            for(Yhdista yhd : this) {
                fo.println(yhd.toString());
            }
        } catch (IOException e) {
            throw new SailoException ("Ongelmia tiedostoon kirjoittamisessa" + e.getMessage());
        }
        muutettu = false;
    }
   
    
    
    /** palauttaa varatiedoston nimen
     * @return varatiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonNimi +".bak";
    }
    
    /** palauttaa tiedoston nimen
     * @return tiedostonnimi
     */
    public String getTiedostonNimi() {
        return tiedostonNimi +".dat";
    }

    @Override
    public Iterator<Yhdista> iterator() {
        return alkiot.iterator();
    }

    /** luetaan tiedosto
     * @throws SailoException virhe
     */
    public void lueTiedostoa() throws SailoException {
      lueTiedostoa(getTiedostonNimi());
        
    }
    
    
    /**
     * @param tunnusNumero ruuan id-numero
     * @return palauttaa listan löydetyistä raaka-aineista
     */
    public List<Integer> aineet(int tunnusNumero) {
        List<Integer> loytyneet = new ArrayList<Integer>();
        for(Yhdista yhd : alkiot)
            if(yhd.getRuokaId()== tunnusNumero) loytyneet.add(yhd.getAineId());
        return loytyneet;
    }

    /**poistaa kaikki viittaukset taulukosta kyseiseen ruokaan
     * @param tunnusnumero poistettava ruuan tunnusnumero
     */
    public void poista(int tunnusnumero) {
       for(int i = 0; i < alkiot.size(); i++) {
           if(alkiot.get(i).getRuokaId()== tunnusnumero) {
               alkiot.remove(i);
               i--;
           }
      
       }
        muutettu = true;
    }
   
}
