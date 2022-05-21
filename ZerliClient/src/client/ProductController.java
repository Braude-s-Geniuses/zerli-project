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

public class ProductController {
    private Message response;

    private HashMap<Integer, Image> productImages = new HashMap<>();

    public void addProduct(Product product) {
        Message message = new Message(product, MessageFromClient.PRODUCT_ADD);
        Client.clientController.getClient().handleMessageFromUI(message, true);
    }

    public void getProducts() {
        Message message = new Message(null, MessageFromClient.PRODUCTS_GET);
        Client.clientController.getClient().handleMessageFromUI(message, true);
    }

    public void getProductItems(int productId) {
        Message message = new Message(productId, MessageFromClient.PRODUCT_GET_ITEMS);
        Client.clientController.getClient().handleMessageFromUI(message, true);
    }

    public void updateProduct(Product product) {
        Message message = new Message(product, MessageFromClient.PRODUCT_UPDATE);
        Client.clientController.getClient().handleMessageFromUI(message, true);
    }

    public void deleteProduct(Product product) {
        Message message = new Message(product, MessageFromClient.PRODUCT_DELETE);
        Client.clientController.getClient().handleMessageFromUI(message, true);
    }

    public void createProductImage(Product product) {
        File fileImage = null;

        if(!productImages.containsKey(product.getProductId())) {
            try {
                fileImage = File.createTempFile("zerli-product-" + product.getProductId() + "-", "");
                fileImage.deleteOnExit();
                FileOutputStream fileOutputStream = new FileOutputStream(fileImage);
                byte b[] = product.getImage().getBytes(1L, (int) product.getImage().length());

                fileOutputStream.write(b);
                fileOutputStream.close();

                Image image = new Image(fileImage.toURI().toString());
                productImages.put(product.getProductId(), image);
            } catch (SerialException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Message getResponse() {
        return response;
    }

    public void setResponse(Message response) {
        this.response = response;
    }

    public HashMap<Integer, Image> getProductImages() {
        return productImages;
    }

    public void setProductImages(HashMap<Integer, Image> productImages) {
        this.productImages = productImages;
    }
}