package com.projectname.common.enums;

import lombok.Getter;

public enum MailType {
	
	MAIL_01("お知らせ", "お知らせ:[$subject]", "mail001.vm", false),
	
	MAIL_02("パスワード再設定通知", "パスワード再設定のお知らせ", "mail002.vm", true);
	
	// テンプレート名
	@Getter
	private String templateName;
	
	// メールタイトル
	@Getter
	private String subject;
	
	// テンプレートファイル名称(.vm)
	@Getter
	private String templateFileName;
	
	// リアルタイム送信フラグ
	@Getter
	private boolean realTimeFlg;
	
	// コンストラクタ
	MailType(final String templateName, final String subject, final String templateFileName, final boolean realTimeFlg) {
		this.templateName = templateName;
		this.subject = subject;
		this.templateFileName = templateFileName;
		this.realTimeFlg = realTimeFlg;
	}

}
