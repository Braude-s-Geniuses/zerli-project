package server;

import util.ReportType;
import com.itextpdf.text.DocumentException;
import communication.Message;
import communication.MessageFromServer;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
public class ReportController {
    public static Connection connection = Server.databaseController.getConnection();
    private static HashMap<Integer, String> quarters = new HashMap<>();

    private static void  initQuarters() {
        quarters.put(1, "04");
        quarters.put(4, "01");
        quarters.put(7, "02");
        quarters.put(10, "03");
    }

    /**
     * Every day checks if there's new data to create report (can change to- month/quarter ...)
     */
    public static void TaskGenerateReport() {
        TimerTask task = new TimerTask() {
            public void run() {
                ReportController.generateReportAccordingToDB();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, 86400000); // period: 1 day
    }
    /**
     * Checks what reports need to be created and generate them
     */
    public static void generateReportAccordingToDB() {
        initQuarters();
        int fromYear;
        int fromMonth;
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();

        String lastReport = (String) getLastReport().getData();     //get last report.

        if (lastReport == null) { //First time generating reports
            fromYear = 2020;
            fromMonth = 1;
        }
        else  //Saving last report values
        {
            String yearString = lastReport.split("_")[2].substring(0, 4);   //get year
            String monthString;
            if(lastReport.contains("quarter")) {
                String quarter = lastReport.split("quarter")[1].substring(2, 3);
                int month = Integer.valueOf(quarter) * 3;
                monthString = String.valueOf(month);   //get month
            } else
                monthString = lastReport.split("-")[1].substring(0, 2);
            fromYear = Integer.valueOf(yearString);
            fromMonth = Integer.valueOf(monthString);


            if(fromMonth == 12) {
                fromMonth = 1;
                fromYear++;
            }
            else fromMonth++;
        }
        //Generate report for the previous years (relevant to the first time generating report in zerli)
        for (int i = fromYear; i < currentYear; i++) {
            if (i == fromYear) {
                for (int j = fromMonth; j <= 12; j++) {
                    reportGenerator(j,i);
                }
            } else if (i != fromYear) {
                for (int j = 1; j <= 12; j++) {
                    reportGenerator(j, i);
                }
            }
        }
        // Generate report for the current year
        for (int i = fromMonth; i < currentMonth; i++) {
            reportGenerator(i,currentYear);
        }
    }

    /**
     * Generate monthly and quarterly report
     * @param month - month of the report
     * @param year - year of the report
     */
    private static void reportGenerator(int month, int year){
        try{
            if (month == 4 || month == 7 || month == 10) {
                generateQuarterlyReport(quarters.get(month), String.valueOf(year));
            } else if (month == 1 && year != 2020)
                generateQuarterlyReport(quarters.get(month), String.valueOf(year - 1));

            if (month < 10) {
                generateMonthlyReport("0" + month, String.valueOf(year));
            } else
                generateMonthlyReport(String.valueOf(month), String.valueOf(year));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate monthly revenue, order and complaints reports for all the branches
     * @param month - month of wanted report
     * @param year - year of wanted report
     * @throws DocumentException
     */
    public static void generateMonthlyReport(String month, String year) throws DocumentException {
        AbstractReportsGenerator reportsGenerator;
        ArrayList<String> branchList;
        branchList = (ArrayList<String>) OrderController.getAllBranches().getData();

        for (String branch : branchList) {
            //monthly order report
            reportsGenerator = new ReportMonthlyOrderGenerator(branch, month, year, "Order");
            reportsGenerator.generate(branch);

            //monthly revenue report
            reportsGenerator = new ReportMonthlyRevenueGenerator(branch, month, year, "Revenue");
            reportsGenerator.generate(branch);

            //monthly complaints report
            reportsGenerator = new ReportMonthlyComplaintsGenerator(branch, month, year, "Complaints");
            reportsGenerator.generate(branch);

        }
    }

    /**
     * Generate quarterly revenue, order and complaints reports for all the branches
     * @param quarter - quarter of wanted report
     * @param year - year of wanted report
     * @throws DocumentException
     */
    public static void generateQuarterlyReport(String quarter, String year) throws DocumentException {
        AbstractReportsGenerator reportsGenerator;
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


    /**
     * Asking for the information needed for order report from order table in DB.
     * for monthly report from month = to month
     * @param branch
     * @param fromMonth
     * @param toMonth
     * @param year
     * @return
     */
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

    /**
     * Asking for the information needed for revenue report from revenue table in DB.
     * @param branch
     * @param month
     * @param year
     * @return
     */
    public static ArrayList<Object> extractRevenueInfoForReport(String branch, String month, String year) {
        ArrayList<Object> dataForCells = new ArrayList<>();
        ResultSet rs;
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

    /**
     * Asking for the information needed for complaints report from complaints table in DB.
     * for monthly report from month = to month
     * @param branch
     * @param fromMonth
     * @param toMonth
     * @param year
     * @return
     */
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
            complaintsData.addAll(extractComplaintsInfoForReport(branch, fromMonth, fromMonth, year));
            complaintsData.addAll(extractComplaintsInfoForReport(branch, middleMonth, middleMonth, year));
            complaintsData.addAll(extractComplaintsInfoForReport(branch, toMonth, toMonth, year));

//            String middleMonth = String.valueOf(Integer.valueOf(fromMonth) + 1);
//            complaintsData.add(monthData(branch, fromMonth, year));
//            complaintsData.add(monthData(branch, middleMonth, year));
//            complaintsData.add(monthData(branch, toMonth, year));
        }

        return complaintsData;
    }

    /**
     * Asking for numbers of complaints in specified month and year
     * @param branch
     * @param month
     * @param year
     * @return Sum of complaint that has been made in given month and year
     */
    private static int monthData(String branch, String month, String year) {
        int complaintsCount = 0;
        ResultSet rs;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(distinct complaint_id) AS quantity FROM `complaint`\n" +
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

    /**
     * Asking for numbers of complaints in specified month and year
     * @param branch
     * @param from
     * @param to
     * @return Sum of complaint that has been made in given week
     */
    private static int weekData(String branch, String from, String to) {
        int complaintsCount = 0;
        ResultSet rs = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(distinct complaint_id) AS quantity FROM `complaint`" +
                    "WHERE DATE(created_at) between ? AND ?  AND order_id IN " +
                    "(SELECT order_id FROM `order` WHERE branch = ? );");
            preparedStatement.setString(1, from);
            preparedStatement.setString(2, to);
            preparedStatement.setString(3, branch);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                complaintsCount = rs.getInt("quantity");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return complaintsCount;
    }


    /**
     *  Asking for the information needed for revenue quarterly report from revenue table in DB
     * @param branch
     * @param period
     * @param year
     * @return
     */
    public static HashMap<String, Float> extractRevenueInfoForReportQuarterly(String branch, String period, String year) {
        HashMap<String, Float> revenueData = new HashMap<>();
        int from = Integer.valueOf(period.substring(0, 2));
        int to = Integer.valueOf(period.substring(3));
        for (int i = from; i <= to; i++) {
            revenueData.put(String.valueOf(i), 0f);
        }
        ResultSet rs;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT WEEK(order_date) AS weeks ,sum(discount_price) AS revenue FROM `order` WHERE \n" +
                    "branch = ? AND MONTH(order_date) BETWEEN ? AND ? AND YEAR(order_date) = ? \n" +
                    "AND (order_status = 'NORMAL_COMPLETED' OR order_status = 'EXPRESS_COMPLETED')\n" +
                    "GROUP BY weeks ORDER BY weeks;");
            preparedStatement.setString(1, branch);

            preparedStatement.setString(2, period.substring(0, 2));
            preparedStatement.setString(3, period.substring(3));
            preparedStatement.setString(4, year);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                revenueData.put(rs.getString("weeks"), rs.getFloat("revenue"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return revenueData;
    }

    /**
     * Saves given report in DB
     * @param byteArrayOutputStream
     * @param nameOfFile
     * @param reportType
     * @param period
     * @param YearOfFile
     */
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

    /**
     * Ask for specific report from DB
     * @param data report type, date and branch for show
     * @return the wanted report
     */
    public static Message viewReport(ArrayList<String> data) {
        String name = data.get(0) + "_" + data.get(2) + "_" + data.get(1) + ".pdf";
        SerialBlob blob;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT file FROM `report` WHERE name =?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            blob = new SerialBlob(resultSet.getBlob(1));

        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.REPORT_VIEW_FAIL);
        }
        return new Message(blob, MessageFromServer.REPORT_VIEW_SUCCESS);
    }

    /**
     * Ask for the branch that the manager id is managing
     * @param branchMangerId - id of the manager of the branch
     * @return - branch name
     */
    public static Message getManagersBranch(Integer branchMangerId) {
        String branch;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT branch FROM `branch` WHERE manager_id =?");
            preparedStatement.setInt(1, branchMangerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            branch = resultSet.getString("complaint");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Message(branch, MessageFromServer.MANAGER_BRANCH_GET_SUCCESS);
    }

    /**
     * Gets the latest created report from the database
     * @return Message with the report name in it
     */
    public static Message getLastReport() {
        String lastReport = null;
        Statement stmt;
        try {
            stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT name FROM report WHERE report_id = (select MAX(report_id) FROM report);");
            while (resultSet.next()) {
                lastReport = resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.REPORT_LAST_GET_FAIL);
        }
        return new Message(lastReport, MessageFromServer.REPORT_LAST_GET_SUCCESS);
    }
}