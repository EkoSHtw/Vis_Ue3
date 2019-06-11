package Ue3;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{

    	  Parent root = FXMLLoader.load(getClass().getResource("Layout.fxml"));
    	    
          Scene scene = new Scene(root, 1500, 650);
      
          primaryStage.setTitle("FXML Welcome");
          primaryStage.setScene(scene);
          primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
