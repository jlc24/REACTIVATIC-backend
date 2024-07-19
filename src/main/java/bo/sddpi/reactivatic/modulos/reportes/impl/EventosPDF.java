package bo.sddpi.reactivatic.modulos.reportes.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class EventosPDF extends PdfPageEventHelper {

    protected float startpos = -1;
    protected boolean title = true;

    public void setTitle(boolean title) {
        this.title = title;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {

        DateTimeFormatter formatofecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatohora = DateTimeFormatter.ofPattern("HH.mm:ss");

        Rectangle pagesize = document.getPageSize();

        PdfContentByte cb = writer.getDirectContent();

        BaseFont helvetica = null;

        try {
            helvetica = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        cb.setFontAndSize(helvetica, 8);

        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT,
                new Phrase("Página "+String.valueOf(writer.getPageNumber())), pagesize.getRight() - 20, pagesize.getBottom() + 10, 0);

        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT,
                new Phrase(LocalDate.now().format(formatofecha)+ " - " + LocalTime.now().format(formatohora)), pagesize.getLeft()+ 20, pagesize.getBottom() + 10, 0);
    }

}
