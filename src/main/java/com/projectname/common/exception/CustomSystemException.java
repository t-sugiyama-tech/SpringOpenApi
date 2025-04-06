package com.projectname.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/** RuntimeExceptionを継承したカスタムシステム例外は@Transactionalでのロールバック対象になる.  */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomSystemException extends RuntimeException {
	
	/** カスタムコード.  */ 
	private String customCode;
	
	/** 自由形式.  */ 
	private List<String> objList;
	
	/** システム例外クラス.  */
	public CustomSystemException(final String message) {
		super(message);
	}
	
	/** システム例外クラス.  */
	public CustomSystemException(final String message, final Throwable cause) {
		// 元のエラー原因を出力する場合
		super(message, cause);
	}
	
	/** システム例外クラス.  */
	public CustomSystemException(final String message, final String customCode, final List<String> objList) {
		// カスタムコードはHttp自体にHttpStatusを保持しているためアンチパターンではあるが、フロントエンドに返却する必要性がある場合は設定
		super(message);
		this.customCode = customCode;
		this.objList = objList;
	}

}
