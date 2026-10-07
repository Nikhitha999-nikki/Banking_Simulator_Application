package com.bank.BankSimulator.util;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PdfReportUtil {

    public static byte[] generateTransactionStatement(
            String accountNumber,
            List<Map<String, Object>> transactions) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Document document = new Document(PageSize.A4, 36, 36, 36, 36);

            PdfWriter.getInstance(document, outputStream);

            document.open();

            // Fonts
            Font titleFont = new Font(
                    Font.HELVETICA,
                    20,
                    Font.BOLD
            );

            Font subtitleFont = new Font(
                    Font.HELVETICA,
                    12,
                    Font.NORMAL
            );

            Font tableHeaderFont = new Font(
                    Font.HELVETICA,
                    10,
                    Font.BOLD
            );

            Font tableFont = new Font(
                    Font.HELVETICA,
                    9,
                    Font.NORMAL
            );

            // Title
            Paragraph title = new Paragraph(
                    "BANKING SIMULATOR",
                    titleFont
            );

            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Subtitle
            Paragraph statementTitle = new Paragraph(
                    "Transaction Statement",
                    subtitleFont
            );

            statementTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(statementTitle);

            document.add(new Paragraph(" "));

            // Account details
            document.add(
                    new Paragraph(
                            "Account Number : " + accountNumber,
                            subtitleFont
                    )
            );

            String generatedDate =
                    new SimpleDateFormat(
                            "dd-MM-yyyy HH:mm:ss"
                    ).format(new Date());

            document.add(
                    new Paragraph(
                            "Generated On   : " + generatedDate,
                            subtitleFont
                    )
            );

            document.add(new Paragraph(" "));

            // Transaction table
            PdfPTable table = new PdfPTable(4);

            table.setWidthPercentage(100);

            table.setWidths(new float[]{
                    2.5f,
                    1.5f,
                    1.5f,
                    2.0f
            });

            addHeaderCell(table, "Date & Time", tableHeaderFont);
            addHeaderCell(table, "Type", tableHeaderFont);
            addHeaderCell(table, "Amount", tableHeaderFont);
            addHeaderCell(table, "Target Account", tableHeaderFont);

            for (Map<String, Object> transaction : transactions) {

                Object timestamp =
                        transaction.get("timestamp");

                Object type =
                        transaction.get("type");

                Object amount =
                        transaction.get("amount");

                Object targetAccount =
                        transaction.get("targetAccount");

                String dateText =
                        timestamp != null
                                ? new SimpleDateFormat(
                                        "dd-MM-yyyy HH:mm"
                                  ).format(timestamp)
                                : "-";

                String typeText =
                        type != null
                                ? type.toString()
                                : "-";

                String amountText = "-";

                if (amount != null) {

                    BigDecimal value;

                    if (amount instanceof BigDecimal) {
                        value = (BigDecimal) amount;
                    } else {
                        value = new BigDecimal(
                                amount.toString()
                        );
                    }

                    amountText =
                            "Rs. " + value.toPlainString();
                }

                String targetText =
                        targetAccount != null
                                ? targetAccount.toString()
                                : "-";

                addCell(table, dateText, tableFont);
                addCell(table, typeText, tableFont);
                addCell(table, amountText, tableFont);
                addCell(table, targetText, tableFont);
            }

            document.add(table);

            document.add(new Paragraph(" "));

            Paragraph footer = new Paragraph(
                    "This is a computer-generated transaction statement.",
                    tableFont
            );

            footer.setAlignment(Element.ALIGN_CENTER);

            document.add(footer);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate transaction PDF",
                    e
            );
        }
    }

    private static void addHeaderCell(
            PdfPTable table,
            String text,
            Font font) {

        PdfPCell cell = new PdfPCell(
                new Phrase(text, font)
        );

        cell.setHorizontalAlignment(
                Element.ALIGN_CENTER
        );

        cell.setPadding(6);

        table.addCell(cell);
    }

    private static void addCell(
            PdfPTable table,
            String text,
            Font font) {

        PdfPCell cell = new PdfPCell(
                new Phrase(text, font)
        );

        cell.setPadding(5);

        table.addCell(cell);
    }
}