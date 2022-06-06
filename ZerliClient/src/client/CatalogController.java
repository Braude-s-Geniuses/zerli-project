package client;

import communication.Message;
import communication.MessageFromClient;
import product.Product;

import java.util.ArrayList;

/**
 * Controller for everything related to Catalog
 */
public class CatalogController extends AbstractController {

    public ArrayList<Product> list = new ArrayList<>();
    public Product createProduct;

    CatalogController(IMessageService service) {
        super(service);
    }

    /**
     * Get all products from server to be showed in the catalog, sorted by discounts in the catalog to be shown first
     */
    public void getProducts() {
        Message message = new Message(null, MessageFromClient.CATALOG_PRODUCTS_GET);
        getService().sendToServer(message, true);
    }

    /**
     * Gets from server the items connected to given productID.
     * @param productId
     */
    public void getProductItems(int productId) {
        Message message = new Message(productId, MessageFromClient.CATALOG_GET_PRODUCT_ITEMS);
        getService().sendToServer(message, true);
    }

}
