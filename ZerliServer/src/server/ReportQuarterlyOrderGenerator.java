package server;

import util.ReportType;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.time.Month;
import java.util.ArrayList;
import java.util.Locale;

import static com.itextpdf.text.pdf.BaseFont.HELVETICA;
import static com.itextpdf.text.pdf.BaseFont.HELVETICA_BOLD;

public class ReportQuarterlyOrderGenerator extends AbstractQuarterlyReportGenerator {
    private final ArrayList<String> orderColumns = new ArrayList<String>();
    public ArrayList<String> getOrderColumns() {return orderColumns;}
    /**
     * Creates order report column in addition to the month in the wanted quarter
     * @param branch
     * @param quarter
     * @param year
     * @param type
     */
    public ReportQuarterlyOrderGenerator(String branch, String quarter, String year, String type) {
        super(branch, quarter, year, type);
        orderColumns.add("Product No.");
        orderColumns.add("Product Name");
        int from = Integer.valueOf(getQuarters().get(quarter).substring(0,2));
        int to =  Integer.valueOf(getQuarters().get(quarter).substring(3));
        for (int i = from; i<=to; i++){
            String month = String.valueOf(Month.of(i)).toLowerCase(Locale.ROOT);
            month = month.substring(0, 1).toUpperCase() + month.substring(1);
            orderColumns.add("Quantity\n" + month);
        }
        orderColumns.add("Total");
    }

    /**
     * Generate quarterly revenue report, fill it and saves it in DB
     * @param branch
     */
    @Override
    public void generate(String branch) {
        try{
            generateReportTitle();
            ArrayList<Object> ordersReportDataFromDB;
            ordersReportDataFromDB = (ReportController.extractOrderInfoForReport(branch,quarters.get(quarter).substring(0,2),quarters.get(quarter).substring(3) ,year));

            if (ordersReportDataFromDB.isEmpty() || ordersReportDataFromDB.size() == 0) {
                noDataForReport(title);
            } else {
                generateColumns(getOrderColumns());
                fillColumns(ordersReportDataFromDB);
                endOfReport();
            }

            closeDocument(ReportType.QUARTERLY_ORDER_REPORT);
        }
        catch (DocumentException e) {
            e.printStackTrace();
        }
    }



    /**
     * Fills the table with given data
     * @param values
     * @throws DocumentException
     */
    @Override
    public void fillColumns(ArrayList<Object> values) throws DocumentException {
        float colSize = 600f;
        float[] columnWidth = new float[orderColumns.size()];
        int max = 0, productSum = 0, quantitySum = 0;
        int totalIndex = orderColumns.indexOf("Total") + 1;
        String mostSold = null;
        for (int i = 0; i < columnWidth.length; i++) {
            columnWidth[i] = colSize;
        }

        PdfPTable table = new PdfPTable(columnWidth);       //product_id (int), name(String), quantity(Arraylist<Integer>, Sum(int))
        if (!values.isEmpty()){
            for (int i = 0, k = 1; i < values.size(); i++, k++) {
                PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(values.get(i)), FontFactory.getFont(HELVETICA, 11, BaseColor.BLACK)));
                setCell(cell);
                table.addCell(cell);

                if (k % totalIndex == 0){
                    int totalProduct = (int)values.get(i);
                    if((int)values.get(i) > max){
                        max = (int)values.get(i);
                        mostSold = values.get(i-4).toString();
                    }
                    productSum += 1;
                    quantitySum += (int)values.get(i);
                }
            }
        }
        this.document.add(table);
        reportSummery.add(mostSold);
        reportSummery.add(String.valueOf(max));
        reportSummery.add(String.valueOf(quantitySum));
        reportSummery.add(String.valueOf(productSum));
    }

    /**
     * Add table to report with th columns titles
     * @param columns
     * @throws DocumentException
     */
    @Override
    public void generateColumns(ArrayList<String> columns) throws DocumentException {

        float colSize = 830 / columns.size();
        float[] columnWidth = new float[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            columnWidth[i] = colSize;
        }
        PdfPTable table = new PdfPTable(columnWidth);

        for (String column : columns) {
            PdfPCell cell = new PdfPCell(new Phrase(column, FontFactory.getFont(HELVETICA_BOLD, 12, BaseColor.BLACK)));
            setCell(cell);
            table.addCell(cell);
        }
        this.document.add(table);
    }

    /**
     * Adds summery lines to the report
     * @throws DocumentException
     */
    @Override
    public void endOfReport() throws DocumentException {
        float colSize = 600f;
        float[] columnWidth = {colSize};

        PdfPTable table = new PdfPTable(columnWidth);
        PdfPCell spaceCell = new PdfPCell(new Phrase("\n\n\n\n\n"));
        PdfPCell mustPurchasedCell = new PdfPCell(new Phrase("Most purchased: " + reportSummery.get(0) +" "+ reportSummery.get(1) + " times", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));
        PdfPCell totalCell = new PdfPCell(new Phrase("Total purchased:  "+ reportSummery.get(2) + " products (from inventory of " +reportSummery.get(3)+" products)", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));
        setCell(mustPurchasedCell);
        setCell(totalCell);
        setCell(spaceCell);

        table.addCell(spaceCell);
        table.addCell(mustPurchasedCell);
        table.addCell(totalCell);

        document.add(table);
    }
    @Override
    public void addHistogram(Object orderData, float x, float y) {}
}