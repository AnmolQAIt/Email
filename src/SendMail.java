import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
//import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

public static void main(String[] args) {
	
	String host="webmail.qainfotech.com";
	String uname="anmolaggarwal@qainfotech.com";
	String password="Anmolkiet@18";
	
	
	Properties props=new Properties();
	props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", "587");
    
	
	Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(uname, password);
		   }
	         });

	
	MimeMessage mm=new MimeMessage(session);
	
	try {
	Address	from = new InternetAddress(uname);
	
	Address to=new InternetAddress("anmolaggarwal68@gmail.com");
	mm.setSubject("Test Sending Email");
	mm.setText("This is a simple mail Test");
	mm.setFrom(from);
	mm.setFileName("ABC");
	mm.addRecipient(MimeMessage.RecipientType.TO, to);
    mm.saveChanges();
    
    Transport.send(mm);
    System.out.println("Message sent successfully");
    
    
	}
	catch (MessagingException e) {
        throw new RuntimeException(e);
     }
}
}
