package controller;

import com.example.mymanager.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.OutSourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the class controller for Modify Part Screen
 */
public class ModifyPart implements Initializable {
    private static int id = 0;
    public TextField partID;
    public TextField partName;
    public TextField partStock;
    public TextField partPrice;
    public TextField partMax;
    public TextField optionalSource;
    public TextField partMin;
    public Label toggleLabel;
    public RadioButton inHouseRadioBtn;
    public RadioButton outsourcedRadioBtn;
    public ToggleGroup tGroup;
    public Label errorLabel;

    /**
     * Gets the selected partID and sets its to id field
     * @param partId set to id-1
     */
    public static void getSelectedPart(int partId) {
        id = partId-1;
    }

    /**
     * loads text fields with data from the selected part when first initialized
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Part part = Inventory.lookupPart(id);

        partID.setText(Integer.toString(part.getId()));
        partName.setText(part.getName());
        partStock.setText(Integer.toString(part.getStock()));
        partPrice.setText(Double.toString(part.getPrice()));
        partMax.setText(Integer.toString(part.getMax()));
        partMin.setText(Integer.toString(part.getMin()));

        if(Inventory.lookupPart(id) instanceof InHouse) {
            optionalSource.setText(Integer.toString(((InHouse)part).getMachineId()));
        } else {
            tGroup.selectToggle(outsourcedRadioBtn);
            toggleLabel.setText("Company Name");
            optionalSource.setText(((OutSourced)part).getCompanyName());
        }

        System.out.println("ModifyPart initialized");
    }

    /**
     * Updates Part data of the selected part
     * @param actionEvent save button clicked
     * @throws IOException
     */
    public void onSaveBtnClick(ActionEvent actionEvent) throws IOException {
        int id = Integer.parseInt(partID.getText());
        String name = partName.getText();;
        double price = 0;
        int stock = 0;
        int min = 0;
        int max = 0;
        String error = "Exception: ";
        boolean exceptions = false;

        if(name.isBlank()) {
            exceptions = true;
            error += "Name is empty!\n";
        }

        try{
            price = Double.parseDouble(partPrice.getText());
        } catch (NumberFormatException e) {
            exceptions = true;
            error += "Price must be a number!\n";
        }
        try{
            stock = Integer.parseInt(partStock.getText());
        } catch (NumberFormatException e) {
            exceptions = true;
            error += "Stock must be a number!\n";
        }
        try{
            min = Integer.parseInt(partMin.getText());
        } catch (NumberFormatException e) {
            exceptions = true;
            error += "Min must be a number!\n";
        }
        try{
            max = Integer.parseInt(partMax.getText());
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
            error += "Min must be less than Max\n";
        }

        if(inHouseRadioBtn.isSelected()) {
            int machineId = 0;
            try{
                machineId = Integer.parseInt(optionalSource.getText());
            } catch (NumberFormatException e) {
                exceptions = true;
                error += "Machine ID must be a number!";
            }

            if(exceptions) {
                errorLabel.setText(error);
                return;
            }

            InHouse part = new InHouse(id, name, price, stock, min, max, machineId);
            Inventory.updatePart(this.id, part);
        } else {
            String company = optionalSource.getText();
            if(company.isBlank()) {
                exceptions = true;
                error += "Company Name is empty!";
            }

            if(exceptions) {
                errorLabel.setText(error);
                return;
            }

            OutSourced part = new OutSourced(id, name, price, stock, min, max, company);
            Inventory.updatePart(this.id, part);
        }

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
     * Updates the text of the toggle label to Machine ID
     * @param actionEvent inHouse Radio button selected
     */
    public void inHouseSelected(ActionEvent actionEvent) { toggleLabel.setText("Machine ID");
    }

    /**
     * Updates the text of the toggle label to Company Name
     * @param actionEvent outsourced Radio button selected
     */
    public void outsourcedSelected(ActionEvent actionEvent) { toggleLabel.setText("Company Name");
    }
}
