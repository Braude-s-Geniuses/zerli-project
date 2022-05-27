package client;

import communication.Message;
import communication.MessageFromClient;
import order.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class CatalogController {

    public ArrayList<Product> list = new ArrayList<>();
    public Message response;

    public boolean isCreateProduct = false;
    private HashMap<Product, Integer> createProduct = new HashMap<>();

    public HashMap<Product, Integer> getCreateProduct() {
        return createProduct;
    }

    public void addProductToCustomProduct(Product product, int quantity) {
        int currentQuantity = 0;

        if(createProduct.containsKey(product))
            currentQuantity = createProduct.get(product);

        createProduct.put(product, currentQuantity + quantity);
    }

    public void getProducts() {
        Client.productController.getProducts();
        //Client.clientController.getClient().handleMessageFromUI(new Message(null, MessageFromClient.GET_PRODUCT), true);
    }

    public void setProducts(ArrayList<Product> list) {
        this.list = list;
    }

    public void getProductItems(int productId) {
        Message message = new Message(productId, MessageFromClient.CATALOG_GET_PRODUCT_ITEMS);
        Client.clientController.getClient().handleMessageFromUI(message, true);
    }

    public ArrayList<Product> getList() {
        return list;
    }

    public Message getResponse() {
        return response;
    }

    public void setResponse(Message response) {
        this.response = response;
    }
}
