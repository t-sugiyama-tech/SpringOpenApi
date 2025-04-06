package com.projectname.service;

import com.projectname.common.enums.MailType;
import com.projectname.common.utils.SESMailOperator;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.VelocityContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HogeService {
	
	// メール送信機能
	private final SESMailOperator sesMailOperator;
	
	public String createUser(final String userMail) {

		// .vmのメールテンプレートで指定された置換対象文字を指定
		VelocityContext context = new VelocityContext();
		context.put("name", "Keiko");
		context.put("address", "Tokyo");

		// お知らせメール送信
		sesMailOperator.sendRealTimeMail(MailType.MAIL_01, context, userMail, "U001");
				
		return "登録成功";
	}

}
