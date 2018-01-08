package milo.shiftplanner.mail;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.smtp.SimpleSMTPHeader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

@Service
public class EmailService {

//	@Inject
	private String mailSmtpHost;
//	@Inject
	private String mailSmtpSender;

	public void send(String recipient, String subject, String message) {
		if (mailSmtpHost != null && !mailSmtpHost.isEmpty()) {
			smtpSend(recipient, subject, message);
		}
	}

	private void smtpSend(String recipient, String subject, String message) {
		try {
			SimpleSMTPHeader header = new SimpleSMTPHeader(mailSmtpSender, recipient, subject);

			SMTPClient client = new SMTPClient();
			client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));

			client.connect(mailSmtpHost);

			if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
				client.disconnect();
				throw new RuntimeException("SMTP server refused connection.");
			}

			client.login();
			client.setSender(mailSmtpSender);
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

