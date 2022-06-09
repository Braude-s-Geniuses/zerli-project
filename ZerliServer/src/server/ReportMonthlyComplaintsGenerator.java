package server;

import util.ReportType;
import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
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
import java.util.ArrayList;

public class ReportMonthlyComplaintsGenerator extends AbstractMonthlyReportGenerator {

    public ReportMonthlyComplaintsGenerator(String branch, String month, String year, String type) {
        super(branch, month,year, type);
    }
    /**
     * Generate monthly complaints report, filling it and saves it in DB
     * @param branch
     */
    @Override
    public void generate(String branch) {
        ArrayList<Integer> complaintsReportDataFromDB;
        try {
            generateReportTitle();
            complaintsReportDataFromDB = ReportController.extractComplaintsInfoForReport(branch,month,month ,year);
            if (isDataEmpty(complaintsReportDataFromDB)) {
                noDataForReport(title);
                closeDocument(ReportType.MONTHLY_COMPLAINTS_REPORT);
            } else {
                addHistogram(complaintsReportDataFromDB, 45f, 220f);
                endOfReport();
                closeDocument(ReportType.MONTHLY_COMPLAINTS_REPORT);
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
     * @param complaintsData
     * @param x
     * @param y
     */
    public void addHistogram(Object complaintsData, float x, float y) {
        ArrayList<Integer> complaintsAmount = (ArrayList<Integer>) complaintsData;
        JFreeChart chart = generateBarChart(complaintsAmount);
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

        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        int max = 0, sum = 0;
        String week= null;
        for (int i = 0; i < 5 ; i++){
            if(complaintsData.get(i) > max) {
                max = complaintsData.get(i);
                week = String.valueOf((i+1));
            }
            sum += complaintsData.get(i);
            dataSet.setValue(complaintsData.get(i), "Week", "week" + (i+1));
        }
        this.reportSummery.add(String.valueOf(sum));
        this.reportSummery.add(String.valueOf(max));
        this.reportSummery.add(week);
        JFreeChart chart = ChartFactory.createBarChart(
                "Complaints amount over the month", "Week", "Complaints in Units",
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
        PdfPCell cell = new PdfPCell(new Phrase("Total Complaints: " + reportSummery.get(0) , FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));
        PdfPCell cell2 = new PdfPCell(new Phrase("Week number " + reportSummery.get(2) + " Was With Most Complaints This Month: " + reportSummery.get(1), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));
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
}