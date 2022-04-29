package server;

import communication.Message;

public class ServerController {

    private static ZerliServer server;

    public ServerController() {
        server = new ZerliServer(Server.DEFAULT_PORT);
    }

    public static ZerliServer getServer() {
        return server;
    }
}
