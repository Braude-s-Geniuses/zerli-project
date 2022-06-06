package server;

import complaint.Complaint;
import complaint.ComplaintStatus;
import communication.Message;
import communication.MessageFromServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.lang.String.valueOf;

public class ComplaintController {

    public static Connection connection = Server.databaseController.getConnection();

    /**
     * Inserts the arrived complaint to database
     * @param complaint
     * @return Message with proper response
     */
    public static Message setComplaintToDataBase(Complaint complaint) {
        try {

            PreparedStatement preparedStmt = connection.prepareStatement("INSERT INTO `complaint` (complaint_id, customer_id, expert_id, order_id, status, created_at,description) VALUES (?,?,?,?,?,?,?);");
            preparedStmt.setInt(1, complaint.getComplaintId());
            preparedStmt.setString(2, complaint.getCustomerId());
            preparedStmt.setInt(3, complaint.getServiceId());
            preparedStmt.setInt(4, complaint.getOrderId());
            preparedStmt.setString(5, valueOf(complaint.getComplaintStatus()));
            preparedStmt.setTimestamp(6, complaint.getCreatedAt());
            preparedStmt.setString(7, complaint.getDescription());
            preparedStmt.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Message("Failed to add Complaint!", MessageFromServer.COMPLAINT_RESPONSE);
        }
        return new Message("Added Successfully", MessageFromServer.COMPLAINT_RESPONSE);
    }

    /**
     * Validates if such customer exists and if there is order provided is connected to him.
     * @param data
     * @return
     */
    public static Message validateCustomerAndOrderDatabase(Object data) {
        ArrayList<Object> objToSend = new ArrayList<>();
        ArrayList<Object> arr = (ArrayList<Object>) data;
        String customerUsername = (String) arr.get(0);
        int orderID = (int) arr.get(1);

        int customerUserId = 0;
        try {
            PreparedStatement preparedUsernameValidationStatement = connection.prepareStatement("SELECT user_id FROM user WHERE id = '" + customerUsername + "'");
            ResultSet validationUsernameResult = preparedUsernameValidationStatement.executeQuery();
            if (!validationUsernameResult.next()) {
                objToSend.add("No such username in database");
                return new Message(objToSend, MessageFromServer.COMPLAINT_VALIDATE_RESPONSE);
            } else {
                customerUserId = validationUsernameResult.getInt("user_id");
            }


            PreparedStatement preparedOrderValidationStatement = connection.prepareStatement("SELECT * FROM `order` WHERE order_id = ? AND customer_id = ?");
            preparedOrderValidationStatement.setInt(1, orderID);
            preparedOrderValidationStatement.setInt(2, customerUserId);
            ResultSet validationOrderResult = preparedOrderValidationStatement.executeQuery();

            if (!validationOrderResult.next()) {
                objToSend.add("No such order connected to this username in database");
                return new Message(objToSend, MessageFromServer.COMPLAINT_VALIDATE_RESPONSE);
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        objToSend.clear();
        objToSend.add("Validated Successfully!");
        objToSend.add(customerUsername);
        return new Message(objToSend, MessageFromServer.COMPLAINT_VALIDATE_RESPONSE);
    }

    /**
     * Returns all complaints exists in the database
     * @return
     */
    public static Message getAllComplaintsFromDatabase() {
        ArrayList<Complaint> complaints = new ArrayList<>();
        try {
            PreparedStatement preparedComplaintStatement = connection.prepareStatement("SELECT * FROM complaint");
            ResultSet complaintResult = preparedComplaintStatement.executeQuery();

            while (complaintResult.next()) {

                Complaint complaint = new Complaint();

                complaint.setComplaintId(complaintResult.getInt("complaint_id"));
                complaint.setCustomerId(complaintResult.getString("customer_id"));
                complaint.setServiceId(complaintResult.getInt("expert_id"));
                complaint.setOrderId(complaintResult.getInt("order_id"));
                complaint.setComplaintStatus(ComplaintStatus.valueOf(complaintResult.getString("status")));
                complaint.setCreatedAt(complaintResult.getTimestamp("created_at"));
                complaint.setDescription(complaintResult.getString("description"));

                complaints.add(complaint);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return new Message(complaints, MessageFromServer.COMPLAINTS_GET_SUCCESS);
    }

    /**
     * Closes the complaint and returns ok if the complaint closed properly
     * @param msg
     * @return
     */
    public static Message closeStatus(ArrayList<Object> msg) {
        double amountToAdd;
        double sum = 0;
        int complaintID = (Integer) msg.get(0);
        String customerID = (String) msg.get(1);

        try {
            PreparedStatement preparedStmt = connection.prepareStatement("UPDATE `complaint` SET `status` = 'CLOSED' WHERE (`complaint_id` = ' " + complaintID + "');");
            preparedStmt.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            PreparedStatement preparedBalanceStatement = connection.prepareStatement("SELECT balance\n" +
                    "FROM customer\n" +
                    "WHERE customer_id=" + customerID);
            ResultSet statusResult = preparedBalanceStatement.executeQuery();
            while (statusResult.next()) {
                sum = statusResult.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        int customerUserid = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id FROM user WHERE id = ?");
            preparedStatement.setString(1, customerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            customerUserid = resultSet.getInt("user_id");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if (msg.get(2) != null) {
            amountToAdd =(Double) msg.get(2);
            sum += amountToAdd;
            try {
                PreparedStatement preparedStmt = connection.prepareStatement("UPDATE `customer` SET `balance` = " + sum + " WHERE (`customer_id` = ' " + customerUserid + "');");
                preparedStmt.execute();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return new Message("Added Successfully", MessageFromServer.COMPLAINT_CLOSE_SUCCESS);
    }
}
