package com.dev.sdk.util;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSender {

	private static Properties properties = null;
	private static Session session = null;

	static {
		try {
			// Sets SMTP server properties
			properties = new Properties();
			//properties.put("mail.debug", "true");  
			properties.put("mail.smtp.host", "smtp.gmail.com");
			properties.put("mail.smtp.port", "587"); //Working
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.login.username", "uis.qa2@gmail.com");
			properties.put("mail.login.password", "Test123@");
			
			//Not working
			/*
			properties.put("mail.smtp.port", 25);  
			properties.put("mail.smtp.socketFactory.port", 25);  
			properties.put("mail.transport.protocol", "smtp");
			*/			
			
			//Working
			/*
			properties.put("mail.smtp.socketFactory.port", "465");
			properties.put("mail.smtp.port", "465");	
			properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			properties.put("mail.smtp.socketFactory.fallback", "false");
			*/

			// Creates a new session with an authenticator
			Authenticator auth = new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
							(String) properties.get("mail.login.username"),
							(String) properties.get("mail.login.password"));
				}
			};

			session = Session.getInstance(properties, auth);
			System.out.println("Session created - " + session);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendEmailWithAttachments(final String userName,
			String toAddress, String subject, String message,
			String[] attachFiles) throws AddressException, MessagingException {

		// creates a new e-mail message
		Message msg = new MimeMessage(session);

		msg.setFrom(new InternetAddress("splunkalert@gmail.com"));
		InternetAddress[] toAddresses = InternetAddress.parse(toAddress, true);
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		
		//msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
		
		msg.setSubject(subject);
		msg.setSentDate(new Date());

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

	/**
	 * Test sending e-mail with attachments
	 */
	public static void main(String[] args) {
		// message info
		String mailFrom = "splunkalert@gmail.com";
		//String mailTo = "amitav.mohanty@intl.verizon.com,srinivas.reddy.alluri@intl.verizon.com,surya.p.ambarkar@intl.verizon.com";
		String mailTo = "amitav.mohanty@intl.verizon.com";
		String subject = "Splunk Alert Notification";
		String message = "You have some incidents";

		// attachments
		String[] attachFiles = new String[1];
		//attachFiles[0] = "/root/util/test.txt";
		attachFiles[0] = "C:/EclipseWorkspace/Configuration/properties/config/Sample.txt";

		try {

			sendEmailWithAttachments(mailFrom, mailTo, subject, message, attachFiles);
			System.out.println("Email sent.");

		} catch (Exception ex) {
			System.out.println("Could not send email.");
			ex.printStackTrace();
		}
	}
}
