package common;

import java.io.Serializable;

/** Represents an enumeration of messages that can be sent by the Server to Client.
 *
 */
public enum MessageFromServer implements Serializable {
	IMPORT_ORDERS_TABLE_SUCCEED,
	IMPORT_ORDERS_TABLE_NOT_SUCCEED,
	UPDATE_SUCCEED,
	UPDATE_NOT_SUCCEED   	
}
