package com.carfher.consultaproc;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class EmailSender {
    private static final String fromEmail = "cafearfon@gmail.com"; //requires valid gmail id
	private static final String password = "gpxs fvcb hwia qzln";
	private static final String toEmail = "cafearfon@gmail.com"; // can be any email id 
	private static Session session = null;

    public EmailSender(){
        Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
		props.put("mail.smtp.port", "587"); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		
        //create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
        session = Session.getInstance(props, auth);
    }


    public void sendMail(List<String> pdfs){
		try {
        MimeMessage msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(fromEmail, "NoReply-JD"));
        msg.setReplyTo(InternetAddress.parse(toEmail, false));
        msg.setSubject("Estados del tribunal - Coincidencia", "UTF-8");
        msg.setSentDate(new Date());
        msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

        // Parte del mensaje (texto)
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText("Se ha encontrado una coincidencia con el documento adjunto.", "UTF-8");

        // Parte del archivo adjunto
        MimeBodyPart attachmentPart = new MimeBodyPart();
        for(String archivo : pdfs){
            DataSource source = new FileDataSource(archivo);
            attachmentPart.setDataHandler(new javax.activation.DataHandler(source));
            attachmentPart.setFileName(MimeUtility.encodeText(new File(archivo).getName(), "UTF-8", null));
        }
        

        // Crea el mensaje multipart
        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);

        // Asigna el contenido al mensaje
        msg.setContent(multipart);

        System.out.println("Message with attachment is ready");
        Transport.send(msg);
        System.out.println("EMail Sent Successfully!!");
    } catch (Exception e) {
        e.printStackTrace();
    }
	}

}
