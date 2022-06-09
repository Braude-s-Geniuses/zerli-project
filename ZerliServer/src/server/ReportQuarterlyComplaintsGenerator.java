package server;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import util.ReportType;
import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.time.Month;
import java.util.ArrayList;

public class ReportQuarterlyComplaintsGenerator extends AbstractQuarterlyReportGenerator {

    public ReportQuarterlyComplaintsGenerator(String branch, String quarter, String year, String type) {
        super(branch, quarter, year, type);
    }
    /**
     * Generate quarterly complaints report, fill it and saves it in DB
     * @param branch
     */
    @Override
    public void generate(String branch) {
        try {
            ArrayList<Integer> complaintsReportDataFromDB;
            generateReportTitle();
            complaintsReportDataFromDB = ReportController.extractComplaintsInfoForReport(branch, quarters.get(quarter).substring(0,2), quarters.get(quarter).substring(3), year);

            if (isDataEmpty(complaintsReportDataFromDB)) {
                noDataForReport(title);
                closeDocument(ReportType.QUARTERLY_COMPLAINTS_REPORT);
            } else {
                addHistogram(complaintsReportDataFromDB, 45f, 220f);
                endOfReport();
                closeDocument(ReportType.QUARTERLY_COMPLAINTS_REPORT);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    /**
     * Check if there's no data to fill in the report
     * @param ordersReportDataFromDB
     * @return true - if empty, false - if not
     */
    private boolean isDataEmpty(ArrayList<Integer> ordersReportDataFromDB) {
        int sum = 0;
        for (int i : ordersReportDataFromDB) {
            sum += i;
        }
        return sum == 0;
    }

    /**
     * Adds histogram with data given from DB
     * @param orderData
     * @param x
     * @param y
     */
    @Override
    public void addHistogram(Object orderData, float x, float y) {
        ArrayList<Integer> revenueDataForHistogram = (ArrayList<Integer>) orderData;

        JFreeChart chart = generateBarChart(revenueDataForHistogram);
        PdfContentByte contentByte = writer.getDirectContent();
        PdfTemplate template = contentByte.createTemplate(500, 400);
        Graphics2D graphics2d = new PdfGraphics2D(template, 500, 400);
        Rectangle2D rectangle2d = new Rectangle2D.Double(0,0, 500, 400);
        chart.draw(graphics2d, rectangle2d);
        graphics2d.dispose();
        contentByte.addTemplate(template,x,y);
    }

    /**
     * Filling the bar chart in the data given
     * @param complaintsData
     * @return
     */
    public JFreeChart generateBarChart(ArrayList<Integer> complaintsData) {
        int monthsForYAxis = Integer.valueOf(quarters.get(quarter).substring(0,2));
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        int max = 0, sum = 0;
        String month= null;
        String week= null;

        for (int i = 0; i < 15 ; i++){
            if(complaintsData.get(i) > max) {
                max = complaintsData.get(i);
                week = String.valueOf((i+1));
            }
            sum += complaintsData.get(i);
            dataSet.addValue(complaintsData.get(i), String.valueOf(i/5), ""+ (i+1));
        }

        this.reportSummery.add(String.valueOf(sum));
        this.reportSummery.add(String.valueOf(max));
        this.reportSummery.add(week);
        JFreeChart chart = ChartFactory.createBarChart(
                "Complaints amount over the quarter", "Week", "Complaints in Units",
                dataSet, PlotOrientation.VERTICAL, false, true, false);
        CategoryPlot chartPlot = (CategoryPlot)chart.getPlot();
        chart.getPlot().setBackgroundPaint( new Color(228,215,222) );
        ((BarRenderer)chartPlot.getRenderer()).setBarPainter(new StandardBarPainter());

        BarRenderer r = (BarRenderer)chart.getCategoryPlot().getRenderer();
        r.setSeriesPaint(0,new Color(119,56,90));

        return chart;
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
        PdfPCell cell3 = new PdfPCell(new Phrase("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));
        PdfPCell cell = new PdfPCell(new Phrase("Total complaints: " + reportSummery.get(0) , FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));
        PdfPCell cell2 = new PdfPCell(new Phrase("Week number " + reportSummery.get(2) + " was with most complaints this quarter: " + reportSummery.get(1), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));
        setCell(cell);
        setCell(cell3);
        setCell(cell2);

        table.addCell(cell3);
        table.addCell(cell);
        table.addCell(cell2);
        document.add(table);
    }

    @Override
    public void fillColumns(ArrayList<Object> values) throws DocumentException {}

    @Override
    public void generateColumns(ArrayList<String> columns) throws DocumentException {}
}