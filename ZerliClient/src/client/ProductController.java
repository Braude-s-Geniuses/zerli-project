package client;

import communication.Message;
import communication.MessageFromClient;
import order.Product;

public class ProductController {
    private Message response;

    public void addProduct(Product product) {
        Message message = new Message(product, MessageFromClient.PRODUCT_ADD);
        Client.clientController.getClient().handleMessageFromUI(message, true);
    }

    public void getProducts() {
        Message message = new Message(null, MessageFromClient.PRODUCTS_GET);
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

    public Message getResponse() {
        return response;
    }

    public void setResponse(Message response) {
        this.response = response;
    }
}