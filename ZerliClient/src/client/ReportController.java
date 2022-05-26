package client;

import communication.Message;
import communication.MessageFromClient;

import java.util.ArrayList;


public class ReportController {
    private Message response;

    public void viewReport(ArrayList<String> reportData) {
        Message msg = new Message();
        msg.setData(reportData);
        switch (reportData.get(0)) {
            case "Order":
                msg.setTask(MessageFromClient.VIEW_ORDER_REPORT);
                break;
            case "Revenue":
                msg.setTask(MessageFromClient.VIEW_REVENUE_REPORT);
                break;
            case "Complaints":
                msg.setTask(MessageFromClient.VIEW_COMPLAINTS_REPORT);
                break;
            default:
                break;
        }
        Client.clientController.getClient().handleMessageFromUI(msg,true);
    }

    public Message getResponse() {
        return response;
    }

    public void setResponse(Message response) {
        this.response = response;
    }
}