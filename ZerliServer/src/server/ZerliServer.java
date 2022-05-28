package server;

import branch.Complaint;
import communication.Message;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import order.Order;
import servergui.ServerUIController;
import survey.Survey;
import survey.SurveyAnswers;
import user.User;

import java.io.IOException;
import java.util.ArrayList;

import static communication.MessageFromServer.LOGIN_SUCCEED;
import static communication.MessageFromServer.LOGOUT_SUCCEED;


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
        } catch (IOException e) {}
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
                Server.serverUIController.removeClientFromTable(client);
                ServerUIController.printToServerConsoleUI(client.getInetAddress().getHostAddress() + " (" + client.getInetAddress() + ") has disconnected");
                break;
            case LOGIN_REQUEST:
                UserController loginController= new UserController();
                messageFromServer = loginController.login((User)messageFromClient.getData());

                if(messageFromServer.getAnswer() == LOGIN_SUCCEED)
                    ServerUIController.setClientLoggedInTable(client, (User) messageFromServer.getData());
                break;
            case LOGOUT_REQUEST:
                loginController= new UserController();
                messageFromServer = loginController.logout((int)messageFromClient.getData());

                if(messageFromServer.getAnswer() == LOGOUT_SUCCEED)
                    ServerUIController.setClientLoggedOutTable(client);
                break;
//            case GET_PRODUCT:
//                messageFromServer = CatalogController.getProductsFromDataBase();
//                break;
            case REQUEST_ORDERS_TABLE:
                messageFromServer = OrderController.getAllOrdersFromServer((int) messageFromClient.getData());
                break;
            case REQUEST_BRANCHES:
                messageFromServer = OrderController.getAllBranches();
                break;
            case ADD_NEW_ORDER:
                messageFromServer = OrderController.AddNewOrder((Order) messageFromClient.getData());
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
            case PRODUCT_UPDATE:
                messageFromServer = ProductController.updateProduct(messageFromClient);
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
            default:
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
        ServerUIController.printToServerConsoleUI("Incoming connection from: " + client.getInetAddress().getHostAddress() + " (" + client.getInetAddress().getCanonicalHostName() + ")");
    }

}