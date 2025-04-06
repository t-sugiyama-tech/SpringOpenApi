package com.projectname.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/** Exceptionを継承したカスタム業務例外は@Transactionalでのロールバック対象外になるため、業務チェックに限定するべき.  */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomBusinessException extends Exception {
	
	/** カスタムコード.  */ 
	private String customCode;
	
	/** 自由形式.  */ 
	private List<String> objList;
	
	/** 業務例外クラス.  */
	public CustomBusinessException(final String message, final Throwable cause) {
		// 元のエラー原因を出力する場合
		super(message, cause);
	}
	
	/** 業務例外クラス.  */
	public CustomBusinessException(final String message, final String customCode, final List<String> objList) {
		// カスタムコードはHttp自体にHttpStatusを保持しているためアンチパターンではあるが、フロントエンドに返却する必要性がある場合は設定
		super(message);
		this.customCode = customCode;
		this.objList = objList;
	}

}
