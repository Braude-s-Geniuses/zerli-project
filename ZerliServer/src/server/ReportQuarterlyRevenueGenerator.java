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
import report.ReportType;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportQuarterlyRevenueGenerator extends AbstractQuarterlyReportGenerator {

    public ReportQuarterlyRevenueGenerator(String branch, String quarter, String year, String type) {
        super(branch, quarter, year, type);
    }

    @Override
    public void generate(String branch) {
        try{
            generateReportTitle();
            HashMap<String,Float> revenueReportDataFromDB = ReportController.extractRevenueInfoForReportQuarterly(branch,quarters.get(quarter),year);

            if (isDataEmpty(revenueReportDataFromDB)) {
                noDataForReport();
            } else {
                addHistogram(revenueReportDataFromDB,45f,230f);
                endOfReport();
            }

            closeDocument(ReportType.QUARTERLY_REVENUE_REPORT);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private boolean isDataEmpty(HashMap<String, Float> ordersReportDataFromDB) {
        int sum = 0;
        for (Map.Entry<String, Float> entry : ordersReportDataFromDB.entrySet()) {
            sum += entry.getValue();
        }
        return sum == 0 ? true : false;
    }

    @Override
    public void fillColumns(ArrayList<Object> values) throws DocumentException {

    }
    @Override
    public void generateColumns(ArrayList<String> columns) throws DocumentException {
    }

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

    public JFreeChart generateBarChart(HashMap<String,Float> revenueDataForHistogram) {

        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        float max = 0, sum = 0;
        String month= null;
        for (Map.Entry<String, Float> entry : revenueDataForHistogram.entrySet()){      //i- is declining to reverse the months on the chart.
            if(entry.getValue() > max) {
                max = entry.getValue();
                month = entry.getKey();
            }
            sum += entry.getValue();
            dataSet.setValue(entry.getValue() , "Months", entry.getKey());
        }
        //for end of report
        this.reportSummery.add(String.valueOf(sum));
        this.reportSummery.add(String.valueOf(max));
        this.reportSummery.add(month);

        JFreeChart chart = ChartFactory.createBarChart(
                "Revenue amount over the quarter", "Months", "Revenue in Shekels",
                dataSet, PlotOrientation.VERTICAL, false, true, false);
        CategoryPlot chartPlot = (CategoryPlot)chart.getPlot();
        chart.getPlot().setBackgroundPaint( new Color(228,215,222) );
        ((BarRenderer)chartPlot.getRenderer()).setBarPainter(new StandardBarPainter());

        BarRenderer r = (BarRenderer)chart.getCategoryPlot().getRenderer();
        r.setSeriesPaint(0,new Color(119,56,90));

        return chart;
    }
    @Override
    public void endOfReport() throws DocumentException {
        float colSize = 600f;
        float columnWidth[] = {colSize};

        PdfPTable table = new PdfPTable(columnWidth);
        PdfPCell skipTheHistogramCell = new PdfPCell(new Phrase("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));
        PdfPCell totalRevenueCell = new PdfPCell(new Phrase("Total revenue: " + reportSummery.get(0), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));
        PdfPCell bestMonthCell = new PdfPCell(new Phrase("The most profitable month: " + reportSummery.get(2) + " with " + reportSummery.get(1) +" \u20AA", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));
        setCell(totalRevenueCell);
        setCell(skipTheHistogramCell);
        setCell(bestMonthCell);

        table.addCell(skipTheHistogramCell);
        table.addCell(totalRevenueCell);
        table.addCell(bestMonthCell);

        document.add(table);
    }

}