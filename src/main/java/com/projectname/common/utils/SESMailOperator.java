package com.projectname.common.utils;

import com.projectname.common.enums.MailType;
import com.projectname.common.exception.CustomSystemException;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class SESMailOperator {
     
	// メール送信ライブラリ
	private final MailSender mailSender;
	
	// メール送信元
	@Value("${mail.from}")
	private String sendFrom;
	
	// タイトル置換文字
	private static final String SUBJECT_KEY = "$subject";
	
	@Transactional(noRollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW) 
	public void sendRealTimeMail(final MailType mailType, final VelocityContext context, final String sendTo, final String userId) {
		
		String sendResultFlg = "成功";
		// 引数チェック
		// Check Something	
				
		// メール本文
		String mailText = createMailText(mailType, context);
		// メールタイトル
		String subject = createMailSubject(mailType, context);
		
		// メッセージオブジェクト
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(sendTo); // 送信先
		message.setFrom(sendFrom); // 送信元
		message.setReplyTo(null); // 返信先メールアドレス
		message.setSubject(subject); // タイトル
		message.setText(mailText);
		
		//　メール送信
		try {
			mailSender.send(message);
		} catch (Exception ex) {
			// 送信結果フラグを失敗に更新
			sendResultFlg = "失敗";
			// ログ出力
		} finally {
			// 送信結果をDBに登録
			// Insert Table
		}
		
	 }
	
	private String createMailText(final MailType mailType, final VelocityContext context) {
	    
		Properties prop = new Properties();
	    prop.setProperty("resource.loader", "classpath");
	    prop.setProperty("classpath.resource.loader.class",  ClasspathResourceLoader.class.getName());
	    prop.setProperty("input.encoding", "UTF-8");
	    prop.setProperty("output.encoding", "UTF-8");
	    prop.setProperty("file.resource.loader.cache", "true");
	    
	    //　Velocityエンジンの生成
	    VelocityEngine velocityEngine = new VelocityEngine(prop);
	    velocityEngine.init();
	    // テンプレートの読み込み置換文字列を変換
	    StringWriter writer = new StringWriter();
	    velocityEngine.mergeTemplate("templates/" + mailType.getTemplateFileName(), StandardCharsets.UTF_8.name(), context, writer);
        return writer.toString();
	}
	
	private String createMailSubject(final MailType mailType, final VelocityContext context) {
		
		// メール種類からタイトルを取得
		String subject = mailType.getSubject();
		
		// タイトルに置換文字列が設定されている場合
		if (subject.contains(SUBJECT_KEY)) {
			if (!context.containsKey(SUBJECT_KEY)) {
				// 置換文字列引数にタイトル用の文字列が設定されていない
				throw new CustomSystemException("パラメータが不足しています。");
			}
			String replaceWord = (String) context.get(SUBJECT_KEY);
			subject = subject.replace(SUBJECT_KEY, replaceWord);
		}
		return subject;
	}
}
