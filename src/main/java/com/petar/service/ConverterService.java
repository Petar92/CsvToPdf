/*
 * iText is outdated and this part needs to be replaced using OpenPDF 
 */

package com.petar.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.PropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.petar.model.S3OutputStream;

@Component
public class ConverterService {
	
	AmazonS3 s3client = AmazonS3ClientBuilder
			  .standard()
			  .withCredentials(new PropertiesFileCredentialsProvider("/home/petar/.aws/credentials"))
			  .withRegion(Regions.EU_CENTRAL_1)
			  .build();
	
	public void convert(MultipartFile file) throws IOException, DocumentException {

		byte[] bytes = file.getBytes();

		String str = new String(bytes);

		String[] splitted = Arrays.stream(str.split("\n")).map(String::trim).toArray(String[]::new);

		List<String> list = new LinkedList<>(Arrays.asList(splitted));
		list.remove(0);
		list.remove(0);
		list.remove(1);
		String first = list.get(0).substring(1, list.get(0).length());
		list.remove(0);
		list.add(0, first);


		int count = 1;
		String[] firstRow = list.get(0).split(",");
		for (String record : list) {
			if (record.equals(list.get(0))) continue;
			String[] line = record.split(",");
			Document document = new Document(PageSize.A4, 25, 25, 25, 25);
			final OutputStream out = new S3OutputStream(s3client, "csv-to-pdf-invoices", "Document/pdfs/" + line[5]  + ".pdf");
			count++;
			PdfWriter pdfWriter = PdfWriter.getInstance(document, out);
			document.setPageSize(PageSize.LETTER.rotate());

			document.open();

			Paragraph heading = new Paragraph("Podaci o firmi",
					FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, new BaseColor(0, 0, 0)));

			document.add(heading);

			PdfPTable t = new PdfPTable(5);
			t.setSpacingBefore(25);
			t.setSpacingAfter(25);
						
			String date1 = firstRow[2];
			String sender1 = firstRow[3];
			String address1 = firstRow[4];
			String recipient1 = firstRow[5];
			String recipientAddress1 = firstRow[6];

			String date2 = line[3];
			String sender2 = line[4];
			String address2 = line[5];
			String recipient2 = line[6];
			String recipientAddress2 = line[7];

			
			PdfPCell c1 = new PdfPCell(new Phrase(date1));
			t.addCell(c1);

			PdfPCell c2 = new PdfPCell(new Phrase(sender1));
			t.addCell(c2);

			PdfPCell c3 = new PdfPCell(new Phrase(address1));
			t.addCell(c3);

			PdfPCell c4 = new PdfPCell(new Phrase(recipient1));
			t.addCell(c4);
			
			PdfPCell c5 = new PdfPCell(new Phrase(recipientAddress1));
			t.addCell(c5);
			
			PdfPCell c6 = new PdfPCell(new Phrase(date2));
			t.addCell(c6);

			PdfPCell c7 = new PdfPCell(new Phrase(sender2));
			t.addCell(c7);

			PdfPCell c8 = new PdfPCell(new Phrase(address2));
			t.addCell(c8);

			PdfPCell c9 = new PdfPCell(new Phrase(recipient2));
			t.addCell(c9);
			
			PdfPCell c10 = new PdfPCell(new Phrase(recipientAddress2));
			t.addCell(c10);
			
			document.add(t);

			Paragraph footer1 = new Paragraph("            ____________________                              ____________________",
					FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, new BaseColor(0, 0, 0)));
			
			Paragraph footer2 = new Paragraph("                     (robu izdao)                                     	             (robu primio)",
					FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, new BaseColor(0, 0, 0)));
			
			document.add(footer1);
			document.add(footer2);
			
			document.close();
			pdfWriter.close();

		}

	}
}
