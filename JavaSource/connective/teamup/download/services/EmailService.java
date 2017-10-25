/*
 * 06/01/2005 - Created
 */
package connective.teamup.download.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.log4j.Logger;

import connective.teamup.download.ActionException;
import connective.teamup.download.CarrierInfo;

/**
 * Singleton service for handling email processes.
 * 
 * @author Kyle McCreary
 */
public class EmailService
{
	private static final Logger LOGGER = Logger.getLogger(EmailService.class);
	
	private static EmailService _the_instance = null;
	
	protected String techSupportEmailAddress = "";


	/**
	 * Default singleton constructor.
	 */
	protected EmailService()
	{
		super();
	}

	public static EmailService getInstance()
	{
		if (_the_instance == null)
			_the_instance = new EmailService();
		return _the_instance;
	}

	/**
	 * Returns the technical support email address for error reports.
	 * @return String
	 */
	public String getTechSupportEmailAddress()
	{
		return techSupportEmailAddress;
	}

	/**
	 * Sets the technical support email address for error reports.
	 * @param string - The email address
	 */
	public void setTechSupportEmailAddress(String string)
	{
		techSupportEmailAddress = string;
	}

	/**
	 * Send an email (non-HTML only) using the smtp messaging service.
	 * 
	 * @param from The sending email address
	 * @param to The recipient email address
	 * @param subject The email subject line
	 * @param message The body of the email
	 * 
	 * @throws javax.mail.MessagingException
	 */
	public void sendEMail(String to, String subject, String message)
		throws ActionException
	{
		sendEMail(to, subject, message, null, null, null);
	}

//	/**
//	 * Send an email using the smtp messaging service.
//	 * 
//	 * @param from The sending email address
//	 * @param to The recipient email address
//	 * @param subject The email subject line
//	 * @param message The body of the email
//	 * @param attachment The name of the file attachment (if applicable)
//	 * 
//	 * @throws javax.mail.MessagingException
//	 */
//	public void sendEMail(String to, String subject, String message, InputStream[] attachments, String[] attachmentNames)
//		throws ActionException
//	{
//		// send the email
//		Session session = null;
//		try
//		{
//			InitialContext ctx = new InitialContext();
//			session = (Session) ctx.lookup("java:comp/env/mail/teamupdl");
//		}
//		catch (NamingException e) {}
//		
//		try
//		{
//			if (session != null)
//			{
//				// check the subject line -- no multi-line text allowed
//				if (subject != null)
//				{
//					int n = subject.indexOf('\n');
//					if (n > 0)
//						subject = subject.substring(0, n);
//				}
//				
//				MimeMessage mimemsg = new MimeMessage(session);
//				// parse multiple email recipients using semicolon separator
//				if (to.indexOf(';') != -1)
//				{
//					StringTokenizer st = new StringTokenizer(to, ";");
//					while (st.hasMoreTokens())
//						mimemsg.addRecipient(Message.RecipientType.TO, new InternetAddress(st.nextToken()));
//				}
//				else
//				{
//					mimemsg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//				}
//				mimemsg.setSubject(subject);
//				Multipart multipart = new MimeMultipart();
//				
//				// add the message body
//				MimeBodyPart msgBody = new MimeBodyPart();
//				msgBody.setText(message);
//				multipart.addBodyPart(msgBody);
//									
//				if (attachments != null && attachments.length > 0 && 
//					attachmentNames != null && attachmentNames.length > 0 &&
//					attachments.length == attachmentNames.length)
//				{
//					for (int i=0; i < attachments.length; i++)
//					{
//						MimeBodyPart attachmentPart = new MimeBodyPart();
//						attachments[i].reset();
//						DataSource source = new ByteArrayDataSource(attachments[i], "application/octet-stream");
//						attachmentPart.setDataHandler(new DataHandler(source));
//						attachmentPart.setFileName(attachmentNames[i]);
//						multipart.addBodyPart(attachmentPart);
//					}
//				}
//	
//				// send the message
//				mimemsg.setContent(multipart);
//				Transport.send(mimemsg);
//			}
//		}
//		catch (Exception e)
//		{
//			throw new ActionException("Error sending email", e);
//		}
//	}

	/**
	 * Send an email with alternative HTML version using the smtp messaging service.
	 * 
	 * @param from The sending email address
	 * @param to The recipient email address
	 * @param subject The email subject line
	 * @param textMsg The body of the email (text version)
	 * @param htmlMsg The body of the email (HTML version)
	 * 
	 * @throws javax.mail.MessagingException
	 */
	public void sendEMail(String to, String subject, String textMsg, String htmlMsg)
		throws ActionException
	{
		sendEMail(to, subject, textMsg, htmlMsg, null, null);
	}

	/**
	 * Send an email with alternative HTML version using the smtp messaging service.
	 * 
	 * @param from The sending email address
	 * @param to The recipient email address
	 * @param subject The email subject line
	 * @param textMsg The body of the email (text version)
	 * @param htmlMsg The body of the email (HTML version)
	 * @param attachment The name of the file attachment (if applicable)
	 * 
	 * @throws javax.mail.MessagingException
	 */
	public void sendEMail(String to, String subject, String textMsg, String htmlMsg, InputStream[] attachments, String[] attachmentNames)
		throws ActionException
	{
		// send the email
		Session session = null;
		try
		{
			InitialContext ctx = new InitialContext();
			session = (Session) ctx.lookup("java:comp/env/mail/teamupdl");
		}
		catch (NamingException e) {
			LOGGER.error(e);
		}
		
		try
		{
			if (session != null)
			{
				// check the subject line -- no multi-line text allowed
				if (subject != null)
				{
					int n = subject.indexOf('\n');
					if (n > 0)
						subject = subject.substring(0, n);
				}
				
				MimeMessage mimemsg = new MimeMessage(session);
				// parse multiple email recipients using semicolon separator
				if (to.indexOf(';') != -1)
				{
					StringTokenizer st = new StringTokenizer(to, ";");
					while (st.hasMoreTokens())
						mimemsg.addRecipient(Message.RecipientType.TO, new InternetAddress(st.nextToken()));
				}
				else
				{
					mimemsg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				}
				mimemsg.setSubject(subject);
				
				Multipart multipart = null;
				if (htmlMsg == null || htmlMsg.trim().equals(""))
					multipart = new MimeMultipart();
				else
					multipart = new MimeMultipart("alternative");
				
				// add the text version of the message body
				MimeBodyPart textMsgBody = new MimeBodyPart();
				textMsgBody.setContent(textMsg, "text/plain");
				multipart.addBodyPart(textMsgBody);
				
				if (htmlMsg != null && !htmlMsg.trim().equals(""))
				{
					// add the HTML version of the message body
					MimeBodyPart htmlMsgBody = new MimeBodyPart();
					htmlMsgBody.setContent(htmlMsg, "text/html");
					multipart.addBodyPart(htmlMsgBody);
				}
									
				if (attachments != null && attachments.length > 0 && 
					attachmentNames != null && attachmentNames.length > 0 &&
					attachments.length == attachmentNames.length)
				{
					for (int i=0; i < attachments.length; i++)
					{
						MimeBodyPart attachmentPart = new MimeBodyPart();
						attachments[i].reset();
						DataSource source = new ByteArrayDataSource(attachments[i], "application/octet-stream");
						attachmentPart.setDataHandler(new DataHandler(source));
						attachmentPart.setFileName(attachmentNames[i]);
						multipart.addBodyPart(attachmentPart);
					}
				}
	
				// send the message
				mimemsg.setContent(multipart);
				Transport.send(mimemsg);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error sending email", e);
			throw new ActionException("Error sending email", e);
		}
	}

	/**
	 * Send a notification email to the carrier when an unexpected exception occurs.
	 * @param theException - The exception info
	 * @param appName - The application from which the exception originated
	 * @param agentId - The current agent ID, if applicable
	 */
	public void sendErrorNotification(Throwable theException, String appName, String agentId, String appVersion, String dbVersion)
	{
		String to = CarrierInfo.getInstance().getErrorsEmail();
		
		if (to != null && !to.equals(""))
		{
			// Default the email subject to include company name and exception message
			String subject = CarrierInfo.getInstance().getShortName() + 
							 " TEAM-UP Download System error - " + theException.getMessage();
			
			// Default the email body to include app name, agent ID (if applicable) and the 
			// exception stack trace
			String body = "Application:  ";
			if (appName != null && !appName.equals(""))
				body += appName + "\n";
			else
				body += "(unknown)\n";
			if (appVersion != null && dbVersion != null)
				body += "TUDL version: " + appVersion + " / DB version: " + dbVersion + "\n";
			if (agentId != null && !agentId.equals(""))
				body += "Agent ID:  " + agentId + "\n";
			
			if (theException.getClass().getName().indexOf("ActionException") >= 0)
				body += ((ActionException)theException).toString();
			else
				body += theException.toString();
			body += "\n\n\nSTACK TRACE:\n";
					
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(os);
			theException.printStackTrace(ps);
			body += os.toString();
					
			ps.close();
			try
			{
				os.close();
			} catch (IOException e) {
				LOGGER.error(e);
			}
	
			try
			{
				sendEMail(to, subject, body);
			}
			catch (Exception e)
			{
				LOGGER.error(e);
				System.out.println("Error sending Tech Support email - " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Send an email to CTI's tech support using the smtp messaging service.
	 * 
	 * @param subject - The email subject line
	 * @param message - Special message to include in body of email (or 'null' to just include exception info)
	 * @param exception - The exception (if applicable)
	 * 
	 * @throws javax.mail.MessagingException
	 */
	public void sendTechSupportEmail(String subject, String message, Exception exception)
	{
		try
		{
			subject = CarrierInfo.getInstance().getShortName() + " download error: " + subject;
			String body = "This email is being sent to Ebix,Inc technical support solely for informational purposes.  It has not been sent to the carrier in this form.\n\n--------------------------------\n\n";
			if (message != null && !message.equals(""))
				body += message + "\n\n--------------------------------\n\n";
			if (exception != null)
			{
				body += exception.toString();
				body += "\n\n\nSTACK TRACE:\n";
			}
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(os);
			exception.printStackTrace(ps);
			body += os.toString();
			ps.close();
			os.close();
			
			// send the email
			InitialContext ctx = new InitialContext();
			Session session = (Session) ctx.lookup("java:comp/env/mail/teamupdl");
			MimeMessage mimemsg = new MimeMessage(session);
			mimemsg.addRecipient(Message.RecipientType.TO, new InternetAddress(getTechSupportEmailAddress()));
			mimemsg.setSubject(subject);
			mimemsg.setText(body);
			Transport.send(mimemsg);
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			// Do not throw an exception
			System.out.println("The following exception occurred while trying to send a tech support email: " + e.getMessage());
		}
	}

	public void sendDailyReportEMail(String to, String subject, String textMsg, String htmlMsg, ByteArrayOutputStream outputStream, String attachmentName, String attachmentType)
			throws ActionException
		{
			// send the email
			Session session = null;
			try
			{
				InitialContext ctx = new InitialContext();
				session = (Session) ctx.lookup("java:comp/env/mail/teamupdl");
			}
			catch (NamingException e) {
				LOGGER.error(e);
			}
			
			try
			{
				if (session != null)
				{
					// check the subject line -- no multi-line text allowed
					if (subject != null)
					{
						int n = subject.indexOf('\n');
						if (n > 0)
							subject = subject.substring(0, n);
					}
					
					MimeMessage mimemsg = new MimeMessage(session);
					// parse multiple email recipients using semicolon separator
					if (to.indexOf(';') != -1)
					{
						StringTokenizer st = new StringTokenizer(to, ";");
						while (st.hasMoreTokens())
							mimemsg.addRecipient(Message.RecipientType.TO, new InternetAddress(st.nextToken()));
					}
					else
					{
						mimemsg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
					}
					mimemsg.setSubject(subject);
					
					Multipart multipart = null;
					if (htmlMsg == null || htmlMsg.trim().equals(""))
						multipart = new MimeMultipart();
					else
						multipart = new MimeMultipart("alternative");
					
					if (textMsg != null && !textMsg.trim().equals(""))
					{
						// add the text version of the message body
						MimeBodyPart textMsgBody = new MimeBodyPart();
						textMsgBody.setContent(textMsg, "text/plain");
						multipart.addBodyPart(textMsgBody);
					}
					
					
					if (htmlMsg != null && !htmlMsg.trim().equals(""))
					{
						// add the HTML version of the message body
						MimeBodyPart htmlMsgBody = new MimeBodyPart();
						htmlMsgBody.setContent(htmlMsg, "text/html");
						multipart.addBodyPart(htmlMsgBody);
					}
										
					if (outputStream != null )
					{
						byte[] bytes = outputStream.toByteArray();
						MimeBodyPart attachmentPart = new MimeBodyPart();

						DataSource source = new ByteArrayDataSource(bytes, attachmentType);
						attachmentPart.setDataHandler(new DataHandler(source));

						attachmentPart.setFileName(attachmentName);
						multipart.addBodyPart(attachmentPart);
						
						/*for (int i=0; i < attachments.length; i++)
						{
							MimeBodyPart attachmentPart = new MimeBodyPart();
							attachments[i].reset();
							DataSource source = new ByteArrayDataSource(attachments[i], "application/octet-stream");
							attachmentPart.setDataHandler(new DataHandler(source));
							attachmentPart.setFileName(attachmentNames[i]);
							multipart.addBodyPart(attachmentPart);
						}*/
					}
		
					// send the message
					mimemsg.setContent(multipart);
					Transport.send(mimemsg);
				}
			}
			catch (Exception e)
			{
				LOGGER.error("Error sending email", e);
				throw new ActionException("Error sending email", e);
			}
		}
}
