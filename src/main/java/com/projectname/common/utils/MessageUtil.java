package com.projectname.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.context.support.ResourceBundleMessageSource;

import com.projectname.common.enums.MessageId;

public final class MessageUtil {
	
	// Utilクラスの為、インスタンス化を抑止
	private MessageUtil() {
	}
	
	// 引数なしのメッセージ
	public static String getMessage(final MessageId messageId) {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("msg/messages");
		messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
		return messageSource.getMessage(messageId.toString(), null, Locale.getDefault());
	}
	
	// 引数ありのメッセージ
	public static String getMessage(final MessageId messageId, final Object[] params) {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("msg/messages");
		messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
		return messageSource.getMessage(messageId.toString(), params, Locale.getDefault());
	}

}
