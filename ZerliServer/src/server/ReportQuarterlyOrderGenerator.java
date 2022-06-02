package server;

import report.ReportType;
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
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.itextpdf.text.pdf.BaseFont.HELVETICA;

public class ReportQuarterlyOrderGenerator extends AbstractQuarterlyReportGenerator {


    private final ArrayList<String> orderColumns = new ArrayList<String>();

    public ReportQuarterlyOrderGenerator(String branch, String quarter, String year, String type) {
        super(branch, quarter, year, type);
        orderColumns.add("Product No.");
        orderColumns.add("Product Name");
        int from = Integer.valueOf(getQuarters().get(quarter).substring(0,2));
        int to =  Integer.valueOf(getQuarters().get(quarter).substring(3));
        for (int i = from; i<=to; i++){
            String month = String.valueOf(Month.of(i)).toLowerCase(Locale.ROOT);
            orderColumns.add("Quantity\n" + month);
        }
        orderColumns.add("Total");
    }

    public ArrayList<String> getOrderColumns() {return orderColumns;}

    @Override
    public void generate(String branch) {
        try{
            generateReportTitle();

            ArrayList<Object> ordersReportDataFromDB;
            ordersReportDataFromDB = (ReportController.extractOrderInfoForReport(branch,quarters.get(quarter).substring(0,2),quarters.get(quarter).substring(3) ,year));

            if (ordersReportDataFromDB.isEmpty()) {
                noDataForReport();
            } else {
                generateColumns(getOrderColumns());
                fillColumns(ordersReportDataFromDB);
                endOfReport();
            }

            closeDocument(ReportType.QUARTERLY_ORDER_REPORT);
        }
        catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fillColumns(ArrayList<Object> values) throws DocumentException {
        float colSize = 600f;
        float[] columnWidth = new float[orderColumns.size()];
        int max = 0, productSum = 0, quantitySum = 0;
        int totalIndex = orderColumns.indexOf("Total") + 1;
        String mostSold = null;
        for (int i = 0; i < columnWidth.length; i++) {
            columnWidth[i] = colSize;
        }

        PdfPTable table = new PdfPTable(columnWidth);       //product_id (int), name(String), quantity(Arraylist<Integer>, Sum(int))
        if (!values.isEmpty()){
            for (int i = 0, k = 1; i < values.size(); i++, k++) {
                PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(values.get(i)), FontFactory.getFont(HELVETICA, 11, BaseColor.BLACK)));
                setCell(cell);
                table.addCell(cell);

                if (k % totalIndex == 0){
                    int totalProduct = (int)values.get(i);
                    if((int)values.get(i) > max){
                        max = (int)values.get(i);
                        mostSold = values.get(i-4).toString();
                    }
                    productSum += 1;
                    quantitySum += (int)values.get(i);
                }
            }
        }
        this.document.add(table);
        reportSummery.add(mostSold);
        reportSummery.add(String.valueOf(max));
        reportSummery.add(String.valueOf(quantitySum));
        reportSummery.add(String.valueOf(productSum));
    }
    //
    @Override
    public void generateColumns(ArrayList<String> columns) throws DocumentException {

        float colSizeforspace = 600f;
        float[] columnWidthfoespace = {colSizeforspace};

        float colSize = 830 / columns.size();
        float[] columnWidth = new float[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            columnWidth[i] = colSize;
        }
        PdfPTable table = new PdfPTable(columnWidth);

        for (String column : columns) {
            PdfPCell cell = new PdfPCell(new Phrase(column, FontFactory.getFont(HELVETICA, 16, BaseColor.BLACK)));
            setCell(cell);
            table.addCell(cell);
        }
        this.document.add(table);
    }

    @Override
    public void addHistogram(Object orderData, float x, float y) {
        HashMap<String,Float> revenueDataForHistogram = (HashMap<String, Float>) orderData;

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
        float[] columnWidth = {colSize};

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