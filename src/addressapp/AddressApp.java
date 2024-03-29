package addressapp;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AddressApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setTitle("AddressApp");
        stage.getIcons().add(new Image("file:src/resources/images/icon.png"));
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
