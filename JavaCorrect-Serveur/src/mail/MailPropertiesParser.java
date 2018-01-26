package mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public class MailPropertiesParser {
	private Properties headerProperties = new Properties();
	private Properties imapProperties = new Properties();
	private Properties smtpProperties = new Properties();
	private Properties mailAdressProperties = new Properties();
	private static MailPropertiesParser instance = null;
	
	private MailPropertiesParser() {
		final Properties prop = new Properties();
		final File file = new File("resources/mail.properties");
		try (final InputStream input = new FileInputStream(file);) {
			prop.load(input);
			this.imapProperties.put("mail.imap.host", prop.getProperty("IMAP_HOST"));
			this.imapProperties.put("mail.imap.port", prop.getProperty("IMAP_PORT"));
			this.imapProperties.put("mail.imap.auth", prop.getProperty("IMAP_AUTH"));
			this.imapProperties.put("mail.imap.ssl.enable", prop.getProperty("IMAP_SSL_ENABLED"));			
			this.smtpProperties.put("mail.smtp.host", prop.getProperty("SMTP_HOST"));
			this.smtpProperties.put("mail.smtp.port", prop.getProperty("SMTP_PORT"));
			this.smtpProperties.put("mail.smtp.auth", prop.getProperty("SMTP_AUTH"));
			this.smtpProperties.put("mail.smtp.starttls.enable", prop.getProperty("SMTP_STARTTLS_ENABLED"));
			this.headerProperties.put("Content-type", prop.getProperty("HEADER_CONTENT_TYPE"));
			this.headerProperties.put("format", prop.getProperty("HEADER_FORMAT"));
			this.headerProperties.put("Content-Transfer-Encoding", prop.getProperty("HEADER_TRANSFERT_ENCODING"));
			this.mailAdressProperties.put("address", prop.getProperty("MAILADDRESS"));
			this.mailAdressProperties.put("password", prop.getProperty("MAILPASSWORD"));
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public Properties getImapProperties() {
		return this.imapProperties;
	}
	public Properties getSmtpProperties() {
		return this.smtpProperties;
	}
	public Properties getHeaderProperties() {
		return this.headerProperties;
	}
	public Properties getMailAdressProperties() {
		return mailAdressProperties;
	}

	
	public static synchronized MailPropertiesParser getInstance() {
		if (instance == null) {
			instance = new MailPropertiesParser();
		}
		return instance;
	}


}

