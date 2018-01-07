package milo.shiftplanner.mail;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.smtp.SimpleSMTPHeader;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

@Named
public final class EmailService {

	@Value("${mail.smtp.host}")
	private String smtpHost;
	@Value("${mail.sender}")
	private String sender;

	public void send(String recipient, String subject, String message) {
		if (smtpHost != null && !smtpHost.isEmpty()) {
			smtpSend(recipient, subject, message);
		}
	}

	private void smtpSend(String recipient, String subject, String message) {
		try {
			SimpleSMTPHeader header = new SimpleSMTPHeader(sender, recipient, subject);

			SMTPClient client = new SMTPClient();
			client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));

			client.connect(smtpHost);

			if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
				client.disconnect();
				throw new RuntimeException("SMTP server refused connection.");
			}

			client.login();
			client.setSender(sender);
			client.addRecipient(recipient);
			Writer writer = client.sendMessageData();

			if (writer != null) {
				writer.write(header.toString());
				writer.write(message);
				writer.close();
				client.completePendingCommand();
			}

			client.logout();
			client.disconnect();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

