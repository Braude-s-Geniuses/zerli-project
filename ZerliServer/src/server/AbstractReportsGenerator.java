package server;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import util.ReportType;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public abstract class AbstractReportsGenerator {

    protected  ArrayList<String> reportSummery = new ArrayList<>();
    protected Document document;
    protected PdfWriter writer = null;
    protected ByteArrayOutputStream byteArrayOutputStream;
    protected String branch;
    protected String fileName;
    public abstract void generate(String branch);
    public abstract void generateReportTitle() ;
    public abstract void fillColumns(ArrayList<Object> values) throws DocumentException;
    public abstract void generateColumns(ArrayList<String> columns) throws DocumentException;
    public abstract void addHistogram(Object complaintsData,float x, float y);
    public abstract void endOfReport() throws DocumentException;
    public abstract void closeDocument(ReportType reportType);

    /**
     * Creates a new document and leaves it open to write
     * @param branch
     */
    public AbstractReportsGenerator(String branch) {
        this.branch = branch;
        this.document = new Document(PageSize.A4);
        this.byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            writer = PdfWriter.getInstance(document, byteArrayOutputStream);
            this.document.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the cells in all the reports in the same way
     * @param cell
     */
    protected void setCell(PdfPCell cell) {
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        cell.setBorder(0);
    }

    /**
     * Report with no data gets an appropriate message
     * @param type
     */
    protected void noDataForReport(String type){
        PdfPCell cell = null;
        try {
            float col = 600f;
            float[] columnWidth = {col};
            PdfPTable table = new PdfPTable(columnWidth);
            switch (type){
                case "Order":
                case "Revenue":
                    cell = new PdfPCell(new Phrase("\n\nNo placed orders in the specified time", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, new BaseColor(119, 56, 90))));
                    break;
                case "Complaints":
                    cell = new PdfPCell(new Phrase("\n\nNo complaints has been made in the specified time", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, new BaseColor(119, 56, 90))));
                    break;
            }

            setCell(cell);
            table.addCell(cell);
            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}