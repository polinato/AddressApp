package addressapp;

import java.io.IOException;
import java.sql.SQLException;

import db.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.DateUtil;
import view.PersonEditDialogController;

public class FXMLDocumentController {
    
    private ObservableList<Person> personData = FXCollections.observableArrayList();
    
    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;
    
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;
    
    @FXML    
    public void initialize() {

        DatabaseHandler databaseHandler = new DatabaseHandler();
        personData = databaseHandler.readDBTable();

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));

        personTable.setItems(personData);

        showPersonDetails(null);
        
        personTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
    }   
    
    private void showPersonDetails(Person person) {
        
        if (person != null) {
            
            firstNameLabel.setText(person.getFirstname());
            lastNameLabel.setText(person.getLastname());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());
            birthdayLabel.setText(DateUtil.format(person.getBirthday()));
        } 
        
        else {
            
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }   
    }
    
    @FXML
    private void handleDeletePerson() {
        
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            System.out.println("Index: " + selectedIndex);
            System.out.println("table fn: " + personData.get(selectedIndex).getFirstname());
            DatabaseHandler db = new DatabaseHandler();
            db.deletePerson(personData.get(selectedIndex));
            personTable.getItems().remove(selectedIndex);
        }
        else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();
        }
    }
    
   public boolean showPersonEditDialog(Person person) {
       
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PersonEditDialogController.class.getResource("PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(null);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);
            dialogStage.getIcons().add(new Image("file:src/resources/images/icon.png"));         


            dialogStage.showAndWait();
            return controller.isOkClicked();
        } 
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
   
   @FXML
    private void handleNewPerson() throws SQLException, ClassNotFoundException {
        
        Person tempPerson = new Person();
        boolean okClicked = showPersonEditDialog(tempPerson);
        
        if (okClicked) {
            personData.add(tempPerson);
            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.addPersonToDB(tempPerson);
            personTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
        }
    }
    
    @FXML
    private void handleEditPerson() {
        
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        
        if (selectedPerson != null) {
            
            boolean okClicked = showPersonEditDialog(selectedPerson);
            
            if (okClicked) {
                
                DatabaseHandler databaseHandler = new DatabaseHandler();
                showPersonDetails(selectedPerson);
                int selectedIndex = personTable.getSelectionModel().getSelectedIndex();

                databaseHandler.changePerson(selectedIndex, selectedPerson);
                personData.set(selectedIndex, selectedPerson);
            }
        } 
        
        else {          
            
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();
        }
    }  
}
