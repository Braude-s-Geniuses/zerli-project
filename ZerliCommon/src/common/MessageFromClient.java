package common;

import java.io.Serializable;

/** Represents an enumeration of messages that can be sent by the Client to Server.
 *
 */
public enum MessageFromClient implements Serializable {
	DISCONNECT_CLIENT,
	UPDATE_COLOR,
	UPDATE_DATE,
	REQUEST_ORDERS_TABLE	
}
