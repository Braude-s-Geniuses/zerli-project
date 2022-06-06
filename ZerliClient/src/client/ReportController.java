package client;

import communication.Message;
import communication.MessageFromClient;

import java.util.ArrayList;

/**
 * Controller for everything related to Reports
 */
public class ReportController extends AbstractController {

    ReportController(IMessageService service) {
        super(service);
    }

    /**
     * Gets a report from the server given time parameters
     * @param reportData -
     */
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
        getService().sendToServer(msg, true);
    }

    /**
     * Ask from DB the branch of the manager
     * @param userId - branch manager id
     */
    public void getManagersBranch(int userId) {
        Message msg = new Message(userId, MessageFromClient.MANAGER_BRANCH_GET);
        getService().sendToServer(msg, true);
    }

}