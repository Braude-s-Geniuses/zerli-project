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
                msg.setTask(MessageFromClient.REPORT_ORDER_VIEW);
                break;
            case "Revenue":
                msg.setTask(MessageFromClient.REPORT_REVENUE_VIEW);
                break;
            case "Complaints":
                msg.setTask(MessageFromClient.REPORT_COMPLAINT_VIEW);
                break;
            default:
                break;
        }
        Client.clientController.getClient().handleMessageFromUI(msg,true);
    }

    /**
     * Ask from DB the branch of the manager
     * @param userId - branch manager id
     */
    public void getManagersBranch(int userId) {
        Message msg = new Message(userId, MessageFromClient.MANAGER_BRANCH_GET);
        Client.clientController.getClient().handleMessageFromUI(msg,true);
    }

    public Message getResponse() {
        return response;
    }

    public void setResponse(Message response) {
        this.response = response;
    }
}