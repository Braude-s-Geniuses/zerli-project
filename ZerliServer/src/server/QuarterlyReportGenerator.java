package server;

import report.ReportType;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.time.Month;
import java.util.HashMap;

import static com.itextpdf.text.pdf.BaseFont.HELVETICA;
import static com.sun.xml.internal.ws.util.StringUtils.capitalize;

public abstract class QuarterlyReportGenerator extends AbstractReportsGenerator {

    protected String title;
    protected String quarter;
    protected String year;
    final protected HashMap<String,String> quarters = new HashMap<>();{
        {
            quarters.put("01","01-03");
            quarters.put("02","04-06");
            quarters.put("03","07-09");
            quarters.put("04","10-12");
        }
    }

    public QuarterlyReportGenerator(String branch, String quarter, String year, String type) {
        super(branch);
        this.title = type;
        this.quarter = quarter;
        this.year = year;
        fileName = this.title + "_" + this.branch + "_" + year + "-quarter_" + quarter + ".pdf";
    }

    public HashMap<String, String> getQuarters() {
        return quarters;
    }
    public void generateReportTitle() {
        try {
            //Add content to the document.

            float col = 600f;
            float columnWidth[] = {col};
            PdfPTable table = new PdfPTable(columnWidth);

            PdfPCell titleCell = new PdfPCell(new Phrase("Quarterly " + this.title + " Report\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 40, new BaseColor(119, 56, 90))));
            setCell(titleCell);
            table.addCell(titleCell);

            PdfPCell branchCell = new PdfPCell(new Phrase("Branch: " + this.branch, FontFactory.getFont(HELVETICA, 20, new BaseColor(119, 56, 90))));
            setCell(branchCell);
            table.addCell(branchCell);

            Month fromMonth = Month.of(Integer.valueOf(getQuarters().get(quarter).substring(0,2)));
            String fromMonthStr = fromMonth.toString();
            fromMonthStr = fromMonthStr.toLowerCase();
            fromMonthStr = capitalize(fromMonthStr);

            Month toMonth = Month.of(Integer.valueOf(getQuarters().get(quarter).substring(3)));
            String toMonthStr = toMonth.toString();
            toMonthStr = toMonthStr.toLowerCase();
            toMonthStr = capitalize(toMonthStr);

            PdfPCell cell3 = new PdfPCell(new Phrase("Months  "  +fromMonthStr + " - " + toMonthStr + ",  " + year + "\n\n", FontFactory.getFont(HELVETICA, 20, new BaseColor(119, 56, 90))));
            setCell(cell3);
            table.addCell(cell3);

            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void closeDocument(ReportType reportType){
        document.close();
        writer.close();
        ReportController.saveReport(byteArrayOutputStream,fileName, reportType, quarter,year);
    }
}