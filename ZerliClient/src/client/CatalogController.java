package client;

import communication.Message;
import communication.MessageFromClient;
import order.Product;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Controller for the catalog
 */
public class CatalogController extends AbstractController {

    public ArrayList<Product> list = new ArrayList<>();
    public Product createProduct;

    /**
     * Get all products from server to be showed in the catalog
     */
    public void getProducts() {
        Client.productController.getProducts();
    }

    /**
     * Gets from server the items connected to given productID.
     * @param productId
     */
    public void getProductItems(int productId) {
        Message message = new Message(productId, MessageFromClient.CATALOG_GET_PRODUCT_ITEMS);
        Client.clientController.getClient().handleMessageFromUI(message, true);
    }

}
