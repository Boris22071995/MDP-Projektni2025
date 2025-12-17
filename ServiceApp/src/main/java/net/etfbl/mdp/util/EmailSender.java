package net.etfbl.mdp.util;

import javax.mail.*;
import javax.mail.internet.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class EmailSender {

	public static void sendZip(String from, String password, String to, String subject, String text, File zipFile) throws Exception{
		Properties props = new Properties();
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");
	    
	    Session session = Session.getInstance(props,
	            new Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(from, password);
	                }
	            }
	        );
	    
	    Message message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(from));
	    message.setRecipients(
	            Message.RecipientType.TO,
	            InternetAddress.parse(to)
	    );
	    message.setSubject(subject);
	    
	    MimeBodyPart textPart = new MimeBodyPart();
	    textPart.setText(text);

	    MimeBodyPart attachmentPart = new MimeBodyPart();
	    attachmentPart.attachFile(zipFile);

	    Multipart multipart = new MimeMultipart();
	    multipart.addBodyPart(textPart);
	    multipart.addBodyPart(attachmentPart);

	    message.setContent(multipart);

	    Transport.send(message);
	}
	
}
