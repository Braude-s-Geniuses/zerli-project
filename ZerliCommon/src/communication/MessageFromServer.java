package communication;

import java.io.Serializable;

/** Represents an enumeration of messages that can be sent by the Server to Client.
 *
 */
public enum MessageFromServer implements Serializable {
    UPDATE_NOT_SUCCEED,
    IMPORT_ORDERS_TABLE_NOT_SUCCEED,
    IMPORT_ORDERS_TABLE_SUCCEED,
    LOGIN_NOT_REGISTERED,
    LOGIN_NOT_SUCCEED,
    LOGIN_SUCCEED,
    LOGOUT_NOT_SUCCEED,
    LOGOUT_SUCCEED,
    ALREADY_LOGGED_IN,
    SUCCESSFULLY_DISCONNECTED,
    UPDATE_SUCCEED,
    IMPORTED_PRODUCTS_SUCCEED,
    SEND_ORDER_CATALOG,
    IMPORT_BRANCHES_NOT_SUCCEDD, 
    IMPORT_BRANCHES_SUCCEDD,
    ADDED_ORDER_SUCCESSFULLY,
    ADDED_ORDER_NOT_SUCCESSFULLY

}
