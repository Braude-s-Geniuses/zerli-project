package server;

import util.ReportType;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.ArrayList;

import static com.itextpdf.text.pdf.BaseFont.HELVETICA;

public class ReportMonthlyRevenueGenerator extends AbstractMonthlyReportGenerator {

    private ArrayList<String> revenueColumns =new ArrayList<String>();

    public ReportMonthlyRevenueGenerator(String branch, String month, String year, String type) {
        super(branch, month,year, type);
        revenueColumns.add("Order No.");
        revenueColumns.add("Date");
        revenueColumns.add("Price");
    }

    public ArrayList<String> getRevenueColumns() { return revenueColumns; }

    /**
     * Gives the option to add/ remove column
     * @param revenueColumns
     */
    public void setRevenueColumns(ArrayList<String> revenueColumns) { this.revenueColumns = revenueColumns;}

    /**
     * Generates monthly revenue report, fills it and saves in DB
     * @param branch
     */
    @Override
    public void generate(String branch) {
        try {
            generateReportTitle();
            ArrayList<Object> revenueReportDataFromDB = ReportController.extractRevenueInfoForReport(branch,month, year);
            if (revenueReportDataFromDB.isEmpty()) {
                noDataForReport(title);
            } else {
                generateColumns(revenueColumns);
                fillColumns(revenueReportDataFromDB);
                endOfReport();
            }
            closeDocument(ReportType.MONTHLY_REVENUE_REPORT);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fills the table in the report with given data
     * @param values - data to fill from DB
     * @throws DocumentException
     */
    @Override
    public void fillColumns(ArrayList<Object> values) throws DocumentException {
        float colSize = 600f;
        float[] columnWidth = new float[revenueColumns.size()];
        float productSum = 0;
        int priceIndex = revenueColumns.indexOf("Price") +1;
        ArrayList<String> dataSumOfReport = new ArrayList<>();
        for (int i = 0; i < columnWidth.length; i++) {
            columnWidth[i] = colSize;
        }
        PdfPTable table = new PdfPTable(columnWidth);
        if (!values.isEmpty()) {
            for (int i = 0, k = 1; i < values.size(); i++, k++) {
                PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(values.get(i)), FontFactory.getFont(HELVETICA, 11, BaseColor.BLACK)));
                setCell(cell);
                table.addCell(cell);

                if (k % priceIndex == 0) {
                    productSum += (Integer)values.get(i);
                }
            }
        }
        this.document.add(table);
        reportSummery.add(String.valueOf(productSum));
    }

    /**
     * Writes summery lines to the report
     * @throws DocumentException
     */
    @Override
    public void endOfReport() throws DocumentException {
        float colSize = 600f;
        float[] columnWidth = {colSize};

        PdfPTable table = new PdfPTable(columnWidth);
        PdfPCell cell3 = new PdfPCell(new Phrase("\n\n\n\n\n"));
        PdfPCell cell = new PdfPCell(new Phrase("Total revenue: " + reportSummery.get(0), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));
        setCell(cell);
        setCell(cell3);

        table.addCell(cell3);
        table.addCell(cell);

        document.add(table);
    }

    @Override
    public void addHistogram(Object complaintsData, float x, float y) {}
}