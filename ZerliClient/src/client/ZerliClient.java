package client;

import branch.Complaint;
import communication.Message;
import communication.MessageFromClient;
import communication.MessageFromServer;
import ocsf.client.AbstractClient;
import order.Product;
import survey.SurveyAnswers;
import user.User;

import java.util.ArrayList;
import java.util.List;

/** ZerliClient represents the implementation of <code>OCSF.AbstractClient</code>
 *
 */
public class ZerliClient extends AbstractClient {

    /**
     * Used to restore the response from the server to a <code>Message</code> sent by the <code>ZerliClient</code>.
     */
    private MessageFromServer message = null;

    /**
     * Used to perform busy-wait on client to server requests which require the client to wait for a server response.
     */
    private boolean awaitResponse = false;

    /** Contructor used to establish a connection with the <code>ZerliServer</code>.
     *
     * @param host the server address which the <code>ZerliClient</code> connects to.
     * @param port the server port which the <code>ZerliClient</code> connects to (defaults to 5555).
     */
    public ZerliClient(String host, int port) {
        super(host, port);
    }

    /** The method is triggered by <code>AbstractClient</code> once the <code>ZerliServer</code>
     * sends a <code>Message</code> to the client.
     * The client parses the message according to <code>MessageFromServer.enum</code>.
     *
     * @param msg the message received.
     */
    @Override
    protected void handleMessageFromServer(Object msg) {
        awaitResponse = false;
        Message messageFromServer = (Message) msg;

        switch (messageFromServer.getAnswer()) {
            case LOGIN_SUCCEED:
            case LOGIN_NOT_SUCCEED:
            case ALREADY_LOGGED_IN:
            case LOGOUT_SUCCEED:
            case LOGOUT_NOT_SUCCEED:
                Client.userController.setLoggedInUser((User)messageFromServer.getData());
                Client.userController.setResponse(messageFromServer);
                break;
            case GET_USER_INFORMATION_SUCCEED:
                Client.userController.setUserTypeForInformation(messageFromServer.getData());
                Client.userController.setResponse(messageFromServer);
                break;
            case GET_USER_INFORMATION_NOT_SUCCEED:
                Client.userController.setUserTypeForInformation(null);
                Client.userController.setResponse(null);
                break;
            case CREATE_NEW_CUSTOMER:
            case FREEZE_CUSTOMER:
            case CHANGE_PERMISSION:
                Client.userController.setResponse(messageFromServer);
                break;
            case IMPORTED_PRODUCTS_SUCCEED:
                Client.catalogController.setProducts((ArrayList<Product>) messageFromServer.getData());
                break;
            case CUSTOMER_IS_BLOCKED:
                Client.userController.setLoggedInUser(null);
                message = MessageFromServer.CUSTOMER_IS_BLOCKED;
                break;
            case IMPORT_ORDERS_TABLE_SUCCEED:
            case IMPORT_ORDERS_TABLE_NOT_SUCCEED:
            case IMPORT_BRANCHES_SUCCEDD:
            case ADDED_ORDER_SUCCESSFULLY:
            case ADDED_ORDER_NOT_SUCCESSFULLY:
            case ORDER_PRODUCTS_DELIVERED_SUCCESSFULLY:
                Client.orderController.setResponse((Message) msg);
                break;
            case ITEM_ADD_SUCCESS:
            case ITEM_ADD_FAIL:
            case ITEMS_GET_SUCCESS:
            case ITEMS_GET_FAIL:
            case ITEM_UPDATE_SUCCESS:
            case ITEM_UPDATE_FAIL:
            case ITEM_DELETE_SUCCESS:
            case ITEM_DELETE_FAIL:
                Client.itemController.setResponse(messageFromServer);
                break;
            case PRODUCT_ADD_SUCCESS:
            case PRODUCT_ADD_FAIL:
            case PRODUCTS_GET_SUCCESS:
            case PRODUCTS_GET_FAIL:
            case PRODUCT_UPDATE_SUCCESS:
            case PRODUCT_UPDATE_FAIL:
            case PRODUCT_DELETE_SUCCESS:
            case PRODUCT_DELETE_FAIL:
            case PRODUCT_GET_ITEMS_SUCCEED:
            case PRODUCT_GET_ITEMS_FAIL:
                Client.productController.setResponse(messageFromServer);
                break;
            case SURVEY_NAMES_SUCCESSFULLY:
                Client.surveyController.setSurveyNames((ArrayList<String>) messageFromServer.getData());
                break;
            case SURVEY_ANSWERS_SUCCESSFULLY:
                Client.surveyController.setSurveyAnswersList((List<SurveyAnswers>) messageFromServer.getData());
                break;
            case UPLOAD_SURVEY_SUMMARY_SUCCESSFULLY:
                Client.surveyController.setUploadStatus(true);
                break;
            case GET_SURVEY_ID_SUCCESSFULLY:
                Client.surveyController.setSurveyID((int)messageFromServer.getData());
                break;
            case SURVEY_IDS_SUCCESSFUL:
                Client.surveyController.setCurrSurveys((ArrayList<List>)messageFromServer.getData());
                message = MessageFromServer.SURVEY_IDS_SUCCESSFUL;
                break;
            case SURVEY_IDS_CUSTOMER_SUCCESSFUL:
                Client.surveyController.setCurrCustomers((ArrayList<Integer>)messageFromServer.getData());
                message = MessageFromServer.SURVEY_IDS_CUSTOMER_SUCCESSFUL;
                break;
            case SURVER_ALREADY_FILLED:
                message = MessageFromServer.SURVER_ALREADY_FILLED;
                break;
            case SURVEY_IDS_NOT_SUCCESSFULLY:
                message = MessageFromServer.SURVEY_IDS_NOT_SUCCESSFULLY;
                break;
            case SURVEY_HAS_BEEN_COMPLETED:
                message = MessageFromServer.SURVEY_HAS_BEEN_COMPLETED;
                break;
            case SURVER_INSERT_NOT_SUCCESSFULLY:
                message = MessageFromServer.SURVER_INSERT_NOT_SUCCESSFULLY;
                break;
            case SURVER_INSERT_SUCCEED:
                message = MessageFromServer.SURVER_INSERT_SUCCEED;
                break;
            case UNAUTHORIZED_CUSTOMER:
                message = MessageFromServer.UNAUTHORIZED_CUSTOMER;
                break;
            case ADDED_COMPLAINT_SUCCESSFULLY:
                Client.complaintController.setComplaintStatusReceived(messageFromServer);
                break;
            case RESULT_OF_VALIDATION:
                Client.complaintController.setValidationResult(messageFromServer);
                break;
            case IMPORTED_COMPLAINTS_SUCCEED:
                Client.complaintController.setAllComplaints((ArrayList<Complaint>) messageFromServer.getData());
                break;
            case STATUS_CLOSED_SUCCESSFULLY:
                Client.complaintController.setStatusClosed((String)messageFromServer.getData());
                break;
            case CATALOG_GET_PRODUCT_ITEMS_FAIL:
            case CATALOG_GET_PRODUCT_ITEMS_SUCCEED:
                Client.catalogController.setResponse(messageFromServer);
                break;
            case ORDER_REPORT_IMPORTED_SUCCESSFULLY:
            case ORDER_REPORT_IMPORTED_NOT_SUCCESSFULLY:
                Client.reportController.setResponse((Message) msg);
                break;
            case IMPORT_DELIVERY_TABLE_NOT_SUCCEED:
            case IMPORT_DELIVERY_TABLE_SUCCEED:
            case DELIVERY_UPDATE_FAIL:
            case DELIVERY_UPDATE_SUCCESS:
                Client.deliveryController.setResponse((Message)messageFromServer);
                break;
        }

    }

    /** The method is triggered by <code>clientgui</code> Controllers once the client does
     * an action requiring sending a message to <code>ZerliServer</code>.
     *
     * @param message the message to send.
     * @param await <code>true</code> the clients waits for server response; <code>false</code> client continues without wait.
     */
    public void handleMessageFromUI(Object message, boolean await) {
        try {
            if (await)
                awaitResponse = true;
            sendToServer(message);
            while (awaitResponse) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Getter for <code>message</code>
     *
     * @return <code>message</code>.
     */
    public MessageFromServer getMessage() {
        return message;
    }

    /** The method is used to disconnect the client and close the application safely.
     *
     * @param notifyServer <code>true</code> the client notifies the <code>ZerliServer</code> of his disconnection;
     * <code>false</code> the server is not notified.
     */
    public void quit(boolean notifyServer) {
        try {
            if(notifyServer) {
                Message message = new Message(null, MessageFromClient.DISCONNECT_CLIENT);
                handleMessageFromUI(message, false);
            }
            closeConnection();
        } catch (Exception e) {
            System.exit(0);
        }
    }



}