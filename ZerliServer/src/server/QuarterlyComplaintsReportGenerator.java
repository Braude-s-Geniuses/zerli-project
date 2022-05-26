package server;

import report.ReportType;
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

public class QuarterlyComplaintsReportGenerator extends QuarterlyReportGenerator{

    public QuarterlyComplaintsReportGenerator(String branch, String quarter, String year, String type) {
        super(branch, quarter, year, type);
    }

    @Override
    public void generate(String branch) {
        try {
            ArrayList<Integer> ordersReportDataFromDB;
            generateReportTitle();
            ordersReportDataFromDB = ReportController.extractComplaintsInfoForReport(branch, quarters.get(quarter).substring(0,2), quarters.get(quarter).substring(3), year);
            addHistogram(ordersReportDataFromDB, 45f, 220f);
            endOfReport();
            closeDocument(ReportType.QUARTERLY_COMPLAINTS_REPORT);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //Gal, if you got up to this point it means you are right TOP LEVEL Program engineer.
        // now, the next stage in your journey will be to understand my messed up code. good luck//
    }

    @Override
    public void fillColumns(ArrayList<Object> values) throws DocumentException {

    }

    @Override
    public void generateColumns(ArrayList<String> columns) throws DocumentException {

    }

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


    public JFreeChart generateBarChart(ArrayList<Integer> complaintsData) {
        int monthsForYAxis = Integer.valueOf(quarters.get(quarter).substring(0,2));
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        int max = 0, sum = 0;
        String month= null;

        for (int i = 0; i < 3 ; i++){
            if(complaintsData.get(i) > max) {
                max = complaintsData.get(i);
                month = String.valueOf(monthsForYAxis);
            }
            sum += complaintsData.get(i);
            dataSet.setValue(complaintsData.get(i), "Month",  Month.of(monthsForYAxis+i));
        }
        this.reportSummery.add(String.valueOf(sum));
        this.reportSummery.add(String.valueOf(max));
        this.reportSummery.add(month);
        JFreeChart chart = ChartFactory.createBarChart(
                "Complaints amount over the quarter", "Month", "Complaints in Units",
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

    }
}