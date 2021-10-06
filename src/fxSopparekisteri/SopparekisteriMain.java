package fxSopparekisteri;	
import javafx.application.Application;
import javafx.stage.Stage;
import sopparekisteri.Rekisteri;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author Jouni Ahola
 * @version 4.4.2019
 *
 */
public class SopparekisteriMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
		    final FXMLLoader ldr = new FXMLLoader(getClass().getResource("SopparekisteriGUIView.fxml"));
		    final Pane root = (Pane)ldr.load();
		    final SopparekisteriGUIController rekisteriCtrl = (SopparekisteriGUIController)ldr.getController();
		   	//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("SopparekisteriGUIView.fxml"));
		    final Scene scene = new Scene(root,800,600);
		    //Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("sopparekisteri.css").toExternalForm());
			
			primaryStage.setScene(scene);
			Rekisteri rekisteri = new Rekisteri();
            rekisteriCtrl.setRekisteri(rekisteri);
			primaryStage.show();
			rekisteriCtrl.lueTiedostoa();
			primaryStage.setTitle("Reseptikokoelma");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
