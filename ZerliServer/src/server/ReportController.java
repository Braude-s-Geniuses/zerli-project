package server;

import report.ReportType;
import com.itextpdf.text.DocumentException;
import communication.Message;
import communication.MessageFromServer;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
public class ReportController {

    public static Connection connection = Server.databaseController.getConnection();

    public static void getLastReports() {
        HashMap<Integer, String> quarters = new HashMap<>();
        {
            {
                quarters.put(1, "04");
                quarters.put(4, "01");
                quarters.put(7, "02");
                quarters.put(10, "03");
            }
        }

        String lastReport = (String) OrderController.getLastReport().getData();     //get last report.
        if (lastReport == null) {                                                    //if first time.
            LocalDate currentDate = LocalDate.now();
            int currentYear = currentDate.getYear();
            int currentMonth = currentDate.getMonthValue();

            for (int i = 2017; i < currentYear; i++) {
                for (int j = 1; j <= 12; j++) {
                    if (j == 4 || j == 7 || j == 10 || j == 1) {
                        try {
                            generateReportQuarterly(quarters.get(j), String.valueOf(i));
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        if(j < 10){
                            generateReport("0" + j, String.valueOf(i));
                        }
                        else
                            generateReport(String.valueOf(j), String.valueOf(i));
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (int i = 1; i <= currentMonth; i++) {       //for current year.
                if (i == 4 || i == 7 || i == 10 || i == 1) {
                    try {
                        generateReportQuarterly(quarters.get(i), String.valueOf(currentYear));
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if(i < 10) {
                        generateReport("0" + i, String.valueOf(currentYear));
                    }
                    else
                        generateReport(String.valueOf(i), String.valueOf(currentYear));
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }

        } else {     //not first time activating.
            String yearString = lastReport.substring(lastReport.lastIndexOf('.') - 7, lastReport.lastIndexOf('.') - 3);   //get year
            String monthString = lastReport.substring(lastReport.lastIndexOf('.') - 2, lastReport.lastIndexOf('.'));   //get month
            int yearFromDB = Integer.valueOf(yearString);
            int monthFromDB = Integer.valueOf(monthString);
            LocalDate currentDate = LocalDate.now();
            int currentYear = currentDate.getYear();
            int currentMonth = currentDate.getMonthValue();

            for (int i = yearFromDB; i < currentYear; i++) {
                if (i == yearFromDB) {
                    for (int j = monthFromDB + 1; j <= 12; j++) {
                        if (j == 4 || j == 7 || j == 10 || j == 1) {
                            try {
                                generateReportQuarterly(quarters.get(j), String.valueOf(i));
                            } catch (DocumentException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            if( j < 10) {
                                generateReport("0" + j, String.valueOf(i));
                            }
                            else
                                generateReport(String.valueOf(j), String.valueOf(i));
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (i != yearFromDB) {
                    for (int j = 1; j <= 12; j++) {
                        if (j == 4 || j == 7 || j == 10 || j == 1) {
                            try {
                                generateReportQuarterly(quarters.get(j), String.valueOf(i));
                            } catch (DocumentException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            if( j < 10) {
                                generateReport("0" + j, String.valueOf(i));
                            }
                            else
                                generateReport(String.valueOf(j), String.valueOf(i));
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            for (int i = monthFromDB + 1; i <= currentMonth; i++) {       //for current year.
                if (i == 4 || i == 7 || i == 10 || i == 1) {
                    try {
                        generateReportQuarterly(quarters.get(i), String.valueOf(currentYear));
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if(i < 10) {
                        generateReport("0" + i, String.valueOf(currentYear));
                    }
                    else
                        generateReport(String.valueOf(i), String.valueOf(currentYear));
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void generateReport(String month, String year) throws DocumentException {
        AbstractReportsGenerator reportsGenerator;
        ArrayList<String> branchList;
        branchList = (ArrayList<String>) OrderController.getAllBranches().getData();

        for (String branch : branchList) {

            //monthly order report
            reportsGenerator = new ReportOrderMonthlyGenerator(branch, month, year, "Order");
            reportsGenerator.generate(branch);

            //monthly revenue report
            reportsGenerator = new ReportRevenueMonthlyGenerator(branch, month, year, "Revenue");
            reportsGenerator.generate(branch);

            //monthly complaints report
            reportsGenerator = new ReportComplaintsMonthlyGenerator(branch, month, year, "Complaints");
            reportsGenerator.generate(branch);

        }
    }

    public static void generateReportQuarterly(String quarter, String year) throws DocumentException {
        AbstractReportsGenerator reportsGenerator;
        ArrayList<String> reportData = new ArrayList<>();
        ArrayList<String> branchList;
        branchList = (ArrayList<String>) OrderController.getAllBranches().getData();

        for (String branch : branchList) {
            //Quarterly revenue report
            reportsGenerator = new ReportQuarterlyRevenueGenerator(branch, quarter, year, "Revenue");
            reportsGenerator.generate(branch);

            //Quarterly Order report
            reportsGenerator = new ReportQuarterlyOrderGenerator(branch, quarter, year, "Order");
            reportsGenerator.generate(branch);

            //Quarterly Complaints report
            reportsGenerator = new ReportQuarterlyComplaintsGenerator(branch, quarter, year, "Complaints");
            reportsGenerator.generate(branch);
        }
    }

    public static void checkForReportUpdates() {
        TimerTask task = new TimerTask() {
            public void run() {
                ReportController.getLastReports();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, 86400000); // period: 1 day
    }


    public static ArrayList<Object> extractOrderInfoForReport(String branch, String fromMonth, String toMonth, String year) {
        ArrayList<Object> dataForCells = new ArrayList<>();
        ResultSet rs = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT op.product_id, MONTH(o.order_date) AS month, p.name, sum(distinct op.quantity) AS quantity FROM `order_product` op, `product` p, `order` o\n" +
                    " WHERE \n" +
                    " op.product_id = p.product_id AND o.order_id = op.order_id AND branch = ? AND MONTH(order_date) BETWEEN ? AND ? AND YEAR(order_date) = ?  \n" +
                    " AND (order_status = 'NORMAL_COMPLETED' OR order_status = 'EXPRESS_COMPLETED') \n" +
                    " GROUP BY month, op.product_id ORDER BY op.product_id, month ASC;\n" +
                    "                            ");
            preparedStatement.setString(1, branch);
            preparedStatement.setString(2, fromMonth);
            preparedStatement.setString(3, toMonth);
            preparedStatement.setString(4, year);
            rs = preparedStatement.executeQuery();
            if(rs.next() == true) {
                if (fromMonth.equals(toMonth)) {
                    do {
                        dataForCells.add(rs.getInt("product_id"));
                        dataForCells.add(rs.getString("name"));
                        dataForCells.add(rs.getInt("quantity"));
                    }while (rs.next());
                } else {
                    int[] quantitys = {0, 0, 0};
                    int productId = -1;

                    do {
                        if (productId != rs.getInt("product_id")) {      //if new product.
                            if (productId != -1) {                                  //if first time getting a new product.
                                int sum = 0;
                                for (int quantity : quantitys) {
                                    dataForCells.add(quantity);
                                    sum += quantity;
                                }
                                dataForCells.add(sum);
                                quantitys = new int[]{0, 0, 0};
                            }
                            dataForCells.add(rs.getInt("product_id"));
                            dataForCells.add(rs.getString("name"));

                            productId = rs.getInt("product_id");

                            quantitys[0] = rs.getInt("quantity");
                        } else {
                            if (quantitys[1] == 0) {
                                quantitys[1] = rs.getInt("quantity");
                            } else {
                                quantitys[2] = rs.getInt("quantity");
                            }
                        }
                    }while (rs.next());
                    //last result
                    for (int quantity : quantitys) {
                        dataForCells.add(quantity);
                    }
                    dataForCells.add(quantitys[0] + quantitys[1] + quantitys[2]);   //sum
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataForCells;
    }

    public static ArrayList<Object> extractRevenueInfoForReport(String branch, String month, String year) {
        ArrayList<Object> dataForCells = new ArrayList<>();
        ResultSet rs = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT order_id, order_date, discount_price FROM `order` " +
                    "WHERE " +
                    "branch = ? AND MONTH(order_date) = ? AND YEAR(order_date) = ?  " +
                    "AND (order_status = 'NORMAL_COMPLETED' OR order_status = 'EXPRESS_COMPLETED');");
            preparedStatement.setString(1, branch);
            preparedStatement.setString(2, month);
            preparedStatement.setString(3, year);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                dataForCells.add(rs.getInt("order_id"));
                dataForCells.add(rs.getString("order_date"));
                dataForCells.add(rs.getInt("discount_price"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataForCells;
    }

    public static ArrayList<Integer> extractComplaintsInfoForReport(String branch, String fromMonth, String toMonth, String year) {
        ArrayList<Integer> complaintsData = new ArrayList<>();

        if (fromMonth.equals(toMonth)) {
            String month = fromMonth;
            complaintsData.add(weekData(branch, year + "-" + month + "-01", year + "-" + month + "-07"));
            complaintsData.add(weekData(branch, year + "-" + month + "-08", year + "-" + month + "-14"));
            complaintsData.add(weekData(branch, year + "-" + month + "-15", year + "-" + month + "-21"));
            complaintsData.add(weekData(branch, year + "-" + month + "-22", year + "-" + month + "-28"));
            complaintsData.add(weekData(branch, year + "-" + month + "-29", year + "-" + month + "-31"));
        } else {
            String middleMonth = String.valueOf(Integer.valueOf(fromMonth) + 1);
            complaintsData.add(monthData(branch, fromMonth, year));
            complaintsData.add(monthData(branch, middleMonth, year));
            complaintsData.add(monthData(branch, toMonth, year));
        }

        return complaintsData;
    }

    private static int monthData(String branch, String month, String year) {
        int complaintsCount = 0;
        ResultSet rs = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(distinct complaint_id) AS quantity FROM Complaint\n" +
                    " WHERE order_id IN\n" +
                    "(SELECT order_id FROM `order` WHERE branch = ? AND YEAR(order_date) = ? AND MONTH(order_date) = ?);");
            preparedStatement.setString(1, branch);
            preparedStatement.setString(2, year);
            preparedStatement.setString(3, month);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                complaintsCount = rs.getInt("quantity");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return complaintsCount;
    }

    private static int weekData(String branch, String from, String to) {
        int complaintsCount = 0;
        ResultSet rs = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(distinct complaint_id) AS quantity FROM `Complaint`" +
                    "WHERE order_id IN " +
                    "(SELECT order_id FROM `order` WHERE branch = ? AND order_date between ? AND ?);");
            preparedStatement.setString(1, branch);
            preparedStatement.setString(2, from);
            preparedStatement.setString(3, to);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                complaintsCount = rs.getInt("quantity");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return complaintsCount;
    }

    public static void saveReport(ByteArrayOutputStream byteArrayOutputStream, String nameOfFile, ReportType reportType, String period, String YearOfFile) {
        int orderId = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `report` (report_id, name, report_type, file, month, quarter, year) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, nameOfFile);
            preparedStatement.setString(3, reportType.name());
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(pdfBytes);
            preparedStatement.setBlob(4, bais);
            if (reportType.toString().substring(0, 7).equals("MONTHLY")) {
                preparedStatement.setString(5, period);
                preparedStatement.setString(6, null);
            } else {       //for quarter
                preparedStatement.setString(5, null);
                preparedStatement.setString(6, period);
            }

            preparedStatement.setString(7, YearOfFile);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Message viewReport(ArrayList<String> data) {
        String name = data.get(0) + "_" + data.get(2) + "_" + data.get(1) + ".pdf";
        SerialBlob blob = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT file FROM `report` WHERE name =?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            blob = new SerialBlob(resultSet.getBlob(1));

        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.ORDER_REPORT_IMPORTED_NOT_SUCCESSFULLY);
        }
        return new Message(blob, MessageFromServer.ORDER_REPORT_IMPORTED_SUCCESSFULLY);
    }

    public static HashMap<String, Float> extractRevenueInfoForReportQuarterly(String branch, String period, String year) {
        HashMap<String, Float> revenueData = new HashMap<>();
        int from = Integer.valueOf(period.substring(0, 2));
        int to = Integer.valueOf(period.substring(3));
        for (int i = from; i <= to; i++) {
            revenueData.put(String.valueOf(i), 0f);
        }
        ResultSet rs = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MONTH(order_date) AS Month ,sum(discount_price) AS revenue FROM `order` WHERE \n" +
                    "branch = ? AND MONTH(order_date) BETWEEN ? AND ? AND YEAR(order_date) = ? \n" +
                    "AND (order_status = 'NORMAL_COMPLETED' OR order_status = 'EXPRESS_COMPLETED')\n" +
                    "GROUP BY Month ORDER BY Month;");
            preparedStatement.setString(1, branch);

            preparedStatement.setString(2, period.substring(0, 2));
            preparedStatement.setString(3, period.substring(3));
            preparedStatement.setString(4, year);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                revenueData.put(rs.getString("Month"), rs.getFloat("revenue"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return revenueData;
    }

    public static HashMap<String, Float> extractOrderInfoForReportQuarterly(String branch, String period, String year) {
        HashMap<String, Float> orderData = new HashMap<>();
        int from = Integer.valueOf(period.substring(0, 2));
        int to = Integer.valueOf(period.substring(3));
        for (int i = from; i <= to; i++) {
            orderData.put(String.valueOf(i), 0f);
        }
        ResultSet rs = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MONTH(order_date) AS Month ,count(order_id) AS orders FROM `order` WHERE \n" +
                    "branch = ? AND MONTH(order_date) BETWEEN ? AND ? AND YEAR(order_date) = ? \n" +
                    "AND (order_status = 'NORMAL_COMPLETED' OR order_status = 'EXPRESS_COMPLETED')\n" +
                    "GROUP BY Month ORDER BY Month;");
            preparedStatement.setString(1, branch);
            preparedStatement.setString(2, period.substring(0, 2));
            preparedStatement.setString(3, period.substring(3));
            preparedStatement.setString(4, year);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                orderData.put(rs.getString("Month"), rs.getFloat("orders"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderData;
    }
}