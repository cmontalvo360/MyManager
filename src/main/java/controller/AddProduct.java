package controller;

import com.example.mymanager.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is a controller for the Add Product Screen
 */
public class AddProduct implements Initializable {
    private static String searchQuery = "";
    private static ObservableList<Part> associatedList = FXCollections.observableArrayList();
    public TextField productID;
    public TextField productName;
    public TextField productStock;
    public TextField productPrice;
    public TextField productMax;
    public TextField productMin;
    public TableView<Part> partsTable;
    public TableColumn partIdCol;
    public TableColumn partNameCol;
    public TableColumn partStockCol;
    public TableColumn partPriceCol;
    public TableView<Part> associatedPartsTable;
    public TableColumn associatedIDCol;
    public TableColumn associatedNameCol;
    public TableColumn associatedStockCol;
    public TableColumn associatedPriceCol;
    public TextField partSearchBox;
    public Label errorLabel;

    /**
     * Loads the top table with all the parts available when initialized
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        partsTable.setItems(Inventory.getAllParts());

        associatedIDCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        associatedNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        associatedStockCol.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        associatedPriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        associatedPartsTable.setItems(associatedList);
    }

    /**
     * Creates a new Product and sends you to MainScreen if Requirements are met
     * @param actionEvent save button clicked
     * @throws IOException
     */
    public void onSaveBtnClick(ActionEvent actionEvent) throws IOException {
        int id = Inventory.getAllProducts().size() + 1;
        String name = productName.getText();;
        double price = 0;
        int stock = 0;
        int min = 0;
        int max = 0;
        String error = "Exceptions: ";
        boolean exceptions = false;

        if(name.isBlank()) {
            exceptions = true;
            error += "Name is empty!\n";
        }

        try{
            price = Double.parseDouble(productPrice.getText());
        } catch (NumberFormatException e) {
            exceptions = true;
            error += "Price must be a number!\n";
        }
        try{
            stock = Integer.parseInt(productStock.getText());
        } catch (NumberFormatException e) {
            exceptions = true;
            error += "Stock must be a number!\n";
        }
        try{
            min = Integer.parseInt(productMin.getText());
        } catch (NumberFormatException e) {
            exceptions = true;
            error += "Min must be a number!\n";
        }
        try{
            max = Integer.parseInt(productMax.getText());
        } catch (NumberFormatException e) {
            exceptions = true;
            error += "Max must be a number!\n";
        }

        if(min < max) {
            if(stock > max || stock < min) {
                exceptions = true;
                error += "Inv must be between Min and Max\n";
            }
        } else {
            exceptions = true;
            error += "Min must be less than Max";
        }

        if(exceptions) {
            errorLabel.setText(error);
            return;
        }

        Product product = new Product(id, name, price, stock, min, max);
        for (Part part: associatedList) {
            product.addAssociatedPart(part);
        }

        Inventory.addProduct(product);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sends you to MainScreen without saving
     * @param actionEvent
     * @throws IOException
     */
    public void onCancelBtnClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Removes part from bottom table and associatedlist
     * @param actionEvent remove button selected
     */
    public void onRemovePartClick(ActionEvent actionEvent) {
        Part part = associatedPartsTable.getSelectionModel().getSelectedItem();

        if(part == null) {
            errorLabel.setText("Please select a part to Remove");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Associated Parts");
        alert.setHeaderText("Remove");
        alert.setContentText("Are you sure you want to Remove?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            associatedList.remove(part);
        }
    }

    /**
     * Adds Part from top table to bottom table(associated parts)
     * @param actionEvent add button clicked
     */
    public void onAddPartClick(ActionEvent actionEvent) {
        Part selected = partsTable.getSelectionModel().getSelectedItem();

        if(selected == null) {
            errorLabel.setText("Please select a part to Add");
            return;
        } else {
            associatedList.add(selected);
        }
    }

    /**
     * Starts searching for part when typing and displays on tableview
     * @param keyEvent keypress
     */
    public void partKeyTyped(KeyEvent keyEvent) {
        searchQuery = partSearchBox.getText();
        errorLabel.setText("");

        if(searchQuery.equals("")) {
            partsTable.setItems(Inventory.getAllParts());
        } else {
            if(searchQuery.matches("[0-9]+")) {
                int id = Integer.parseInt(searchQuery);
                partsTable.setItems(Inventory.getAllParts().filtered(part -> part.getId() == id));
            } else {
                partsTable.setItems(Inventory.lookupPart(searchQuery));
            }

            if(partsTable.getItems().size() == 0) {
                errorLabel.setText("No parts were found!");
            }
        }

        if(partsTable.getItems().size() < 2 && !searchQuery.equals("")) {
            partsTable.getSelectionModel().select(0);
        }
    }
}
