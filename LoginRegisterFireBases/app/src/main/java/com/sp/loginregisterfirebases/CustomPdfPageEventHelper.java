package com.sp.loginregisterfirebases;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomPdfPageEventHelper extends PdfPageEventHelper {
    private Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
    private Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
    private String headerText = "CarbonAware";
    private String footerText = "For more information on how we calculate your footprint, please visit our website: Carbonaware.com";

    @Override
    public void onEndPage(PdfWriter writer, com.itextpdf.text.Document document) {
        super.onEndPage(writer, document);

        // Get the current date and time
        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        // Header
        PdfContentByte canvas = writer.getDirectContent();
        Phrase header = new Phrase(headerText, headerFont);
        float x = (document.right() - document.left()) / 2 + document.leftMargin();
        float y = document.top() + 10;
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, header, x, y, 0);

        // Footer
        Phrase footer = new Phrase(footerText, footerFont);
        float footerX = (document.right() - document.left()) / 2 + document.leftMargin();
        float footerY = document.bottom() - 10;
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, footer, footerX, footerY, 0);
    }
}
