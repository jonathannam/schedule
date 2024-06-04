package com.agribank.schedule.service;


import com.agribank.schedule.entity.Email;
import com.agribank.schedule.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

@Service
public class SendMailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

	@Autowired
	private EmailRepository emailRepository;

	@Async
	public void sendEmail(String to, String subject, String body) {
//		SimpleMailMessage mailMessage = new SimpleMailMessage();
//		mailMessage.setFrom(sender);
//		mailMessage.setTo(to);
//		mailMessage.setText(body);
//		mailMessage.setSubject(subject);
//
//		javaMailSender.send(mailMessage);

		try {
			List<Email> emails = emailRepository.findAll();
			List<String> aList = new ArrayList<String>();

			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");

			for (Email email : emails) {
				aList.add(email.getEmail());
			}

			String[] listEmails = aList.toArray(new String[aList.size()]);

			message.setTo(listEmails);
//			message.setSubject("Người mua: " + buyer + " đã mua sim số " + phoneNo);
//			message.setText("Thông tin sim: <br/>-Số điện thoại: " + phoneNo + " <br/>-Giá SIM: "
//					+ FormatUtils.formatCurrency(price) + " <br/>-Giá gốc: "
//					+ FormatUtils.formatCurrency(price - profit) + " <br/>-Người bán: " + seller + " <br/>-Người mua: "
//					+ buyer + " <br/>-Ngày mua: "
//					+ DateTimeUtils.formatDate(new Date(), DateTimeUtils.DD_MM_YYYY_HH_MM), true);

			if (listEmails.length != 0) {
				this.javaMailSender.send(mimeMessage);
			}
		} catch (Exception exception) {
			System.out.println("Lỗi nhé :)");
			exception.printStackTrace();
		}

	}
}
