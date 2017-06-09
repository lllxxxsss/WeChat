package com.wechat.core;

import javax.servlet.http.HttpServletRequest;

public interface CoreController {
/**
 * 
 * @param request
 * @return
 */
	
	public String execute(HttpServletRequest request);
}
