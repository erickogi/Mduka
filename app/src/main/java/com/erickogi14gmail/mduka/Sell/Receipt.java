package com.erickogi14gmail.mduka.Sell;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.erickogi14gmail.mduka.Db.StockItemsPojo;
import com.erickogi14gmail.mduka.R;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Eric on 9/6/2017.
 */

public class Receipt {
    File myFile = null;
    String fle;
    private String LOG_TAG = "pdflog";
    private Context context;

    public Receipt(Context context) {
        this.context = context;
    }

    public File re(ArrayList<StockItemsPojo> items, double total, double cashIn
            , String orgDetails, String officer, String rn) throws DocumentException, FileNotFoundException {

        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "Receipt");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i(LOG_TAG, "Pdf Directory created");
        }
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        myFile = new File(pdfFolder + timeStamp + ".pdf");
        OutputStream output = new FileOutputStream(myFile);
        BaseColor baseColor = new BaseColor(84, 141, 212);
        Rectangle pageSize = new Rectangle(PageSize.A5);
        // pageSize.setBackgroundColor(baseColor);
        Document document = new Document(pageSize);
        PdfWriter writer = PdfWriter.getInstance(document, output);
        document.open();


        //PdfWriter.getInstance(document, output);
        HeaderFooterPageEvent event = new HeaderFooterPageEvent(officer, rn, date);
        writer.setPageEvent(event);
        PdfContentByte canvas = writer.getDirectContentUnder();
        // Image image=Image.
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bgi);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Image img = null;
        try {
            img = Image.getInstance(stream.toByteArray());
            img.setAbsolutePosition(0, 0);
            img.scaleAbsolute(PageSize.A5);
        } catch (BadElementException m) {

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        canvas.saveState();
        PdfGState state = new PdfGState();
        state.setFillOpacity(0.1f);

        canvas.addImage(img);
        canvas.restoreState();


        PdfPTable header = new PdfPTable(3);

//                //  tbl.setWidthPercentage(100);
//
        // header.setTotalWidth(575);
        // header.setLockedWidth(true);

        header.setWidths(new int[]{1, 4, 1});
        header.addCell(createTextCell(""));
        header.addCell(createTextCell(orgDetails));

        header.addCell(createTextCell(""));


        PdfPTable RecieptTilte = new PdfPTable(3);
        // RecieptTilte.setTotalWidth(200);
        // RecieptTilte.setLockedWidth(true);
        RecieptTilte.setWidths(new int[]{1, 1, 1});
        RecieptTilte.setSpacingBefore(8);
        RecieptTilte.setSpacingAfter(6);
        RecieptTilte.getDefaultCell().setBorderWidthLeft(0);
        RecieptTilte.getDefaultCell().setBorderWidthRight(0);
        RecieptTilte.addCell("Receipt");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd ,yyyy");
        String d = simpleDateFormat.format(date);
        RecieptTilte.addCell("" + d);
        RecieptTilte.setSpacingAfter(8);
        RecieptTilte.addCell("RN : 1267");


        PdfPTable RecieptitemsTitles = new PdfPTable(5);

        //  RecieptitemsTitles.setTotalWidth(200);
        RecieptitemsTitles.setWidths(new int[]{2, 1, 1, 2, 2});

        // RecieptitemsTitles.setLockedWidth(true);

        RecieptitemsTitles.addCell(creatTextCellTitles("Item"));
        RecieptitemsTitles.addCell(creatTextCellTitles("Qty"));
        RecieptitemsTitles.addCell(creatTextCellTitles("Unit"));
        RecieptitemsTitles.addCell(creatTextCellTitles("Value"));
        RecieptitemsTitles.addCell(creatTextCellTitles("Total"));

        for (int a = 0; a < items.size(); a++) {
            RecieptitemsTitles.addCell(createTextCellcolor(items.get(a).getItem_name(), a));

            RecieptitemsTitles.addCell(createTextCellcolor("", a));
            RecieptitemsTitles.addCell(createTextCellcolor("", a));
            RecieptitemsTitles.addCell(createTextCellcolor("", a));
            RecieptitemsTitles.addCell(createTextCellcolor("", a));
            RecieptitemsTitles.addCell(createTextCellcolor("", a));


            RecieptitemsTitles.addCell(createTextCellcolor(items.get(a).getItem_quantity(), a));
            RecieptitemsTitles.addCell(createTextCellcolor(items.get(a).getItem_unit_type(), a));
            RecieptitemsTitles.addCell(createTextCellcolor(items.get(a).getItem_selling_price(), a));
            RecieptitemsTitles.addCell(createTextCellcolor(items.get(a).getCart_item_total_price(), a));

        }

        PdfPTable totals = new PdfPTable(3);
        // RecieptTilte.setTotalWidth(200);
        // RecieptTilte.setLockedWidth(true);
        totals.setWidths(new int[]{1, 1, 1});
        totals.setSpacingBefore(8);
        totals.setSpacingAfter(6);
        totals.getDefaultCell().setBorderWidthTop(0);
        totals.getDefaultCell().setBorderWidthBottom(0);
        totals.getDefaultCell().setBorderWidthLeft(0);
        totals.getDefaultCell().setBorderWidthRight(0);
        totals.addCell("");

        totals.addCell("Total");

        totals.setSpacingAfter(8);
        totals.addCell("Ksh : " + total);

        totals.addCell("");

        totals.addCell("Cash In");
        totals.setSpacingAfter(8);
        totals.addCell("Ksh : " + cashIn);

        totals.addCell("");
        totals.addCell("Balance");
        totals.setSpacingAfter(8);
        totals.addCell("Ksh : " + String.valueOf(cashIn - total));


        document.add(header);
        document.add(RecieptTilte);
        document.add(RecieptitemsTitles);
        document.add(totals);
        document.close();

        return myFile;
    }

    public PdfPCell createTextCellNb(String text) {

        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph();

        p.setAlignment(Element.ALIGN_CENTER);
        p.add(text);
        cell.addElement(p);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Rectangle.NO_BORDER);

        return cell;

    }

    public PdfPCell creatTextCellTitles(String text) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        Paragraph p = new Paragraph();
        p.setFont(FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD));
        p.add(text);
        cell.addElement(p);
        // cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public PdfPCell createTextCell(String text) {

        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph();
        p.setFont(FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD));
        //  p.setFont(Font.BOLD);
        p.setAlignment(Element.ALIGN_CENTER);
        p.add(text);
        cell.addElement(p);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Rectangle.NO_BORDER);

        return cell;

    }

    public PdfPCell createTextCellNormal(String text) {

        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph();
        // p.setFont(FontFactory.getFont(FontFactory.TIMES_BOLD,16,java.awt.Font.BOLD));
        //  p.setFont(Font.BOLD);
        p.setAlignment(Element.ALIGN_CENTER);
        p.add(text);
        cell.addElement(p);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Rectangle.NO_BORDER);

        return cell;

    }

    public PdfPCell createTextCellNormalUnderlined(String text) {

        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph();
        // p.setFont(FontFactory.getFont(FontFactory.TIMES_BOLD,16,java.awt.Font.BOLD));
        //  p.setFont(Font.BOLD);
        p.setAlignment(Element.ALIGN_CENTER);

        p.add(text);

        cell.addElement(p);

        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Rectangle.NO_BORDER);

        return cell;

    }

//    public PdfPCell createImageCell(String path) throws IOException {
//        PdfPCell cell = null;
//        try {
//            Image img = Image.getInstance(path);
//            cell = new PdfPCell(img, true);
//            cell.setFixedHeight(30);
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setPaddingTop(10);
//
//        } catch (BadElementException ex) {
//            Logger.getLogger(Printing.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(Printing.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return cell;
//    }
//    public PdfPCell createImageCellCrip(String path,PdfWriter writer) throws IOException, DocumentException {
//        PdfPCell cell = null;
//        try {
//            Image img = Image.getInstance(path);
//            float w=    img.getScaledWidth();
//            float h=    img.getScaledHeight();
//
//            PdfTemplate t=writer.getDirectContent().createTemplate(w, h);
//            t.ellipse(0, 0, w, h);
//            t.newPath();
//            t.addImage(img, w, 0, 0, h, 0, -600);
//            Image clipped=Image.getInstance(t);
//            cell = new PdfPCell(clipped, true);
//            // cell.setFixedHeight(30);
//            // cell.setBorder(Rectangle.NO_BORDER);
//            //cell.setBorder(R);
//            cell.setPaddingTop(10);
//
//        } catch (BadElementException ex) {
//            Logger.getLogger(Printing.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(Printing.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return cell;
//    }
//


    public PdfPCell creatTextCellHeader(String text) {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph();
        p.setFont(FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD));
        p.add(text);
        cell.addElement(p);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public PdfPCell createTextCellcolor(String text, int c) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        if (c % 2 == 0) {

            Paragraph p = new Paragraph();
            //removed color alternates
            //to add color alternates uncomment nxt line
            //cell.setBackgroundColor(Color.CYAN);
            p.add(text);
            cell.addElement(p);
        } else {
            Paragraph p = new Paragraph();
            //cell.setBackgroundColor(Color.CYAN);
            p.add(text);
            cell.addElement(p);
        }

        return cell;

    }

}
