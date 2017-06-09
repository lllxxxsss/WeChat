package com.wechat.portal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;

import com.wechat.core.CoreController;
import com.wechat.util.EncryptUtil;

@Component
public class PortalServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(PortalServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		/*
		 * List<Map<String,Object>> list = loanAppService.selectCfList();
		 * //取得Application对象 ServletContext
		 * application=this.getServletContext(); //设置Application属性
		 * application.setAttribute("cfList", list); super.init();
		 */
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long beginTime = System.currentTimeMillis();
		String echostr = request.getParameter("echostr");
		if (checkSignature(request)) {
			PrintWriter out = response.getWriter();
			out.write(echostr);
			out.flush();
			out.close();
			logger.info("接入微信成功(GET).");
		} else {
			logger.info("与微信握手失败(GET)!");
		}
		logger.debug("Link Portal Cost : " + (System.currentTimeMillis() - beginTime));
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setCharacterEncoding("UTF-8");
		 request.setCharacterEncoding("UTF-8"); 
		 if (!checkSignature(request)){ 
			 logger.error("与微信握手失败(POST)!"); return; 
			 }
		 
		 // 获取WeChat核心处理器 
		 
		 String respXml = ((ApplicationContext)request.getServletContext()
				 .getAttribute(DispatcherServlet.SERVLET_CONTEXT_PREFIX +"Dispatcher"))
				 .getBean(CoreController.class).execute(request);
		 logger.debug("[WeChat Response XML] " + respXml);
		 if(!"".equals(respXml)){ // 将处理结果响应给微信服务器
			 PrintWriter out =response.getWriter();
			 out.write(respXml); 
			 out.flush(); out.close(); 
			 }
		 

	}

	/**
	 * 验证消息真实性
	 * 
	 * @param request
	 * @return
	 */
	private boolean checkSignature(HttpServletRequest request) {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		if (signature == null || timestamp == null || nonce == null) {
			logger.info("signature || timestamp || nonce  is null .........");
			return false;
		}
		logger.info("signature:" + signature + " timestamp:" + timestamp + "  nonce:" + nonce);

		 /*String token = systemService.getParamValue("app.token");*/
		
		String token = "sdafasjdlkfjalsf8eifjajeifla";
		 logger.debug("WeChat App Token : " + token);

		boolean flag = false;
		
		 String sign =EncryptUtil.dictionarySort(token, timestamp, nonce); 
		 String sign_sha1= EncryptUtil.sHA1Encode(sign); // sign_sha1：Portal与微信比对 flag =
		 flag=signature.equalsIgnoreCase(sign_sha1); 
		 
		return flag;
	}
	
	public static void main(String[] args) {
		
		// signaturec:682b553afbdaa40cb11cf4ffbe7c91114a2f156 timestamp:1496648939  nonce:3413600491
		
		String signature="c682b553afbdaa40cb11cf4ffbe7c91114a2f156";
		String timestamp="1496648939";
		String nonce="3413600491";
		String token = "sdafasjdlkfjalsf8eifjajeifla";
		
		 String sign =EncryptUtil.dictionarySort(token, timestamp, nonce); 
		 String sign_sha1= EncryptUtil.sHA1Encode(sign); // sign_sha1：Portal与微信比对 flag =
		 signature.equalsIgnoreCase(sign_sha1); 
		 
		 System.err.println(signature.equalsIgnoreCase(sign_sha1));
		 
		
	}

}
