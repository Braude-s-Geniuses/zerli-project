package unittest;

import org.junit.Before;
import org.junit.Test;
import server.DatabaseController;
import server.ReportController;
import server.UserController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import static org.junit.Assert.*;
import static server.Server.databaseController;

public class RevenueReportTest {

    /**
     *  Stub class for DatabaseController to mock DB connection
     */

    public class DatabaseControllerStub extends DatabaseController {

        public DatabaseControllerStub(){

            databaseController.getInstance();
        }

        @Override
        public String connect() {
            String result = "";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
                result += "\nDriver definition succeed";
            } catch (Exception ex) {
                /* handle the error*/
                result += "\nDriver definition failed";
            }

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost/" + getDbName() + "?serverTimezone=Asia/Jerusalem", getDbUser(), getDbPassword());
                result += "\nSQL connection succeed";

            } catch (SQLException ex) {/* handle any erroresultSet*/
                result += "\nSQLException: " + ex.getMessage();
                result += "\nSQLState: " + ex.getSQLState();
                result += "\nVendorError: " + ex.getErrorCode();
            }
            return result;
        }
    }

    static UserController userController;

    @Before
    public void setUp(){
        DatabaseControllerStub databaseControllerStub = new DatabaseControllerStub();
        databaseControllerStub.setDbName("zerli");
        databaseControllerStub.setDbUser("root");
        databaseControllerStub.setDbPassword("root");
        databaseControllerStub.connect();
        Connection con = databaseControllerStub.getConnection();
        userController = new UserController(con);
        ReportController.connection= con;

    }
    // Description: revenue report quarterly successfully.
    // Input: branch = "Karmiel" with priod "04-06" and year "2021".
    // Expected result: HashMap who is the right data from DB.

    @Test
    public void checkRevenueReportQuarterlySuccessfully() {
        HashMap<String,Float> expected = new HashMap<String,Float>();
        expected.put("13",200.0f);
        expected.put("14",150.0f);
        expected.put("15",192.0f);
        expected.put("16",445.0f);
        expected.put("17",164.0f);
        expected.put("18",307.0f);
        expected.put("19",70.0f);
        expected.put("20",445.0f);
        expected.put("21",234.0f);
        expected.put("22",465.0f);
        expected.put("23",198.0f);
        expected.put("24",327.0f);
        expected.put("25",625.0f);
        expected.put("26",285.0f);

        HashMap <String,Float> actual;
        actual = ReportController.extractRevenueInfoForReportQuarterly("Karmiel","04-06","2021");

        assertEquals(actual,expected);
    }

    // Description: check empty report.
    // Input: branch = "Karmiel" with priod "01-03" and year "2021".
    // Expected result: get empty HashMap.

    @Test
    public void checkRevenueEmptyReportQuarterly() {
        HashMap<String,Float> expected = new HashMap<String,Float>();

        HashMap <String,Float> actual;
        actual = ReportController.extractRevenueInfoForReportQuarterly("Karmiel","01-03","2021");
        assertEquals(actual,expected);

    }

    // Description: check period "".
    // Input: branch = "Karmiel" with priod "" and year "2021".
    // Expected result: get StringIndexOutOfBoundsException.

    @Test
    public void checkRevenueEmptyPeriodReportQuarterly() {
        HashMap <String,Float> actual;
        try {
            actual = ReportController.extractRevenueInfoForReportQuarterly("Karmiel","","2021");
        } catch (StringIndexOutOfBoundsException e) {
            assertTrue(true);
            return;
        }
        fail();
    }
    // Description: check period 1-3.
    // Input: branch = "Karmiel" with priod 1-3 and year "2021".
    // Expected result: get NumberFormatException.

    @Test
    public void checkRevenuePeriodThan_1_3ReportQuarterly() {
        HashMap <String,Float> actual;
        try {
            actual = ReportController.extractRevenueInfoForReportQuarterly("Karmiel","1-3","2021");
        } catch (NumberFormatException e) {
            assertTrue(true);
            return;
        }
        fail();
    }

    // Description: check period 15-18.
    // Input: branch = "Karmiel" with priod 15-18 and year "2021".
    // Expected result: empty HashMap.

    @Test
    public void checkRevenuePeriodThan_15_18ReportQuarterly() {
        HashMap <String,Float> actual;
        HashMap <String,Float> expected = new HashMap<String,Float>();
        actual = ReportController.extractRevenueInfoForReportQuarterly("Karmiel","15-18","2021");
        assertEquals(expected,actual);
    }

    // Description: check period null.
    // Input: branch = "Karmiel" with priod null and year "2021".
    // Expected result: get NullPointerException.

    @Test
    public void checkRevenuePeriodNULLReportQuarterly() {
        HashMap <String,Float> actual;
        try {
            actual = ReportController.extractRevenueInfoForReportQuarterly("Karmiel",null,"2021");
        } catch (NullPointerException e) {
            assertTrue(true);
            return;
        }
        fail();
    }

    // Description: check branch not exist.
    // Input: branch = "icecream" with priod "04-06" and year "2021".
    // Expected result: empty HashMap.

    @Test
    public void checkRevenueBranchNotExistReportQuarterly() {
        HashMap <String,Float> expected = new HashMap<String,Float>();
        HashMap <String,Float> actual;
        actual = ReportController.extractRevenueInfoForReportQuarterly("Icecream","04-06","2021");
        assertEquals(expected,actual);
    }
    // Description: check branch not exist.
    // Input: branch = null with priod "04-06" and year "2021".
    // Expected result: get NullPointerException.

    @Test
    public void checkRevenueBranchNULLReportQuarterly() {
        HashMap <String,Float> actual;
        try {
            actual = ReportController.extractRevenueInfoForReportQuarterly(null,"01-03","2021");
        } catch (NullPointerException e) {
            assertTrue(true);
            return;
        }
        fail();
    }

    // Description: check year "".
    // Input: branch = "Karmiel" with priod "04-06" and year "".
    // Expected result: get StringIndexOutOfBoundsException.

    @Test
    public void checkRevenueEmptyYearReportQuarterly() {
        HashMap <String,Float> actual;
        try {
            actual = ReportController.extractRevenueInfoForReportQuarterly("Karmiel","04-06","");
        } catch (StringIndexOutOfBoundsException e) {
            assertTrue(true);
            return;
        }
        fail();
    }

    // Description: check year "21".
    // Input: branch = "Karmiel" with priod "04-06" and year "21".
    // Expected result: get StringIndexOutOfBoundsException.

    @Test
    public void checkRevenueYear_21ReportQuarterly() {
        HashMap <String,Float> actual;
        try {
            actual = ReportController.extractRevenueInfoForReportQuarterly("Karmiel","04-06","21");
        } catch (StringIndexOutOfBoundsException e) {
            assertTrue(true);
            return;
        }
        fail();
    }

    // Description: check year "2023".
    // Input: branch = "Karmiel" with priod "04-06" and year "2023".
    // Expected result: empty HashMap.

    @Test
    public void checkRevenueYear_2023ReportQuarterly() {
        HashMap <String,Float> actual;
        HashMap <String,Float> expected = new HashMap<String,Float>();
        actual = ReportController.extractRevenueInfoForReportQuarterly("Karmiel","04-06","2023");
        assertEquals(expected,actual);
    }

    // Description: check year null.
    // Input: branch = "Karmiel" with priod "04-06" and year null.
    // Expected result: get NullPointerException.

    @Test
    public void checkRevenueYearNULLReportQuarterly() {
        HashMap <String,Float> actual;
        try {
            actual = ReportController.extractRevenueInfoForReportQuarterly("Karmiel","04-06",null);
        } catch (NullPointerException e) {
            assertTrue(true);
            return;
        }
        fail();
    }

    // Description: check wrong details.
    // Input: branch = "Karmiel" with priod "04-06" and year "2021".
    // Expected result: HashMap not equals.

    @Test
    public void checkRevenueWrongReportQuarterly() {
        HashMap<String, Float> expected = new HashMap<String, Float>();
        expected.put("13", 211.0f);
        expected.put("14", -9.0f);
        expected.put("15", 1.0f);
        expected.put("16", 6.0f);
        expected.put("17", 11.0f);
        expected.put("18", 35.0f);
        expected.put("19", 70.0f);
        expected.put("20", 0.0f);
        HashMap<String, Float> actual;
        actual = ReportController.extractRevenueInfoForReportQuarterly("Karmiel", "04-06", "2021");
        assertNotEquals(actual,expected);
    }

}
