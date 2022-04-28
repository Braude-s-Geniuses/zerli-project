package server;

import common.Message;
import javafx.stage.Stage;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import servergui.ServerUIController;

import java.io.IOException;


/** ZerliClient represents the implementation of <code>OCSF.AbstractServer</code>
 *
 */
public class ZerliServer extends AbstractServer {

    /** The constructor initializes the server.
     *
     * @param port on which the <code>ZerliServer</code> should listen to.
     */
    public ZerliServer(int port) {
        super(port);
    }

    protected void serverStarted() {
        System.out.println("Server listening for connections on port " + getPort());
    }

    protected void serverStopped() {
        try {
            this.close();
            System.out.println("Server has stopped listening for connections.");
        } catch (IOException e) {}
    }

    /** The method is triggered by <code>AbstractServer</code> once the <code>ZerliClient</code>
     * sends a <code>Message</code> to the server.
     * The server parses the message according to <code>MessageFromClient.enum</code>.
     * Once the message is parsed and if required - the server calls the <code>DatabaseController</code> instance
     * to fetch the requested data and then the server sends it back to the client.
     *
     * @param message   the message sent.
     * @param client the connection connected to the client that
     *  sent the message.
     */
    @Override
    public void handleMessageFromClient(Object message, ConnectionToClient client) {
        Message messageFromClient = (Message) message;
        Message messageFromServer = null;

        switch (messageFromClient.getTask()) {
            case DISCONNECT_CLIENT:
                Server.serverUIController.removeClientFromTable(client);
                break;
            case REQUEST_ORDERS_TABLE:
                messageFromServer = Server.serverController.getAllOrders();
                break;
            case UPDATE_COLOR:
            case UPDATE_DATE:
                messageFromServer = Server.serverController.updateOrder(messageFromClient);
                break;
            default:
                break;
        }
        try {
            if(messageFromServer != null)
                client.sendToClient(messageFromServer);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    protected void clientConnected(ConnectionToClient client) {
        Server.serverUIController.addClientToTable(client);
    }

}
