package server;

import common.Message;

public class ServerController {

    private static ZerliServer server;

    public ServerController() {
        server = new ZerliServer(Server.DEFAULT_PORT);
    }

    public Message getAllOrders() {
        return Server.databaseController.getInstance().getAllOrders();
    }

    public Message updateOrder(Message messageFromClient) {
        return Server.databaseController.getInstance().updateOrder(messageFromClient);
    }

    public static ZerliServer getServer() {
        return server;
    }
}
