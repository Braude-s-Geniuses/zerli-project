package server;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import report.ReportType;

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

    public String getFileName() {
        return fileName;
    }

    public Document getDocument() {
        return document;
    }

    public AbstractReportsGenerator(String branch) {
        this.branch = branch;
        this.document = new Document(PageSize.A4);
        this.byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            writer = PdfWriter.getInstance(document, byteArrayOutputStream);
            //writer = PdfWriter.getInstance(document, new FileOutputStream("TestPdf.pdf"));
            this.document.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setCell(PdfPCell cell) {
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        cell.setBorder(0);
    }

}