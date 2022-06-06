package communication;

import java.io.Serializable;

/** Represents an enumeration of messages that can be sent by the Client to Server.
 *
 */
public enum MessageFromClient implements Serializable {
    
    /* Client * UserController */
    DISCONNECT_CLIENT,
    LOGIN_REQUEST,
    LOGOUT_REQUEST,
    EMPLOYEE_PERMISSION_CHANGE,
    USER_INFORMATION_GET,
    CUSTOMER_CREATE_NEW,
    CUSTOMER_FREEZE,

    /* OrderController */
    ORDERS_GET,
    ORDER_BRANCHES_GET,
    ORDER_PRODUCTS_GET,
    ORDER_CREATE_NEW,
    ORDER_CHANGE_STATUS,
    ORDER_CANCEL_TIME,
    ORDER_GET_BALANCE,
    ORDER_GET_BRANCH,
    CUSTOMER_BALANCE_UPDATE,
    CUSTOMER_CREDIT_CARD_UPDATE,
    CUSTOMER_UPDATE_NEW,

    /* ItemController */
    ITEM_ADD,
    ITEMS_GET,
    ITEM_UPDATE,
    ITEM_DELETE,

    /* ProductController */
    PRODUCT_ADD,
    PRODUCTS_GET,
    PRODUCT_UPDATE,
    PRODUCT_DELETE,
    PRODUCT_GET_ITEMS,

    /* CatalogController */
    CATALOG_GET_PRODUCT_ITEMS,

    /* SurveyController */
    SURVEY_IDS_REQUEST,
    SURVEY_SEND,
    SURVEY_NAMES_GET,
    SURVEY_ANSWERS_GET,
    SURVEY_UPLOAD_SUMMARY,
    SURVEY_ID_GET,

    /* ComplaintController */
    COMPLAINT_ADD_NEW,
    COMPLAINT_VALIDATE_CUSTOMER_ORDER,
    COMPLAINTS_GET,
    COMPLAINT_CLOSE_UPDATE,

    /* ReportController */
    REPORT_ORDER_VIEW,
    REPORT_REVENUE_VIEW,
    REPORT_COMPLAINT_VIEW,
    MANAGER_BRANCH_GET,

    /* DeliveryController */
    DELIVERIES_GET,
    DELIVERY_ADD_NEW,
    DELIVERY_HISTORY_GET,
    DELIVERY_ORDER_REFUND,

    ORDERS_GET_BY_BRANCH,
    CUSTOMER_GET_EMAIL,
    CATALOG_PRODUCTS_GET, GET_USER_PERMISSION,

}
