package communication;

import java.io.Serializable;

/** Represents an enumeration of messages that can be sent by the Client to Server.
 *
 */
public enum MessageFromClient implements Serializable {
    DISCONNECT_CLIENT,
    LOGIN_REQUEST,
    LOGOUT_REQUEST,
    GET_PRODUCT,
    REQUEST_ORDERS_TABLE,
    REQUEST_BRANCHES,
    ADD_NEW_ORDER,
    ITEM_ADD,
    ITEMS_GET,
    ITEM_UPDATE,
    ITEM_DELETE,
    PRODUCT_ADD,
    PRODUCTS_GET,
    PRODUCT_UPDATE,
    PRODUCT_DELETE,
    PRODUCT_GET_ITEMS,
    REQUEST_ORDER_PRODUCTS,
    UPDATE_BALANCE_FOR_CUSTOMER,
    UPDATE_CARD_FOR_CUSTOMER,
    SURVEY_IDS_REQUEST,
    SURVEY_SEND,
    REQUEST_ALL_SURVEY_NAMES_BY_EXPERT,
    REQUEST_ALL_SURVEY_ANSWERS,
    REQUEST_UPLOAD_SURVEY_SUMMARY,
    REQUEST_SURVEY_ID,
    ADD_NEW_COMPLAINT,
    VALIDATE_CUSTOMER_AND_ORDER,
    REQUEST_COMPLAINTS_TABLE,
    CLOSE_STATUS, CATALOG_GET_PRODUCT_ITEMS, VIEW_ORDER_REPORT, VIEW_REVENUE_REPORT, VIEW_COMPLAINTS_REPORT, UPDATE_NEW_CUSTOMER, REQUEST_DELIVERIES_TABLE, SEND_DELIVERY, REQUEST_DELIVERIES_HISTORY_TABLE, CHANGE_PERMISSION, GET_USER_INFORMATION, CREATE_NEW_CUSTOMER, FREEZE_CUSTOMER, ORDER_CHANGE_STATUS, ORDER_CANCEL_TIME, ORDER_GET_BALANCE, ORDER_GET_BRANCH, REFUND_ORDER,
}
