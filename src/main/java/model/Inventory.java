package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This is the model class for Inventory
 */
public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * @param newPart the newPart to add
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * @param newProduct the newProduct to add
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * @param partId the partId to search
     * @return the part matching the partID
     */
    public static Part lookupPart(int partId) { return allParts.get(partId); }

    /**
     * @param partName the partName to search
     * @return the parts matching the partName
     */
    public static ObservableList<Part> lookupPart(String partName) { return allParts.filtered(Part ->
            Part.getName().contains(partName)); }

    /**
     * @param productId the productId to search
     * @return the product matching the productId
     */
    public static Product lookupProduct(int productId) { return allProducts.get(productId); }

    /**
     * @param productName the productName to search
     * @return the products matching the productName
     */
    public static ObservableList<Product> lookupProduct(String productName) { return allProducts.filtered(Product ->
            Product.getName().contains(productName)); }

    /**
     * @param index the index of part to update
     * @param selectedPart the new Part to set
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * @param index the index of Product to update
     * @param newProduct the new Product to set
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * @param selectedPart the part to delete
     * @return true or false depending on success
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * @param selectedProduct the product to delete
     * @return true or false depending on success
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * @return allParts list
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * @return allProducts list
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
