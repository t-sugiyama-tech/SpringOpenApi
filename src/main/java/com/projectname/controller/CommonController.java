package com.projectname.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public abstract class CommonController {
	
	protected List<String> getValidationMsg(final BindingResult bindingResult) {
		List<String> msg = new ArrayList<>();
		for (FieldError errors : bindingResult.getFieldErrors()) {
			List<Object> arg = new ArrayList<>();
			for (Object error : errors.getArguments()) {
				if (!(error instanceof MessageSourceResolvable)) {
					arg.add(error);
				}
			}
			msg.add(MessageFormat.format(errors.getDefaultMessage(), arg.toArray()));
		}
		return msg;
	}

}
