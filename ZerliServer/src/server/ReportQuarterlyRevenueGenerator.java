package server;

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
import util.ReportType;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ReportQuarterlyRevenueGenerator extends AbstractQuarterlyReportGenerator {

    public ReportQuarterlyRevenueGenerator(String branch, String quarter, String year, String type) {
        super(branch, quarter, year, type);
        ReportController.setConnection(Server.databaseController.getConnection());
    }

    /**
     * Generate quarterly revenue report, fill it and saves it in DB
     * @param branch
     */
    @Override
    public void generate(String branch) {
        try{
            generateReportTitle();
            HashMap<String,Float> revenueReportDataFromDB = ReportController.extractRevenueInfoForReportQuarterly(branch,quarters.get(quarter),year);

            if (isDataEmpty(revenueReportDataFromDB)) {
                noDataForReport(title);
            } else {
                addHistogram(revenueReportDataFromDB,45f,220f);
                endOfReport();
            }

            closeDocument(ReportType.QUARTERLY_REVENUE_REPORT);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if there's no data to fill in the report
     * @param revenueReportDataFromDB
     * @return true - if empty, false - if not
     */
    private boolean isDataEmpty(HashMap<String, Float> revenueReportDataFromDB) {
        int sum = 0;
        for (Map.Entry<String, Float> entry : revenueReportDataFromDB.entrySet()) {
            sum += entry.getValue();
        }
        return sum == 0;
    }

    /**
     * Adds histogram with data given from DB
     * @param revenueData
     * @param x
     * @param y
     */
    @Override
    public void addHistogram(Object revenueData, float x, float y) {
        HashMap<String,Float> revenueDataForHistogram = (HashMap<String, Float>) revenueData;

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
     * @param revenueDataForHistogram
     * @return
     */
    public JFreeChart generateBarChart(HashMap<String,Float> revenueDataForHistogram) {

        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        int weeks = 0, i = 0;
        float max = 0, sum = 0;
        String bestWeek= null;
        HashMap<Integer,Float> newVals = new HashMap<>();

        for (Map.Entry<String, Float> entry : revenueDataForHistogram.entrySet()) {      //i- is declining to reverse the months on the chart.
            newVals.put(Integer.valueOf(entry.getKey()), entry.getValue());
        }

        Object[] newValsArr = newVals.keySet().toArray();
        Arrays.sort(newValsArr);

        for(Object key : newValsArr) {
            if(i == 0) {
                i = (int) key;
            }

            while((int) key != i) {
                dataSet.setValue(0, String.valueOf(weeks / 5), weeks + 1 + "");
                i++;
            }

            if(newVals.get(key) > max) {
                max = newVals.get(key);
                bestWeek = String.valueOf(weeks + 1);
            }

            sum += newVals.get(key);
            dataSet.setValue(newVals.get(key) , String.valueOf(weeks/5), weeks+1 +"");
            i++;
            weeks++;
        }

        while (weeks < 15){
            dataSet.setValue(0 , String.valueOf(weeks/5), weeks+1 +"");
            weeks++;
        }
        //for end of report
        this.reportSummery.add(String.valueOf(sum));
        this.reportSummery.add(String.valueOf(max));
        this.reportSummery.add(bestWeek);

        JFreeChart chart = ChartFactory.createBarChart(
                "Revenue amount over the quarter", "Weeks", "Revenue in Shekels",
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
        PdfPCell skipTheHistogramCell = new PdfPCell(new Phrase("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));
        PdfPCell totalRevenueCell = new PdfPCell(new Phrase("Total revenue: " + reportSummery.get(0), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));
        PdfPCell bestMonthCell = new PdfPCell(new Phrase("The most profitable week: " + reportSummery.get(2) + " with " + reportSummery.get(1) +" \u20AA", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));
        setCell(totalRevenueCell);
        setCell(skipTheHistogramCell);
        setCell(bestMonthCell);

        table.addCell(skipTheHistogramCell);
        table.addCell(totalRevenueCell);
        table.addCell(bestMonthCell);

        document.add(table);
    }

    @Override
    public void fillColumns(ArrayList<Object> values) throws DocumentException {}
    @Override
    public void generateColumns(ArrayList<String> columns) throws DocumentException {}

}