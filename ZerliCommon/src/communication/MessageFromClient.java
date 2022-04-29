package communication;

import java.io.Serializable;

/** Represents an enumeration of messages that can be sent by the Client to Server.
 *
 */
public enum MessageFromClient implements Serializable {
    DISCONNECT_CLIENT,
    LOGIN_REQUEST
}
