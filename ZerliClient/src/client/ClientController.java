package client;

import communication.Message;
import communication.MessageFromClient;
import communication.MessageFromServer;
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
    public ZerliClient getClient() { return client; }

}
