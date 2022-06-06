package client;

import complaint.Complaint;
import communication.Message;
import communication.MessageFromClient;
import communication.MessageFromServer;
import ocsf.client.AbstractClient;
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
            case LOGIN_SUCCESS:
            case LOGIN_FAIL:
            case ALREADY_LOGGED_IN:
            case LOGOUT_SUCCESS:
            case LOGOUT_FAIL:
            case CUSTOMER_IS_BLOCKED:
                Client.userController.setLoggedInUser((User)messageFromServer.getData());
                Client.userController.getService().setResponse(messageFromServer);
                break;
            case USER_INFORMATION_GET_SUCCESS:
            case GET_USER_PERMISSION_SUCCEED:
                Client.userController.setUserTypeForInformation(messageFromServer.getData());
                Client.userController.getService().setResponse(messageFromServer);
                break;
            case USER_INFORMATION_GET_FAIL:
            case GET_USER_PERMISSION_NOT_SUCCEED:
                Client.userController.setUserTypeForInformation(null);
                Client.userController.getService().setResponse(null);
                break;
            case CUSTOMER_CREATE_NEW:
            case CUSTOMER_FREEZE:
            case EMPLOYEE_PERMISSION_CHANGE:
            case CUSTOMER_GET_EMAIL_SUCCESS:
            case CUSTOMER_GET_EMAIL_FAIL:
                Client.userController.getService().setResponse(messageFromServer);
                break;
            case ORDERS_GET_SUCCESS:
            case ORDERS_GET_FAIL:
            case ORDERS_GET_BY_BRANCH_SUCCESS:
            case ORDERS_GET_BY_BRANCH_FAIL:
            case ORDER_BRANCHES_GET_SUCCESS:
            case ORDER_CREATE_NEW_SUCCESS:
            case ORDER_CREATE_NEW_FAIL:
            case ORDER_PRODUCTS_GET_SUCCESS:
            case ORDER_GET_BRANCH_FAIL:
            case ORDER_GET_BRANCH_SUCCESS:
            case ORDER_GET_BALANCE_SUCCESS:
            case ORDER_STATUS_SUCCESS:
            case ORDER_STATUS_FAIL:
            case ORDER_CANCEL_TIME_SUCCESS:
            case ORDER_CANCEL_TIME_FAIL:
                Client.orderController.getService().setResponse((Message) msg);
                break;
            case ITEM_ADD_SUCCESS:
            case ITEM_ADD_FAIL:
            case ITEMS_GET_SUCCESS:
            case ITEMS_GET_FAIL:
            case ITEM_UPDATE_SUCCESS:
            case ITEM_UPDATE_FAIL:
            case ITEM_DELETE_SUCCESS:
            case ITEM_DELETE_FAIL:
                Client.itemController.getService().setResponse(messageFromServer);
                break;
            case PRODUCT_ADD_SUCCESS:
            case PRODUCT_ADD_FAIL:
            case PRODUCTS_GET_SUCCESS:
            case PRODUCTS_GET_FAIL:
            case PRODUCT_UPDATE_SUCCESS:
            case PRODUCT_UPDATE_FAIL:
            case PRODUCT_GET_ITEMS_SUCCEED:
            case PRODUCT_GET_ITEMS_FAIL:
                Client.productController.getService().setResponse(messageFromServer);
                break;
            case CATALOG_GET_PRODUCT_ITEMS_FAIL:
            case CATALOG_GET_PRODUCT_ITEMS_SUCCESS:
            case CATALOG_PRODUCTS_GET_SUCCESS:
            case CATALOG_PRODUCTS_GET_FAIL:
                Client.catalogController.getService().setResponse(messageFromServer);
                break;
            case REPORT_VIEW_SUCCESS:
            case REPORT_VIEW_FAIL:
            case MANAGER_BRANCH_GET_SUCCESS:
                Client.reportController.getService().setResponse((Message) msg);
                break;
            case DELIVERIES_GET_FAIL:
            case DELIVERIES_GET_SUCCESS:
            case DELIVERY_UPDATE_FAIL:
            case DELIVERY_UPDATE_SUCCESS:
            case DELIVERY_ORDER_REFUND_SUCCESS:
            case DELIVERY_ORDER_REFUND_FAIL:
                Client.deliveryController.getService().setResponse(messageFromServer);
                break;
            case SURVEY_INSERT_FAIL:
            case SURVEY_INSERT_SUCCESS:
            case SURVEY_UNAUTHORIZED_CUSTOMER:
            case SURVEY_ALREADY_FILLED:
                Client.surveyController.getService().setResponse(messageFromServer);
                break;
            case SURVEY_NAMES_SUCCESS:
                Client.surveyController.setSurveyNames((ArrayList<String>) messageFromServer.getData());
                break;
            case SURVEY_ANSWERS_SUCCESS:
                Client.surveyController.setSurveyAnswersList((List<SurveyAnswers>) messageFromServer.getData());
                break;
            case SURVEY_UPLOAD_SUMMARY_SUCCESS:
                Client.surveyController.setUploadStatus(true);
                break;
            case SURVEY_ID_GET_SUCCESS:
                Client.surveyController.setSurveyID((int)messageFromServer.getData());
                break;
            case SURVEY_IDS_REQUEST_SUCCESS:
                Client.surveyController.setCurrSurveys((ArrayList<List>)messageFromServer.getData());
                message = MessageFromServer.SURVEY_IDS_REQUEST_SUCCESS;
                break;
            case SURVEY_IDS_CUSTOMER_SUCCESS:
                Client.surveyController.setCurrCustomers((ArrayList<Integer>)messageFromServer.getData());
                message = MessageFromServer.SURVEY_IDS_CUSTOMER_SUCCESS;
                break;
            case SURVEY_IDS_REQUEST_FAIL:
                message = MessageFromServer.SURVEY_IDS_REQUEST_FAIL;
                break;
            case COMPLAINT_RESPONSE:
                Client.complaintController.setComplaintStatusReceived(messageFromServer);
                break;
            case COMPLAINT_VALIDATE_RESPONSE:
                Client.complaintController.setValidationResult(messageFromServer);
                break;
            case COMPLAINTS_GET_SUCCESS:
                Client.complaintController.setAllComplaints((ArrayList<Complaint>) messageFromServer.getData());
                break;
            case COMPLAINT_CLOSE_SUCCESS:
                Client.complaintController.setStatusClosed((String)messageFromServer.getData());
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