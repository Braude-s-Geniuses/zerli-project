package server;

import complaint.Complaint;
import communication.Message;
import communication.MessageFromServer;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import order.Order;
import servergui.ServerUIController;
import survey.Survey;
import survey.SurveyAnswers;
import user.BranchEmployee;
import user.Customer;
import user.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/** ZerliClient represents the implementation of <code>OCSF.AbstractServer</code>
 *
 */
public class ZerliServer extends AbstractServer {

    /** The constructor initializes the server.
     *
     * @param port on which the <code>ZerliServer</code> should listen to.
     */
    public ZerliServer(int port) {
        super(port);
    }

    protected void serverStarted() {
        System.out.println("Server listening for connections on port " + getPort());
    }

    protected void serverStopped() {
        try {
            this.close();
            System.out.println("Server has stopped listening for connections.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** The method is triggered by <code>AbstractServer</code> once the <code>ZerliClient</code>
     * sends a <code>Message</code> to the server.
     * The server parses the message according to <code>MessageFromClient.enum</code>.
     * Once the message is parsed and if required - the server calls the <code>DatabaseController</code> instance
     * to fetch the requested data and then the server sends it back to the client.
     *
     * @param message   the message sent.
     * @param client the connection connected to the client that
     *  sent the message.
     */
    @Override
    public void handleMessageFromClient(Object message, ConnectionToClient client) {
        Message messageFromClient = (Message) message;
        Message messageFromServer = null;


        switch (messageFromClient.getTask()) {
            case DISCONNECT_CLIENT:
                ServerUIController.removeClientFromTable(client);
                ServerUIController.printToServerConsoleUI(Objects.requireNonNull(client.getInetAddress()).getHostAddress() + " (" + client.getInetAddress() + ") has disconnected");
                break;
            case LOGIN_REQUEST:
                UserController loginController= new UserController();
                messageFromServer = loginController.login((User)messageFromClient.getData());

                if(messageFromServer.getAnswer() == MessageFromServer.LOGIN_SUCCESS)
                    ServerUIController.setClientLoggedInTable(client, (User) messageFromServer.getData());
                break;
            case LOGOUT_REQUEST:
                loginController= new UserController();
                messageFromServer = loginController.logout((int)messageFromClient.getData());

                if(messageFromServer.getAnswer() == MessageFromServer.LOGOUT_SUCCESS)
                    ServerUIController.setClientLoggedOutTable(client);
                break;
            case ORDERS_GET:
                messageFromServer = OrderController.getAllOrdersFromServer((int) messageFromClient.getData());
                break;
            case ORDERS_GET_BY_BRANCH:
                messageFromServer = OrderController.getAllOrdersByBranch((String) messageFromClient.getData());
                break;
            case ORDER_BRANCHES_GET:
                messageFromServer = OrderController.getAllBranches();
                break;
            case ORDER_CREATE_NEW:
                messageFromServer = OrderController.AddNewOrder((Order) messageFromClient.getData());
                break;
            case ORDER_GET_BRANCH:
                messageFromServer = OrderController.getBranch((int)messageFromClient.getData());
                break;
            case ORDER_CHANGE_STATUS:
                messageFromServer = OrderController.UpdateOrderStatus((Order)messageFromClient.getData());
                break;
            case ORDER_CANCEL_TIME:
                messageFromServer = OrderController.UpdateOrderCancel((Order)messageFromClient.getData());
                break;
            case ORDER_GET_BALANCE:
                messageFromServer = OrderController.getBalance((int)messageFromClient.getData());
                break;
            case ORDER_PRODUCTS_GET:
                messageFromServer = OrderController.getOrderDetails((Integer)messageFromClient.getData());
                break;
            case CUSTOMER_BALANCE_UPDATE:
                OrderController.updateBalance((ArrayList<Object>)messageFromClient.getData());
                break;
            case CUSTOMER_CREDIT_CARD_UPDATE:
                OrderController.updateCreditCard((ArrayList<Object>)messageFromClient.getData());
                break;
            case CUSTOMER_UPDATE_NEW:
                OrderController.updateNewCustomer((Integer)messageFromClient.getData());
                break;
            case ITEM_ADD:
                messageFromServer = ItemController.addItem(messageFromClient);
                break;
            case ITEMS_GET:
                messageFromServer = ItemController.getAllItems();
                break;
            case ITEM_UPDATE:
                messageFromServer = ItemController.updateItem(messageFromClient);
                break;
            case ITEM_DELETE:
                messageFromServer = ItemController.deleteItem(messageFromClient);
                break;
            case PRODUCT_ADD:
                messageFromServer = ProductController.addProduct(messageFromClient);
                break;
            case PRODUCTS_GET:
                messageFromServer = ProductController.getAllProducts();
                break;
            case PRODUCT_GET_ITEMS:
                messageFromServer = ProductController.getProductItems(messageFromClient);
                break;
            case PRODUCT_UPDATE:
                messageFromServer = ProductController.updateProduct(messageFromClient);
                break;
            case SURVEY_IDS_REQUEST:
                SurveyController surveyServerController= new SurveyController();
                messageFromServer = surveyServerController.getSurveyIdsAndNames();
                break;
            case SURVEY_SEND:
                surveyServerController= new SurveyController();
                messageFromServer = surveyServerController.addSurveyAnswersByCustomer((SurveyAnswers)messageFromClient.getData());
                break;
            case SURVEY_NAMES_GET:
                surveyServerController= new SurveyController();
                messageFromServer = surveyServerController.tryToGetSurveyNames();
                break;
            case SURVEY_ANSWERS_GET:
                surveyServerController= new SurveyController();
                messageFromServer = surveyServerController.tryToGetSurveyAnswers((String)messageFromClient.getData());
                break;
            case SURVEY_UPLOAD_SUMMARY:
                surveyServerController= new SurveyController();
                messageFromServer = surveyServerController.tryToUploadFile((Survey)messageFromClient.getData());
                break;
            case SURVEY_ID_GET:
                surveyServerController= new SurveyController();
                messageFromServer = surveyServerController.tryToGetSurveyID((String)messageFromClient.getData());
                break;
            case CATALOG_PRODUCTS_GET:
                messageFromServer = CatalogController.getAllProducts();
                break;
            case CATALOG_GET_PRODUCT_ITEMS:
                messageFromServer = CatalogController.getProductItems(messageFromClient);
                break;
            case COMPLAINT_ADD_NEW:
                messageFromServer = ComplaintController.setComplaintToDataBase((Complaint) messageFromClient.getData());
                break;
            case COMPLAINT_VALIDATE_CUSTOMER_ORDER:
                messageFromServer = ComplaintController.validateCustomerAndOrderDatabase(messageFromClient.getData());
                break;
            case COMPLAINTS_GET:
                messageFromServer = ComplaintController.getAllComplaintsFromDatabase();
                break;
            case COMPLAINT_CLOSE_UPDATE:
                messageFromServer = ComplaintController.closeStatus((ArrayList<Object>) messageFromClient.getData());
                break;
            case REPORT_ORDER_VIEW:
            case REPORT_REVENUE_VIEW:
            case REPORT_COMPLAINT_VIEW:
                messageFromServer = ReportController.viewReport((ArrayList<String>) messageFromClient.getData());
                break;
            case MANAGER_BRANCH_GET:
                messageFromServer = ReportController.getManagersBranch((Integer)messageFromClient.getData());
                break;
            case DELIVERIES_GET:
                messageFromServer = DeliveryController.getPreDeliveredOrdersFromServer();
                break;
            case DELIVERY_ADD_NEW:
                messageFromServer = DeliveryController.sendDelivery((Order) messageFromClient.getData());
                break;
            case DELIVERY_HISTORY_GET:
                messageFromServer = DeliveryController.getHistoryDeliveredOrdersFromServer();
                break;
            case DELIVERY_ORDER_REFUND:
                messageFromServer = DeliveryController.refundOrder((Order) messageFromClient.getData());
                break;
            case EMPLOYEE_PERMISSION_CHANGE:
                UserController userController = new UserController();
                messageFromServer = userController.changeBranchEmployeePermission((BranchEmployee) messageFromClient.getData());
                break;
            case USER_INFORMATION_GET:
                UserController userController1 = new UserController();
                messageFromServer = userController1.getUserInformation((List<String>)messageFromClient.getData());
                break;
            case CUSTOMER_CREATE_NEW:
                UserController userController2 = new UserController();
                messageFromServer = userController2.createNewCustomer((Customer)messageFromClient.getData());
                break;
            case CUSTOMER_FREEZE:
                UserController userController3 = new UserController();
                messageFromServer = userController3.FreezeCustomer((Customer)messageFromClient.getData());
                break;
            case CUSTOMER_GET_EMAIL:
                UserController userController4 = new UserController();
                messageFromServer = userController4.getCustomerEmail((int) messageFromClient.getData());
                break;
            case GET_USER_PERMISSION:
                UserController userController5 = new UserController();
                messageFromServer = userController5.getUserPermission((User)messageFromClient.getData());
                break;
            default:
                ServerUIController.printToServerConsoleUI("Unhandled task was requested from client: " + messageFromClient.getTask());
                break;
        }
        try {
            if(messageFromServer != null)
                client.sendToClient(messageFromServer);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void clientConnected(ConnectionToClient client) {
        Server.serverUIController.addClientToTable(client);
        ServerUIController.printToServerConsoleUI("Incoming connection from: " + Objects.requireNonNull(client.getInetAddress()).getHostAddress() + " (" + client.getInetAddress().getCanonicalHostName() + ")");
    }

}