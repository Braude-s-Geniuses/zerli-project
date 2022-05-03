package client;

import communication.Message;
import communication.MessageFromClient;
import order.Product;

import java.util.ArrayList;

public class CatalogController {

    public ArrayList<Product> list = new ArrayList<>();


    public void getProducts() {


        Client.clientController.getClient().handleMessageFromUI(new Message(null, MessageFromClient.GET_PRODUCT), true);

    }

    public void setProducts(ArrayList<Product> list) {

        this.list = list;

    }

    public ArrayList<Product> getList() {
        return list;
    }

}
