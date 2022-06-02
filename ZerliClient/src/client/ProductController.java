package client;

import communication.Message;
import communication.MessageFromClient;
import javafx.scene.image.Image;
import order.Product;

import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * This controller handles all CRUD functions relating to a Product
 */
public class ProductController extends AbstractController {

    /**
     * stores the image for every product in order to prevent
     * duplicates of images on every screen change
     * key: product_id
     * value: image
     */
    private HashMap<Integer, Image> productImages = new HashMap<>();

    /**
     * Adds a new Product to the system
     * @param product - the new Product to be added
     * @return returned result is retrieved through the controller's <code>getResponse()</code>> method
     */
    public void addProduct(Product product) {
        Message message = new Message(product, MessageFromClient.PRODUCT_ADD);
        Client.clientController.getClient().handleMessageFromUI(message, true);
    }

    /**
     * Gets all existing Products from the server
     * @return returned result is retrieved through the controller's <code>getResponse()</code>> method
     */
    public void getProducts() {
        Message message = new Message(null, MessageFromClient.PRODUCTS_GET);
        Client.clientController.getClient().handleMessageFromUI(message, true);
    }

    /**
     * Gets all the Items of a given Product
     * @param productId - the Id of the Product
     * @return returned result is retrieved through the controller's <code>getResponse()</code>> method
     */
    public void getProductItems(int productId) {
        Message message = new Message(productId, MessageFromClient.PRODUCT_GET_ITEMS);
        Client.clientController.getClient().handleMessageFromUI(message, true);
    }

    /**
     * Updates an existing Product in the system
     * @param product - the updated Product with already updated fields
     * @return returned result is retrieved through the controller's <code>getResponse()</code>> method
     */
    public void updateProduct(Product product) {
        Message message = new Message(product, MessageFromClient.PRODUCT_UPDATE);
        Client.clientController.getClient().handleMessageFromUI(message, true);
    }

    /**
     * Creates a local Image (%temp%) of a product in the client's OS and adds its instance to <code>productImages</code>
     * so it can be used later
     * @param product - the product an image is needed to be created
     * @note the prefix of an image is: "zerli-product-{product_id}-hash.png"
     */
    public void createProductImage(Product product) {
        File fileImage = null;

        if(!productImages.containsKey(product.getProductId())) {
            try {
                fileImage = File.createTempFile("zerli-product-" + product.getProductId() + "-", "");
                fileImage.deleteOnExit();
                FileOutputStream fileOutputStream = new FileOutputStream(fileImage);
                byte[] b = product.getImage().getBytes(1L, (int) product.getImage().length());

                fileOutputStream.write(b);
                fileOutputStream.close();

                Image image = new Image(fileImage.toURI().toString());
                productImages.put(product.getProductId(), image);
            } catch (SerialException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * getter for <code>productImages</code>
     * @return <code>productImages</code>
     */
    public HashMap<Integer, Image> getProductImages() {
        return productImages;
    }

}