package server;

import report.ReportType;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.ArrayList;

import static com.itextpdf.text.pdf.BaseFont.HELVETICA;

public class MonthlyOrderReportGenerator extends MonthlyReportGenerator{
    @Override
    public void addHistogram(Object complaintsData, float x, float y) {}

    private  ArrayList<String> orderColumns = new ArrayList<String>();
    public MonthlyOrderReportGenerator(String branch, String month, String year, String type) {
        super(branch, month,year, type);
        orderColumns.add("Product No.");
        orderColumns.add("Product Name");
        orderColumns.add("Quantity");
    }

    public ArrayList<String> getOrderColumns() {return orderColumns;}

    public void setOrderColumns(ArrayList<String> orderColumns) {this.orderColumns = orderColumns;}
    @Override
    public void generate(String branch) {
        try {
            generateReportTitle();
            generateColumns(getOrderColumns());
            ArrayList<Object> ordersReportDataFromDB = ReportController.extractOrderInfoForReport(branch,month, month,year);
            fillColumns(ordersReportDataFromDB);
            endOfReport();
            closeDocument(ReportType.MONTHLY_ORDER_REPORT);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }


    public ArrayList<String> getReportSummery() {
        return reportSummery;
    }

    public void setReportSummery(ArrayList<String> reportSummery) {
        this.reportSummery = reportSummery;
    }

    public void fillColumns(ArrayList<Object> values) throws DocumentException {
        float colSize = 600f;
        float columnWidth[] = new float[orderColumns.size()];
        int max = 0, productSum = 0, quantitySum = 0;
        int quantityIndex = orderColumns.indexOf("Quantity") + 1;
        String mustSold = null;
        for (int i = 0; i < columnWidth.length; i++) {
            columnWidth[i] = colSize;
        }
        PdfPTable table = new PdfPTable(columnWidth);
        if (!values.isEmpty()){
            for (int i = 0, k = 1; i < values.size(); i++, k++) {
                PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(values.get(i)), FontFactory.getFont(HELVETICA, 11, BaseColor.BLACK)));
                setCell(cell);
                table.addCell(cell);

                if (k % quantityIndex == 0){
                    if((int)values.get(i) > max){
                        max = (int)values.get(i);
                        mustSold = values.get(i-1).toString();
                    }
                    productSum += 1;
                    quantitySum += (int)values.get(i);
                }
            }
        }
        this.document.add(table);
        reportSummery.add(mustSold);
        reportSummery.add(String.valueOf(max));
        reportSummery.add(String.valueOf(quantitySum));
        reportSummery.add(String.valueOf(productSum));

    }




    public void endOfReport() throws DocumentException {
        float colSize = 600f;
        float columnWidth[] = {colSize};

        PdfPTable table = new PdfPTable(columnWidth);
        PdfPCell spaceCell = new PdfPCell(new Phrase("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));
        PdfPCell mustPurchasedCell = new PdfPCell(new Phrase("Most purchased: " + reportSummery.get(0) +" -  "+ reportSummery.get(1), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));
        PdfPCell totalCell = new PdfPCell(new Phrase("Total purchased:  "+ reportSummery.get(2) + " products (from inventory of " +reportSummery.get(3)+" products)", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));
        setCell(mustPurchasedCell);
        setCell(totalCell);
        setCell(spaceCell);

        table.addCell(spaceCell);
        table.addCell(mustPurchasedCell);
        table.addCell(totalCell);

        document.add(table);
    }


}