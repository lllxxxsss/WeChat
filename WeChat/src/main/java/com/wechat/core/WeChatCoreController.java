package com.wechat.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.spi.LoggerFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class WeChatCoreController implements CoreController {
	
	private static final Log logger = LogFactory.getLog(WeChatCoreController.class);

	// 核心处理器
	public String execute(HttpServletRequest request) {
		// TODO Auto-generated method stub
		Map<String, String> parseXml = parseXml(request);
		
		
		return null;
	}


	/**
	 * 解析微信XMl
	 * @param request
	 * @return
	 */
	public static Map<String, String> parseXml(HttpServletRequest request) {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();
		// 从request中取得输入流
		InputStream inputStream = null;
		try {
			inputStream = request.getInputStream();
			// 读取输入流
			Document document = new SAXReader().read(inputStream);
			// 得到xml根元素
			Element root = document.getRootElement();
			// 得到根元素的所有子节点
			List<Element> elementList = root.elements();

			// 遍历所有子节点
			for (Element e : elementList)
				map.put(e.getName(), e.getText());

		} catch (IOException e1) {
			logger.error(e1, e1);
		} catch (DocumentException e1) {
			logger.error(e1, e1);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		return map;
	}

}
