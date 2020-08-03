package com.ot.cm.mail;

import java.io.File;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.ot.cm.config.properties.ApplicationProperties;
import com.ot.cm.util.CommonUtil;

@Component
public class InvitationMailClient {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private ApplicationProperties appProperties;

	public void sendMail(String to, String cc, String from, String replyTo, String subject,
			Map<String, String> bodyPlaceholders, String content, File img) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper mimeHelper = new MimeMessageHelper(message, true, "UTF-8");

		String[] toList = to.split(",");
		mimeHelper.setTo(toList);

		if (!CommonUtil.isEmpty(cc)) {
			String[] ccList = cc.split(",");
			mimeHelper.setCc(ccList);
		}

		if (!CommonUtil.isEmpty(from)) {
			mimeHelper.setFrom(from);
		}
		if (!CommonUtil.isEmpty(replyTo)) {
			mimeHelper.setReplyTo(replyTo);
		}
		if (!CommonUtil.isEmpty(subject)) {
			mimeHelper.setSubject(subject);
		}

		/*
		 * if(file!=null){ for(FileSystemResource attachedFile : file){
		 * helper.addAttachment(attachedFile.getFilename(), attachedFile); } }
		 */

		if (bodyPlaceholders != null && !bodyPlaceholders.isEmpty()) {
			mimeHelper.setText(mailContent(bodyPlaceholders, content), true);
			if (null == img) {
				mimeHelper.addInline("header-img", new ClassPathResource("static/OTnotificationLogo.png"));
			} else {
				mimeHelper.addInline("header-img", img);
			}
		}

		try {
			Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
			mailSender.send(message);
		} catch (MailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String mailContent(Map<String, String> placeholdersMap, String content) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html> <body><div><img src='cid:header-img' alt=\"Notification\"></img></div> ");
		sb.append("<p>").append(content).append("</p>");

		sb.append("<p>" + placeholdersMap.get("company_name")
				+ " has requested a partnership with your company to trade on the OpenText Trading Grid using ");
		sb.append(placeholdersMap.get("serviceName") + ".</p>");
		sb.append("<p>Using this service with " + placeholdersMap.get("company_name") + " requires that "
				+ "your company register on the Grid and provide specific company information "
				+ "as well as approve online Terms & Conditions.  "
				+ "The first person to do this for your company becomes "
				+ "the Company Administrator and can add additional users to your company&apos;s account."
				+ " If you&apos;re the right person for this activity, please register using the following link: <p>");
		String url = appProperties.getRegistrationAppURL() + "/company/" + placeholdersMap.get("invitation_code");
		sb.append("<a href='").append(url.trim()).append("'>").append(url + "</a>");
		sb.append("<br>");
		sb.append("<p>If at any time the above link does not work, you can access the Grid directly " + "at "
				+ appProperties.getRegistrationAppURL() + " and use the following invitation code:  <b>"
				+ placeholdersMap.get("invitation_code") + "</b></p>");
		sb.append(
				"<p>If you are an existing user, please login to the Grid to access this invitation from the Message Center,");
		sb.append("and you can bypass some of the registration process. </p>");
		sb.append(
				"<p>After submitting your registration, you will receive an email indicating that you have been activated on the Grid. Should you have any questions about the registration process or "
						+ "use of the Grid, please contact your local Business Network Support team. The following link provides information for all support regions and services:"
						+ "</p>");

		sb.append("<a href=\"http://www.opentext.com/support\" >http://www.opentext.com/support</a>");
		sb.append("<br>");

		sb.append("<p>Regards,</p>");
		sb.append("<p>OpenText Trading Grid Admin</p>"); // Grid Administrator
		sb.append(
				"<p><b>Note:</b> This email was automatically generated. Replies to this email will not be received.</p>");

		sb.append(
				"<br><div style=\"text-align:center\"><small><b>Copyright &copy; 2019. OpenText. All Rights Reserved.</b></small>");
		sb.append("<br><small>Trademarks owned by OpenText.<small></div>");
		sb.append("</body></html>");
		return sb.toString();
	}

}
