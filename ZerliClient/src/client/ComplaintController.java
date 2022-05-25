package client;

import branch.Complaint;
import communication.Message;
import communication.MessageFromClient;

import java.util.ArrayList;

public class ComplaintController {
    private Message response;
    private String statusClosed;
    private Message validationResultFromServer;

    public ArrayList<Complaint> listOfComplaints = new ArrayList<>();

    public void setComplaint(Complaint complaint) {
        Message msg = new Message(complaint, MessageFromClient.ADD_NEW_COMPLAINT);
        Client.clientController.getClient().handleMessageFromUI(msg, true);
    }

    public void setComplaintStatusReceived(Message msg) {
        this.response = msg;
    }

    public Message getComplaintStatusReceived() {
        return response;
    }

    public void validateCustomerAndOrder(String customer, int order){
        ArrayList<Object> arr = new ArrayList<>();
        arr.add(customer);
        arr.add(order);
        Message msg = new Message(arr,MessageFromClient.VALIDATE_CUSTOMER_AND_ORDER);
        Client.clientController.getClient().handleMessageFromUI(msg,true);
    }

    public void setValidationResult(Message msg){
        this.validationResultFromServer = msg;
    }

    public Message getValidationResult(){
        return validationResultFromServer;
    }

    public void requestAllOrders() {
        Message msg = new Message(null, MessageFromClient.REQUEST_COMPLAINTS_TABLE);
        Client.clientController.getClient().handleMessageFromUI(msg, true);
    }

    public void setAllComplaints(ArrayList<Complaint> responseListFromServer) {
        this.listOfComplaints = responseListFromServer;
    }

    public ArrayList<Complaint> getListOfComplaints(){
        return listOfComplaints;
    }

    public void closeStatus(ArrayList<Object> obj){
        Message msg = new Message(obj,MessageFromClient.CLOSE_STATUS);
        Client.clientController.getClient().handleMessageFromUI(msg,true);
    }

    public void setStatusClosed(String data){
        this.statusClosed = data;
    }
    public String getStatusClosed() {
        return statusClosed;
    }
}