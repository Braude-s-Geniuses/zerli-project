package client;

import communication.Message;
import communication.MessageFromClient;
import order.Item;

/**
 * This controller handles all CRUD functions relating to an Item
 */
public class ItemController extends AbstractController {

    ItemController(IMessageService service) {
        super(service);
    }

    /**
     * Adds a new item to the system
     * @param item - the new Item to be added
     * @return returned result is retrieved through the controller's <code>getResponse()</code>> method
     */
    public void addItem(Item item) {
        Message message = new Message(item, MessageFromClient.ITEM_ADD);
        getService().sendToServer(message, true);
    }

    /**
     * Gets all existing Items from the server
     * @return returned result is retrieved through the controller's <code>getResponse()</code>> method
     */
    public void getItems() {
        Message message = new Message(null, MessageFromClient.ITEMS_GET);
        getService().sendToServer(message, true);
    }

    /**
     * Updates an existing Item in the system
     * @param item - the updated Item with already updated fields
     * @return returned result is retrieved through the controller's <code>getResponse()</code>> method
     */
    public void updateItem(Item item) {
        Message message = new Message(item, MessageFromClient.ITEM_UPDATE);
        getService().sendToServer(message, true);
    }

    /**
     * Deletes an item from the system
     * @return returned result is retrieved through the controller's <code>getResponse()</code>> method
     * @param item - the item to be deleted
     * @note If <code>item</code>       belongs to a Product, an <code>SQLException</code>    will be thrown server-side
     * and the item will not be deleted
     */
    public void deleteItem(Item item) {
        Message message = new Message(item, MessageFromClient.ITEM_DELETE);
        getService().sendToServer(message, true);
    }

}