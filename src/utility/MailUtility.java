package utility;

import java.util.Properties;
import java.util.Date;

import javax.mail.*;
import javax.mail.internet.*;

public class MailUtility {
	public MailUtility(String email,String password,String porta,String host,String url){	
		Properties props = new Properties();
		props.put("mail.smtp.socketFactory.port",porta);
		props.put("mail.smtp.host",host);
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.auth","true");
		
		Authenticator auth = new Authenticator() {
     		protected PasswordAuthentication getPasswordAuthentication() {
        		return new PasswordAuthentication(email,password);
      		}
    	};
    	
    	URL = url;
		EMAIL = email;
		SESSIONE = Session.getInstance(props,auth);
	}
	
	public static void sendMail(String destinatario,String sessionID) throws Exception{
		Message msg = new MimeMessage(SESSIONE);

		msg.setFrom(new InternetAddress(EMAIL));
		msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(destinatario));
		msg.setSubject("Messaggio Di Conferma Registrazione 2Stream");
		msg.setText("Benvenuto su 2Stream\nPer completare la registrazione segui il link : \n"
				+ "http://" + URL + "/Progetto/Confirm?cod=" + sessionID);
		msg.setSentDate(new Date());

		Transport.send(msg);
	}
	
	private static Session SESSIONE;
	private static String EMAIL;
	private static String URL;
}
