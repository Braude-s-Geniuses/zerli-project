package client;

import branch.Complaint;
import communication.Message;
import communication.MessageFromClient;

import java.util.ArrayList;

/**
 * Controller for everything related to Complaints
 */
public class ComplaintController {
    private Message response;
    private String statusClosed;
    private Message validationResultFromServer;

    public ArrayList<Complaint> listOfComplaints = new ArrayList<>();

    /**
     * Sends the complaint to the server
     *
     * @param complaint
     */
    public void setComplaint(Complaint complaint) {
        Message msg = new Message(complaint, MessageFromClient.ADD_NEW_COMPLAINT);
        Client.clientController.getClient().handleMessageFromUI(msg, true);
    }

    /**
     * Sets the message returned from the server with closed status.
     *
     * @param msg
     */
    public void setComplaintStatusReceived(Message msg) {
        this.response = msg;
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
        Message msg = new Message(arr,MessageFromClient.VALIDATE_CUSTOMER_AND_ORDER);
        Client.clientController.getClient().handleMessageFromUI(msg,true);
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
        Message msg = new Message(null, MessageFromClient.REQUEST_COMPLAINTS_TABLE);
        Client.clientController.getClient().handleMessageFromUI(msg, true);
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
        Message msg = new Message(obj,MessageFromClient.CLOSE_STATUS);
        Client.clientController.getClient().handleMessageFromUI(msg,true);
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