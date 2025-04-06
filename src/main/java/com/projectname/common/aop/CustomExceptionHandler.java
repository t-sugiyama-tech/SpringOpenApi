package com.projectname.common.aop;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.projectname.common.exception.CustomBusinessException;
import com.projectname.dto.common.ErrorResponse;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {
	
	private static final String BAD_REQUEST_MSG = "400エラーが発生しました。";
	
	// HttpStatus:400時の返却
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(CustomBusinessException.class)
	public ErrorResponse customBusinessExceptionHandler(final CustomBusinessException ex) {
		
		// ログ出力にエラーメッセージ(BAD_REQUEST_MSG)を設定
		
		// 共通レスポンスにエラーメッセージを設定
		ErrorResponse response = new ErrorResponse();
		List<String> msgList = new ArrayList<>();
		msgList.add(ex.getMessage());
		response.setMsgList(msgList);
		
		return response;
	}

	// NotFound例外:404
	// 排他例外:409
	// DBエラーやファイルダウンロードエラー:500
	// すべての例外(Exception):500
	
}
