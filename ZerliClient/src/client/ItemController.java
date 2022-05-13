package client;

import communication.Message;
import communication.MessageFromClient;
import order.Item;

public class ItemController {
    private Message response;

    public void addItem(Item item) {
        Message message = new Message(item, MessageFromClient.ITEM_ADD);
        Client.clientController.getClient().handleMessageFromUI(message, true);
    }

    public void getItems() {
        Message message = new Message(null, MessageFromClient.ITEMS_GET);
        Client.clientController.getClient().handleMessageFromUI(message, true);
    }

    public void updateItem(Item item) {
        Message message = new Message(item, MessageFromClient.ITEM_UPDATE);
        Client.clientController.getClient().handleMessageFromUI(message, true);
    }

    public void deleteItem(Item item) {
        Message message = new Message(item, MessageFromClient.ITEM_DELETE);
        Client.clientController.getClient().handleMessageFromUI(message, true);
    }

    public Message getResponse() {
        return response;
    }

    public void setResponse(Message response) {
        this.response = response;
    }
}