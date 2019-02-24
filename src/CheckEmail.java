import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;

public class CheckEmail implements Runnable{
	
	public static String link;
	File out;
	public CheckEmail(String link,File out)
	{
		this.out=out;
		this.link=link;
	}
	 public static String URLString;
	public static String check(String host,String mailtype,String uname,String pswd)
	{
		try {
		
		Address match=new InternetAddress("anmolaggarwal68@gmail.com");
		Properties properties = new Properties();
          
	      properties.put("mail.imaps.host", host);
	      properties.put("mail.imaps.port", "993");
	      properties.put("mail.imaps.starttls.enable", "true");
	      Session emailSession = Session.getDefaultInstance(properties);
	      
	        Store store;
			store = emailSession.getStore("imaps");
			store.connect(host, uname, pswd);
			Folder emailFolder = store.getFolder("INBOX");
		      emailFolder.open(Folder.READ_ONLY);
		      Message[] messages= emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN),false));
		     
		      System.out.println("messages.length---" + messages.length);

		      for (int i = 0; i<messages.length; i++) {
		         Message message = messages[i];
		         if((message.getFrom()[0].equals(match)) &&(message.getSubject().contains("Test")))
		         { System.out.println("---------------------------------");
		         System.out.println("Email Number " + (i+1));
		   
		         writePart(message);
		         System.out.println("Text: " + message.getContent().toString());
		         }
		      }

		      //close the store and folder objects
		      emailFolder.close(false);
		      store.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return URLString;
	      
	      
	}
	
	public static void printdetails(Message message) throws MessagingException
	{
		 System.out.println("Subject: " + message.getSubject());
         System.out.println("From: " + message.getFrom()[0]);
	}
	
	public static String match_regex(String str)
	{
		
		Pattern pattern = Pattern.compile("https:\\/\\/(.*?)Flash.zip");   
		Matcher matcher = pattern.matcher(str);
		if (matcher.find())
			str=matcher.group(0);
		   return str;
		
	}
	
	public static void writePart(Part p) throws Exception {
	      if (p instanceof Message)
	         

	      System.out.println("----------------------------");
	      System.out.println("CONTENT-TYPE: " + p.getContentType());

	      //if normal text
	      if (p.isMimeType("text/plain")) {
	         System.out.println("This is plain text");
	         System.out.println("---------------------------");
	         String str=((String) p.getContent());
	         URLString=match_regex(str);
	         System.out.println(URLString);
	         
	      } 
	      //Now I am checking the attachment
	      else if (p.isMimeType("multipart/*")) {
	         System.out.println("This is a Multipart");
	         System.out.println("---------------------------");
	         Multipart mp = (Multipart) p.getContent();
	         int count = mp.getCount();
	         for (int i = 0; i < count; i++)
	            writePart(mp.getBodyPart(i));
	      } 
	      
	    /*  else if (p.isMimeType("message/rfc822")) {
	         System.out.println("This is a Nested Message");
	         System.out.println("---------------------------");
	         writePart((Part) p.getContent());
	      } 
	      //Now inline image
	     else if (p.isMimeType("image/jpeg")) {
	         System.out.println("--------> image/jpeg");
	         Object o = p.getContent();

	         InputStream x = (InputStream) o;
	         // Construct the required byte array
	         System.out.println("x.length = " + x.available());
	         while ((i = (int) ((InputStream) x).available()) > 0) {
	            int result = (int) (((InputStream) x).read(bArray));
	            if (result == -1)
	         int i = 0;
	         byte[] bArray = new byte[x.available()];

	            break;
	         }
	         FileOutputStream f2 = new FileOutputStream("/tmp/image.jpg");
	         f2.write(bArray);
	      } */
	      
	      //if image
	      else if (p.getContentType().contains("image/")) {
	         System.out.println("content type" + p.getContentType());
	         File f = new File("image" + new Date().getTime() + ".jpg");
	         DataOutputStream output = new DataOutputStream(
	            new BufferedOutputStream(new FileOutputStream(f)));
	            com.sun.mail.util.BASE64DecoderStream test = 
	                 (com.sun.mail.util.BASE64DecoderStream) p
	                  .getContent();
	         byte[] buffer = new byte[1024];
	         int bytesRead;
	         while ((bytesRead = test.read(buffer)) != -1) {
	            output.write(buffer, 0, bytesRead);
	         }
	      } 
	      else {
	         Object o = p.getContent();
	         if (o instanceof String) {
	            System.out.println("This is a string");
	            System.out.println("---------------------------");
	            System.out.println((String) o);
	         } 
	         else if (o instanceof InputStream) {
	            System.out.println("This is just an input stream");
	            System.out.println("---------------------------");
	            InputStream is = (InputStream) o;
	            is = (InputStream) o;
	            int c;
	            while ((c = is.read()) != -1)
	               System.out.write(c);
	         } 
	         else {
	            System.out.println("This is an unknown type");
	            System.out.println("---------------------------");
	            System.out.println(o.toString());
	         }
	      }
	}

public static void main(String[] args) {
	String host = "webmail.qainfotech.com";// change accordingly
    String mailtype = "imaps";
    String uname = "anmolaggarwal@qainfotech.com";// change accordingly
    String pswd = "Anmolkiet@18";// change accordingly
	 String link=check(host,mailtype,uname,pswd);
	 File out=new File("C:\\Users\\anmol\\OneDrive\\Desktop\\MYABC\\ABC.zip");
	 new Thread(new CheckEmail(link, out)).start();
	
}

@Override
public void run() {
	try {
		URL url=new URL(URLString);
		HttpURLConnection http=(HttpURLConnection)url.openConnection();
		double filesize=(double)http.getContentLengthLong();
		BufferedInputStream in=new BufferedInputStream(http.getInputStream());
		FileOutputStream fos=new FileOutputStream(this.out);
		BufferedOutputStream bout=new BufferedOutputStream(fos, 1024);
		byte[] buffer=new byte[1024];
		double downloaded=0.00;
		int read=0;
		double percentdownloaded=0.00;
		while((read=in.read(buffer, 0, 1024))>=0)
		{
			bout.write(buffer, 0, read);
			downloaded+=read;
			percentdownloaded=(downloaded*100)/filesize;
			String percent=String.format("%.4f",percentdownloaded);
			System.out.println("Downloaded"+percent+"% of file");
		}
		bout.close();
		in.close();
		System.out.println("Download complete");
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
