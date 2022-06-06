package client;

import complaint.Complaint;
import communication.Message;
import communication.MessageFromClient;

import java.util.ArrayList;

/**
 * Controller for everything related to Complaints
 */
public class ComplaintController extends AbstractController {
    private String statusClosed;
    private Message validationResultFromServer;

    public ArrayList<Complaint> listOfComplaints = new ArrayList<>();

    ComplaintController(IMessageService service) {
        super(service);
    }

    /**
     * Sends the complaint to the server
     *
     * @param complaint
     */
    public void setComplaint(Complaint complaint) {
        Message msg = new Message(complaint, MessageFromClient.COMPLAINT_ADD_NEW);
        getService().sendToServer(msg, true);
    }

    /**
     * Sets the message returned from the server with closed status.
     *
     * @param msg
     */
    public void setComplaintStatusReceived(Message msg) {
        this.getService().setResponse(msg);
    }

    /**
     * Sends to server the data to validate if exists in database
     * @param customer
     * @param order
     */
    public void validateCustomerAndOrder(String customer, int order){
        ArrayList<Object> arr = new ArrayList<>();
        arr.add(customer);
        arr.add(order);
        Message msg = new Message(arr,MessageFromClient.COMPLAINT_VALIDATE_CUSTOMER_ORDER);
        getService().sendToServer(msg, true);
    }

    /**
     * sets the response of validation data came from database
     * @param msg
     */
    public void setValidationResult(Message msg){
        this.validationResultFromServer = msg;
    }

    /**
     *
     * @return the validation result
     */
    public Message getValidationResult(){
        return validationResultFromServer;
    }

    /**
     * sends to server request all complaints from database
     */
    public void getAllComplaints() {
        Message msg = new Message(null, MessageFromClient.COMPLAINTS_GET);
        getService().sendToServer(msg, true);
    }

    /**
     * sets the complaints arrived from the server
     * @param responseListFromServer
     */
    public void setAllComplaints(ArrayList<Complaint> responseListFromServer) {
        this.listOfComplaints = responseListFromServer;
    }

    /**
     *
     * @return the list of complaints came from server
     */
    public ArrayList<Complaint> getListOfComplaints(){
        return listOfComplaints;
    }

    /**
     * sends to server the complaint that should be closed.
     * @param obj
     */
    public void closeStatus(ArrayList<Object> obj){
        Message msg = new Message(obj,MessageFromClient.COMPLAINT_CLOSE_UPDATE);
        getService().sendToServer(msg, true);
    }

    /**
     * sets the response came from the server for closing the complaint
     * @param data
     */
    public void setStatusClosed(String data){
        this.statusClosed = data;
    }

    /**
     *
     * @return the message if the closing complaint successes or not.
     */
    public String getStatusClosed() {
        return statusClosed;
    }
}