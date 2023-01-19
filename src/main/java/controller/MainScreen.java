package controller;

import com.example.mymanager.Main;
import javafx.application.Platform;
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
 * This is the MainScreen Controller for MainScreen FXML.
 */
public class MainScreen implements Initializable {
    private static String searchQuery = "";
    public TableView<Product> productsTable;
    public TableView<Part> partsTable;
    public TableColumn<Part, Integer> idCol;
    public TableColumn nameCol;
    public TableColumn<Part, Double> inventoryLevelCol;
    public TableColumn<Part, Double> costCol;
    public TableColumn productIdCol;
    public TableColumn productNameCol;
    public TableColumn productStockCol;
    public TableColumn productPriceCol;
    public TextField partSearchBox;
    public TextField productSearchBox;
    public Label errorLabel;

    /**
     * loads tables with data when first initialized
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        inventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        costCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        partsTable.setItems(Inventory.getAllParts());

        productIdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        productStockCol.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        productsTable.setItems(Inventory.getAllProducts());
    }

    /**
     * Exits the application
     * @param actionEvent exit button clicked
     */
    public void onExitBtnClick(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Sends you to the Add Part Screen
     * @param actionEvent add button clicked
     * @throws IOException
     */
    public void onAddPartBtnClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/AddPart.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sends you to the Modify Part Screen of selected Part
     * @param actionEvent modify button clicked
     * @throws IOException
     */
    public void onModifyPartBtnClick(ActionEvent actionEvent) throws IOException {
        Part selected = partsTable.getSelectionModel().getSelectedItem();

        if(selected == null) {
            errorLabel.setText("Please select a Part to Modify");
            return;
        } else {
            ModifyPart.getSelectedPart(selected.getId());
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/ModifyPart.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Deletes a selected Part
     * @param actionEvent delete button clicked
     */
    public void onDeletePartBtnClick(ActionEvent actionEvent) {
        Part selected = partsTable.getSelectionModel().getSelectedItem();

        if(selected == null) {
            errorLabel.setText("Please select a Part to Delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Part");
        alert.setHeaderText("Delete");
        alert.setContentText("Are you sure you want to Delete?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            Inventory.deletePart(selected);
        }
    }

    /**
     * Sends you to the Add Product Screen
     * @param actionEvent add button clicked
     * @throws IOException
     */
    public void onAddProductBtnClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/AddProduct.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sends you to Modify Screen for selected Product
     * @param actionEvent modify button clicked
     * @throws IOException
     */
    public void onModifyProductBtnClick(ActionEvent actionEvent) throws IOException {
        Product selected = productsTable.getSelectionModel().getSelectedItem();

        if(selected == null) {
            errorLabel.setText("Please select a Product to Modify");
            return;
        } else {
            ModifyProduct.getSelectedProduct(selected.getId());
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/ModifyProduct.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Deletes the selected Product
     * @param actionEvent delete button clicked
     */
    public void onDeleteProductBtnClick(ActionEvent actionEvent) {
        Product selected = productsTable.getSelectionModel().getSelectedItem();

        if(selected == null) {
            errorLabel.setText("Please select a Product to Delete");
            return;
        } else if (selected.getAssociatedParts().size() > 0) {
            errorLabel.setText("Can't Delete...Product has associated parts!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Part");
        alert.setHeaderText("Delete");
        alert.setContentText("Are you sure you want to Delete?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            Inventory.deleteProduct(selected);
        }
    }

    /**
     * Starts searching for part when typing and displays on tableview
     * @param keyEvent keypress
     */
    public void partKeyTypedSearch(KeyEvent keyEvent) {
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

    /**
     * Starts searching for Product when typing and displays on tableview
     * @param keyEvent keypress
     */
    public void productKeyTypedSearch(KeyEvent keyEvent) {
        searchQuery = productSearchBox.getText();
        errorLabel.setText("");

        if(searchQuery.equals("")) {
            productsTable.setItems(Inventory.getAllProducts());
        } else {
            if(searchQuery.matches("[0-9]+")) {
                int id = Integer.parseInt(searchQuery);
                productsTable.setItems(Inventory.getAllProducts().filtered(product -> product.getId() == id));
            } else {
                productsTable.setItems(Inventory.lookupProduct(searchQuery));
            }

            if(productsTable.getItems().size() == 0) {
                errorLabel.setText("No products were found!");
            }
        }

        if(productsTable.getItems().size() < 2) {
            productsTable.getSelectionModel().select(0);
        }
    }
}
