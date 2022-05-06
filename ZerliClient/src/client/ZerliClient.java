package client;

import communication.Message;
import communication.MessageFromClient;
import communication.MessageFromServer;
import order.Order;
import ocsf.client.AbstractClient;
import order.Product;
import user.Customer;
import user.User;
import user.UserType;

import java.util.ArrayList;

/** ZerliClient represents the implementation of <code>OCSF.AbstractClient</code>
 *
 */
public class ZerliClient extends AbstractClient {

    /**
     * Used to store orders fetched from the server once <code>ClientController.requestOrders()</code> is called.
     */
    private ArrayList<Order> orders = null;
    /**
     *  Used to store user fetched from the server once <code>loginClientController.tryToLogin()</code> is called.
     */
    private User loggedInUser = null;

    /**
     * Used to restore the response from the server to a <code>Message</code> sent by the <code>ZerliClient</code>.
     */
    private MessageFromServer message = null;

    /**
     * Used to perform busy-wait on client to server requests which require the client to wait for a server response.
     */
    private boolean awaitResponse = false;

    /** Contructor used to establish a connection with the <code>ZerliServer</code>.
     *
     * @param host the server address which the <code>ZerliClient</code> connects to.
     * @param port the server port which the <code>ZerliClient</code> connects to (defaults to 5555).
     */
    public ZerliClient(String host, int port) {
        super(host, port);
    }

    /** The method is triggered by <code>AbstractClient</code> once the <code>ZerliServer</code>
     * sends a <code>Message</code> to the client.
     * The client parses the message according to <code>MessageFromServer.enum</code>.
     *
     * @param msg the message received.
     */
    @Override
    protected void handleMessageFromServer(Object msg) {
        awaitResponse = false;
        Message messageFromServer = (Message) msg;

        switch (messageFromServer.getAnswer()) {
            case IMPORT_ORDERS_TABLE_SUCCEED:
                orders = (ArrayList<Order>) messageFromServer.getData();
                message = MessageFromServer.IMPORT_ORDERS_TABLE_NOT_SUCCEED;
                break;
            case IMPORT_ORDERS_TABLE_NOT_SUCCEED:
                orders = null;
                message = MessageFromServer.IMPORT_ORDERS_TABLE_NOT_SUCCEED;
                break;
            case UPDATE_SUCCEED:
                message = MessageFromServer.UPDATE_SUCCEED;
                break;
            case UPDATE_NOT_SUCCEED:
                message = MessageFromServer.UPDATE_NOT_SUCCEED;
                break;
            case LOGIN_NOT_SUCCEED:
                loggedInUser = null;
                message = MessageFromServer.LOGIN_NOT_SUCCEED;
                break;
            case LOGIN_SUCCEED:
                loggedInUser = (User)messageFromServer.getData();
                message = MessageFromServer.LOGIN_SUCCEED;
                break;
            case LOGIN_NOT_REGISTERED:
                loggedInUser = (User)messageFromServer.getData();
                message = MessageFromServer.LOGIN_NOT_REGISTERED;
                break;
            case ALREADY_LOGGED_IN:
                loggedInUser = (User)messageFromServer.getData();
                message = MessageFromServer.ALREADY_LOGGED_IN;
                break;
            case LOGOUT_SUCCEED:
                loggedInUser = null;
                message = MessageFromServer.LOGOUT_SUCCEED;
                break;
            case LOGOUT_NOT_SUCCEED:
                loggedInUser = null;
                message = MessageFromServer.LOGOUT_NOT_SUCCEED;
            case IMPORTED_PRODUCTS_SUCCEED:
                Client.catalogController.setProducts((ArrayList<Product>) messageFromServer.getData());
                break;
            case SEND_ORDER_CATALOG:
                Client.catalogController.receivedFromCatalog(messageFromServer);
                break;
            case IMPORT_BRANCHES_SUCCEDD:
                Client.orderController.setResponse((Message) msg);
                break;
        }

    }

    /** The method is triggered by <code>clientgui</code> Controllers once the client does
     * an action requiring sending a message to <code>ZerliServer</code>.
     *
     * @param message the message to send.
     * @param await <code>true</code> the clients waits for server response; <code>false</code> client continues without wait.
     */
    public void handleMessageFromUI(Object message, boolean await) {
        try {
            if (await)
                awaitResponse = true;
            sendToServer(message);
            while (awaitResponse) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            quit(false);
        }
    }

    /**
     *  Getter for <code>users</code>.
     *
     * @return <code>loggedInUser</code>.
     */
    public User getUser() {
        return loggedInUser;
    }

    /** Getter for <code>message</code>
     *
     * @return <code>message</code>.
     */
    public MessageFromServer getMessage() {
        return message;
    }

    /** The method is used to disconnect the client and close the application safely.
     *
     * @param notifyServer <code>true</code> the client notifies the <code>ZerliServer</code> of his disconnection;
     * <code>false</code> the server is not notified.
     */
    public void quit(boolean notifyServer) {
        try {
            if(notifyServer) {
                Message message = new Message(null, MessageFromClient.DISCONNECT_CLIENT);
                handleMessageFromUI(message, false);
            }
            closeConnection();
        } catch (Exception e) {
            System.exit(0);
        }
    }



}
