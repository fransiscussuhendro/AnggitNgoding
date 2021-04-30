package sompo.utility;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail {
	
//	public static void main(String args[]){
//		try {
//	        String[] to = {"fransiscus.suhendro@sompo.co.id","muhamad.irkhamsyah@sompo.co.id" }; // list of recipient email addresses
//	        String[] cc=null;
//	        String[] bcc=null;
////			sendEmailSingleRecipient("fransiscus.suhendro@sompo.co.id","","","no-reply@sompo.co.id","TEST","webmail.sompo.co.id","test","test",null);
//			sendEmailMultipleRecipient(to,cc,bcc,"no-reply@sompo.co.id","TEST","webmail.sompo.co.id","test","test",null);
//		} catch (MessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public  void sendEmailSingleRecipient(String mailTo,String cc,String bcc, String from, String masking, String host, String message,
			String subject, String[] attachFiles) throws MessagingException {

		// Get the session object
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		Session session = Session.getInstance(properties,null);
        session.setDebug(true);
        
		// compose the message
		// message info
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(from, masking));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
		if(!cc.equals("")){
		msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
		}
		if(!bcc.equals("")){
		msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
		}
		msg.setSubject(subject);

		// creates message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, "text/html");

		// creates multi-part
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// adds attachments
		if (attachFiles != null && attachFiles.length > 0) {
			for (String filePath : attachFiles) {
				MimeBodyPart attachPart = new MimeBodyPart();

				try {
					attachPart.attachFile(filePath);
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				multipart.addBodyPart(attachPart);
			}
		}

		// sets the multi-part as e-mail's content
		msg.setContent(multipart);

		// sends the e-mail
		Transport.send(msg);
	}
	
	public  void sendEmailMultipleRecipient(String[] mailTo,String[] cc,String[] bcc, String from, String masking, String host, String message,
			String subject, String[] attachFiles) throws MessagingException {

		// Get the session object
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		Session session = Session.getInstance(properties,null);
        session.setDebug(true);
        
		// compose the message
		// message info
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(from, masking));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InternetAddress[] toAddress=new InternetAddress[mailTo.length];
		for(int i=0;i<mailTo.length;i++){
			toAddress[i]=new InternetAddress(mailTo[i]);
		}
		
		if(cc!=null){
			InternetAddress[] ccAddress=new InternetAddress[cc.length];
			for(int i=0;i<cc.length;i++){
				ccAddress[i]=new InternetAddress(cc[i]);
			}
			msg.addRecipients(Message.RecipientType.CC, ccAddress);
		}
		if(bcc!=null){
			InternetAddress[] bccAddress=new InternetAddress[bcc.length];
			for(int i=0;i<bcc.length;i++){
				bccAddress[i]=new InternetAddress(bcc[i]);
			}
			msg.addRecipients(Message.RecipientType.BCC, bccAddress);
		}
		
		
		
		msg.addRecipients(Message.RecipientType.TO, toAddress);
		
		
		msg.setSubject(subject);

		// creates message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, "text/html");

		// creates multi-part
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// adds attachments
		if (attachFiles != null && attachFiles.length > 0) {
			for (String filePath : attachFiles) {
				MimeBodyPart attachPart = new MimeBodyPart();

				try {
					attachPart.attachFile(filePath);
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				multipart.addBodyPart(attachPart);
			}
		}

		// sets the multi-part as e-mail's content
		msg.setContent(multipart);

		// sends the e-mail
		Transport.send(msg);
	}

}
