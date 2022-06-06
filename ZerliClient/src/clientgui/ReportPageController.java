package clientgui;

import client.Client;
import communication.Message;
import communication.MessageFromServer;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;


public class ReportPageController implements Initializable {
    public static ArrayList<String> reportData = new ArrayList<>();

    @FXML
    private VBox vboxReport;

    /**
     * transform the report to image in order to show on the screen
     */
    public ArrayList<ImageView>  prepareReport() {
        ArrayList<ImageView> reportImages = new ArrayList<>();
        Message message;
        File pdfFile;
        ImageView iv = new ImageView();

        Client.reportController.viewReport(reportData);
        message = Client.reportController.getService().getResponse();
        SerialBlob blob = (SerialBlob) message.getData();
        if (message.getAnswer() == MessageFromServer.REPORT_VIEW_FAIL) {
            //
        } else {
            try {
                pdfFile = File.createTempFile("zerli", "report.pdf");
                pdfFile.deleteOnExit();
                FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
                byte b[] = blob.getBytes(1, (int) blob.length());
                fileOutputStream.write(b);
                fileOutputStream.close();

                PDDocument document = PDDocument.load(pdfFile.getAbsoluteFile());
                ArrayList<PDPage> pdPages = (ArrayList<PDPage>) document.getDocumentCatalog().getAllPages();
                for (PDPage pdPage : pdPages)
                {
                    BufferedImage bim = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB, 200);
                    Image image = SwingFXUtils.toFXImage(bim, null );

                    iv.setImage(image);
                    reportImages.add(iv);
                    PDResources resources = pdPage.getResources();
                    Map images = resources.getImages();
                    if( images != null )
                    {
                        Iterator imageIter = images.keySet().iterator();
                        while( imageIter.hasNext() )
                        {
                            String key = (String)imageIter.next();
                            PDXObjectImage image2 = (PDXObjectImage)images.get( key );
                            String name = "zerli.jpg";
                            image2.write2file( name );
                            iv.setImage(image);
                            reportImages.add(iv);
                        }
                    }
                }
                document.close();
            } catch (IOException | SerialException e) {
                throw new RuntimeException(e);
            }
        }
        return reportImages;
    }


    /**
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * <tt>null</tt> if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or <tt>null</tt> if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<ImageView> reportImages = prepareReport();
        for ( ImageView img: reportImages) {
            img.setFitWidth(860);
            img.setFitHeight(980);
            vboxReport.getChildren().add(img);
        }
        reportData.clear();

    }
}