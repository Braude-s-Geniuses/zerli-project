package client;

import common.Message;
import common.MessageFromClient;
import common.MessageFromServer;
import common.Order;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Timestamp;
import java.util.ArrayList;

/** Client represents the Client Controller which manages <code>ZerliClient</code>,
 * the controller is used by the GUI to communicate with the server.
 */
public class ClientController {

    /** represents the only (static) instance of <code>AbstractClient</code>.
     *
     */
    private static ZerliClient client;

    /** represents the constructor of ClientController.
     *
     * @param host the server address which the <code>ZerliClient</code> connects to.
     */
    public ClientController(String host) {
        client = new ZerliClient(host, 5555);
    }

    /** The method requests from <code>ZerliServer</code> to fetch the entire orders table.
     *  The <code>ZerliClient</code> waits till the <code>ZerliServer</code> responds.
     *  The result is sent back to <code>clientgui.ViewOrdersTableController</code>.
     *
      * @return <code>ArrayList</code> of <code>Order</code>.
     */
    public ArrayList<Order> requestOrders() {
        Message message = new Message(null, MessageFromClient.REQUEST_ORDERS_TABLE);
        Client.clientController.getClient().handleMessageFromUI(message, true);

        return Client.clientController.getClient().getOrders();
    }

    /** The method requests from <code>ZerliServer</code> to update an <code>Order</code>
     *  The <code>ZerliClient</code> waits till the <code>ZerliServer</code> responds.
     *  The result is sent back to <code>clientgui.ViewOrdersTableController</code>.
     *
     * @param orderNumber
     * @param date new <code>Order.deliveryDate</code>; equals to null - not to update this field.
     * @param time new <code>Order.deliveryDate</code>; equals to null - not to update this field (part of deliveryDate).
     * @param color new <code>Order.color</code>; equals to null - not to update this field.
     *
     * @return <code>true</code> if the update was successful; <code>false</code> otherwise.
     *
     * @note Either date & time or color must be not null to perform an update.
     */
    public boolean updateOrder(int orderNumber, String date, String time, String color) {
        Message result = null;

        if(date != null && time != null) {
            Timestamp timestamp = Timestamp.valueOf(date + " " + time + ":00");
            Message message = new Message(new Order(orderNumber, 0.0, null, null, null, null, timestamp, null), MessageFromClient.UPDATE_DATE);
            Client.clientController.getClient().handleMessageFromUI(message, true);
        }

        if(color != null) {
            Message message = new Message(new Order(orderNumber, 0.0, null, color, null, null, null, null), MessageFromClient.UPDATE_COLOR);
            Client.clientController.getClient().handleMessageFromUI(message, true);
        }
        return Client.clientController.getClient().getMessage() == MessageFromServer.UPDATE_SUCCEED ? true : false;
    }

    /** Registers a new <code>EventHandler</code> of type <code>WindowEvent</code> that
     *  specifies to call <code>ZerliClient.quit()</code> once the Client closes the Application.
     *  The event is registered to a specified stage only.
     *
     * @param stage
     */
    public void attachExitEventToStage(Stage stage) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                getClient().quit(true);
            }
        });
    }

    /** Getter for <code>ZerliClient</code> instance.
     *
     * @return <code>client</code>
     */
    public ZerliClient getClient() {
        return client;
    }

}
