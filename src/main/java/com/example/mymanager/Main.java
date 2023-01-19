package com.example.mymanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.OutSourced;
import model.Product;

import java.io.IOException;

/**
 * This is the main class. This is where my program starts.
 *
 * FUTURE ENHANCEMENT - I would like to add a user login feature that would also enable one to track/audit who last updated things.
 *
 * RUNTIME ERROR - A Runtime error I was having was an out of bound exception when I would create a new product the id was wrong.
 * I fix this issue by changing remembering that the id and index of an object are different and, I need to subtract 1 from the
 * ID when populating the table again.
 *
 */
public class Main extends Application {

    /**
     * This is start method that is called when the launch method is called.
     * This is where the first fxml is initialized.
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This is the main line of program. This is the first line of code that is called when the program starts.
     * Some test data is created to fill out some of the table views.
     * @param args
     */
    public static void main(String[] args) {
        InHouse part1 = new InHouse(1, "bolt", 00.50, 320, 50, 1000, 111);
        InHouse part2 = new InHouse(2, "nut", 00.33, 230, 50, 1000, 222);
        InHouse part3 = new InHouse(3, "screw", 00.65, 130, 30, 1000, 333);
        InHouse part4 = new InHouse(4, "hex", 00.55, 223, 20, 500, 444);
        InHouse part5 = new InHouse(5, "torque", 00.80, 122, 40, 600, 555);
        OutSourced part6 = new OutSourced(6, "brake", 20, 200, 20, 300, "ACME");

        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);
        Inventory.addPart(part4);
        Inventory.addPart(part5);
        Inventory.addPart(part6);

        Product prod1 = new Product(1, "bike", 200, 20, 5, 100);
        Product prod2 = new Product(2, "rc car", 300, 100, 10, 200);
        Product prod3 = new Product(3, "scooter", 500, 30, 10, 120);
        Product prod4 = new Product(4, "skateboard", 140, 120, 20, 300);
        Product prod5 = new Product(5, "snowboard", 560, 110, 20, 300);

        Inventory.addProduct(prod1);
        Inventory.addProduct(prod2);
        Inventory.addProduct(prod3);
        Inventory.addProduct(prod4);
        Inventory.addProduct(prod5);

        launch();
    }
}