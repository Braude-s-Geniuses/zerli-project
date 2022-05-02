package communication;

import java.io.Serializable;

/** Represents an enumeration of messages that can be sent by the Server to Client.
 *
 */
public enum MessageFromServer implements Serializable {
    UPDATE_NOT_SUCCEED,
    IMPORT_ORDERS_TABLE_NOT_SUCCEED,
    IMPORT_ORDERS_TABLE_SUCCEED,

    LOGIN_NOT_SUCCEED,

    LOGIN_NOT_REGISTERED,

    LOGIN_SUCCEED,
    UPDATE_SUCCEED
}
