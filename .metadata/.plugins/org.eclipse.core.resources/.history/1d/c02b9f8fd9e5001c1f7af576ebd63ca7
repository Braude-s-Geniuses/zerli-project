package server;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import util.ReportType;

import java.time.Month;
import java.util.ArrayList;

import static com.itextpdf.text.pdf.BaseFont.HELVETICA;
import static com.itextpdf.text.pdf.BaseFont.HELVETICA_BOLD;
import static com.sun.xml.internal.ws.util.StringUtils.capitalize;

public abstract class AbstractMonthlyReportGenerator extends AbstractReportsGenerator{
    protected String title;
    protected String month;
    protected String year;

    public AbstractMonthlyReportGenerator(String branch, String month, String year, String type) {
        super(branch);
        title = type;
        this.month = month;
        this.year = year;
        fileName = this.title + "_" + this.branch + "_" + year + "-" + month + ".pdf";
    }

    /**
     * Prepares the report title using the report type, date and year
     */
    public void generateReportTitle() {
        try {
            float col = 600f;
            float columnWidth[] = {col};
            PdfPTable table = new PdfPTable(columnWidth);

            PdfPCell cell = new PdfPCell(new Phrase(this.title + " Report\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 40, new BaseColor(119, 56, 90))));
            setCell(cell);
            table.addCell(cell);

            PdfPCell cell2 = new PdfPCell(new Phrase("Branch: " + this.branch, FontFactory.getFont(HELVETICA, 16, new BaseColor(119, 56, 90))));
            setCell(cell2);
            table.addCell(cell2);
            Month month = Month.of(Integer.valueOf(this.month));
            String monthStr = month.toString();
            monthStr = monthStr.toLowerCase();
            monthStr = capitalize(monthStr);
            PdfPCell cell3 = new PdfPCell(new Phrase(monthStr + " - " + year + "\n\n", FontFactory.getFont(HELVETICA, 16, new BaseColor(119, 56, 90))));
            setCell(cell3);
            table.addCell(cell3);

            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a table to the report given list of its column
     * @param columns the columns of the wanted report
     * @throws DocumentException
     */
    public void generateColumns(ArrayList<String> columns) throws DocumentException {

        float colSize = 830 / columns.size();
        float columnWidth[] = new float[columns.size()];
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
     * Closes the report and saves it in DB
     * @param reportType - the report type as enum to save in DB
     */
    public void closeDocument(ReportType reportType){
        document.close();
        writer.close();
        ReportController.saveReport(byteArrayOutputStream,fileName, reportType, month,year);
    }

}