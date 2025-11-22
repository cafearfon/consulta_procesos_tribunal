package com.carfher.consultaproc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsultaprocApplication {
	private static String expectedText1 = "ARENAS FONSECA";
	private static String expectedText2 = "15001310500120220002201";
	private static String url = "https://adrina.tribunalsuperiortunja.gov.co/resources/docs/";
	//private static int cont=1077;
	

	
	public static void main(String[] args) {
		List<String> lista = new ArrayList<String>();
		List<String> pdfs = new ArrayList<String>();
		SpringApplication.run(ConsultaprocApplication.class, args);
		LinkFinder lf = new LinkFinder();
		lista=lf.linksFinder("https://adrina.tribunalsuperiortunja.gov.co/lastDocumentsCourt.jsf?categoryName=TODOS&formCourt:tableDocuments:j_idt11:filter=laboral");
		for (String numero : lista) {
			String textoPDF = leerPDF(numero);
			boolean res1 = searchText(textoPDF, expectedText1);
			boolean res2 = searchText(textoPDF, expectedText2);
			if (res1 || res2){
				System.out.println("Expresiones encontradas en el archivo pdf!");
				pdfs.add(numero);
			}
		}
		if (pdfs.size()>0){
			EmailSender es = new EmailSender();
			es.sendMail(pdfs);
		}
	}

	private static String leerPDF(String cont){
		String text ="";
		try{
			String new_url = url+cont+".pdf";
		
			downloadUsingStream(new_url,cont+".pdf");
			File file = new File(cont+".pdf");
			//PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(file));
			//PDFTextStripper stripper = new PDFTextStripper();
			//text = stripper.getText(document);
			//document.close();
			text=parse(file.getName());
		}catch(Exception e){
			System.out.println(e);
		}
		return text;
	}

	public static void downloadUsingStream(String urlStr, String fileDestination) throws IOException {
        URL url = new URL(urlStr);
        try (BufferedInputStream bis = new BufferedInputStream(url.openStream());
             FileOutputStream fos = new FileOutputStream(fileDestination)) {
            byte[] buffer = new byte[1024];
            int count;
            while ((count = bis.read(buffer, 0, 1024)) != -1) {
                fos.write(buffer, 0, count);
            }
        }
    }

	public static String parse(String imagePath) throws Exception {
        Tesseract tesseract = new Tesseract();
        // Perform OCR on the image
        tesseract.setLanguage("spa"); // Set language for OCR
		tesseract.setPageSegMode(1);
        File imageFile = new File(imagePath);
        return tesseract.doOCR(imageFile);
    }

	public static boolean searchText(String text, String expectedText){
		boolean result = false;
		int i = text.indexOf(expectedText);
		if (i > 0){
			result=true;
		}
        System.out.println("Occurrences of '"+expectedText+"': " + i);
		return result;
	}

	
}
