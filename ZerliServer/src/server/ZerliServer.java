package server;

import branch.Complaint;
import communication.Message;
import communication.MessageFromServer;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import order.Order;
import order.Product;
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

                if(messageFromServer.getAnswer() == MessageFromServer.LOGIN_SUCCEED)
                    ServerUIController.setClientLoggedInTable(client, (User) messageFromServer.getData());
                break;
            case LOGOUT_REQUEST:
                loginController= new UserController();
                messageFromServer = loginController.logout((int)messageFromClient.getData());

                if(messageFromServer.getAnswer() == MessageFromServer.LOGOUT_SUCCEED)
                    ServerUIController.setClientLoggedOutTable(client);
                break;
            case REQUEST_ORDERS_TABLE:
                messageFromServer = OrderController.getAllOrdersFromServer((int) messageFromClient.getData());
                break;
            case REQUEST_BRANCHES:
                messageFromServer = OrderController.getAllBranches();
                break;
            case ADD_NEW_ORDER:
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
            case REQUEST_ORDER_PRODUCTS:
                messageFromServer = OrderController.getOrderDetails((Integer)messageFromClient.getData());
                break;
            case UPDATE_BALANCE_FOR_CUSTOMER:
                OrderController.updateBalance((ArrayList<Object>)messageFromClient.getData());
                break;
            case UPDATE_CARD_FOR_CUSTOMER:
                OrderController.updateCreditCard((ArrayList<Object>)messageFromClient.getData());
                break;
            case UPDATE_NEW_CUSTOMER:
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
                messageFromServer = surveyServerController.tryToGetIDsOfSurveys();
                break;
            case SURVEY_SEND:
                surveyServerController= new SurveyController();
                messageFromServer = surveyServerController.tryToInsertSurvey((SurveyAnswers)messageFromClient.getData());
                break;
            case REQUEST_ALL_SURVEY_NAMES_BY_EXPERT:
                surveyServerController= new SurveyController();
                messageFromServer = surveyServerController.tryToGetSurveyNames((int)messageFromClient.getData());
                break;
            case REQUEST_ALL_SURVEY_ANSWERS:
                surveyServerController= new SurveyController();
                messageFromServer = surveyServerController.tryToGetSurveyAnswers((String)messageFromClient.getData());
                break;
            case REQUEST_UPLOAD_SURVEY_SUMMARY:
                surveyServerController= new SurveyController();
                messageFromServer = surveyServerController.tryToUploadFile((Survey)messageFromClient.getData());
                break;
            case REQUEST_SURVEY_ID:
                surveyServerController= new SurveyController();
                messageFromServer = surveyServerController.tryToGetSurveyID((String)messageFromClient.getData());
                break;
            case CATALOG_GET_PRODUCT_ITEMS:
                messageFromServer = CatalogController.getProductItems(messageFromClient);
                break;
            case ADD_NEW_COMPLAINT:
                messageFromServer = ComplaintController.setComplaintToDataBase((Complaint) messageFromClient.getData());
                break;
            case VALIDATE_CUSTOMER_AND_ORDER:
                messageFromServer = ComplaintController.validateCustomerAndOrderDatabase(messageFromClient.getData());
                break;
            case REQUEST_COMPLAINTS_TABLE:
                messageFromServer = ComplaintController.getAllComplaintsFromDatabase();
                break;
            case CLOSE_STATUS:
                messageFromServer = ComplaintController.closeStatus((ArrayList<Object>) messageFromClient.getData());
                break;
            case VIEW_ORDER_REPORT:
            case VIEW_REVENUE_REPORT:
            case VIEW_COMPLAINTS_REPORT:
                messageFromServer = ReportController.viewReport((ArrayList<String>) messageFromClient.getData());
                break;
            case REQUEST_DELIVERIES_TABLE:
                messageFromServer = DeliveryController.getPreDeliveredOrdersFromServer();
                break;
            case SEND_DELIVERY:
                messageFromServer = DeliveryController.sendDelivery((Order) messageFromClient.getData());
                break;
            case REQUEST_DELIVERIES_HISTORY_TABLE:
                messageFromServer = DeliveryController.getHistoryDeliveredOrdersFromServer();
                break;
            case REFUND_ORDER:
                messageFromServer = DeliveryController.refundOrder((Order) messageFromClient.getData());
                break;
            case CHANGE_PERMISSION:
                UserController userController = new UserController();
                messageFromServer = userController.changeBranchEmployeePermission((BranchEmployee) messageFromClient.getData());
                break;
            case GET_USER_INFORMATION:
                UserController userController1 = new UserController();
                messageFromServer = userController1.getUserInformation((List<String>)messageFromClient.getData());
                break;
            case CREATE_NEW_CUSTOMER:
                UserController userController2 = new UserController();
                messageFromServer = userController2.createNewUser((Customer)messageFromClient.getData());
                break;
            case FREEZE_CUSTOMER:
                UserController userController3 = new UserController();
                messageFromServer = userController3.FreezeCustomer((Customer)messageFromClient.getData());
                break;
            default:
                ServerUIController.printToServerConsoleUI("Unhandled task was requested from server: " + messageFromClient.getTask());
                break;
        }
        try {
            if(messageFromServer != null)
                client.sendToClient(messageFromServer);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    @Override
    protected void clientConnected(ConnectionToClient client) {
        Server.serverUIController.addClientToTable(client);
        ServerUIController.printToServerConsoleUI("Incoming connection from: " + Objects.requireNonNull(client.getInetAddress()).getHostAddress() + " (" + client.getInetAddress().getCanonicalHostName() + ")");
    }

}