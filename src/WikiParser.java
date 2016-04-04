import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 对wiki的html文件进行解析，获取需要的数据
 * 
 * @author lianxianghui
 */
class WikiParser {
	String serverUrl;// 如 /link/getDuang.do
	int prefixUrlType;//0 authUrl 1 commonUrl
	List<Param> paramList = new ArrayList<Param>();
	List<Data> dataList = new ArrayList<Data>();

	String encode;
	Document document;
	Element body;
	void parseHtml(String htmlContent) {
		if (encode == null)
			encode = "UTF-8";
		document = Jsoup.parse(htmlContent);
		if (document == null) 
			return;
		body = document.body();
		if (body == null)
			return;
		parseUrl();
		parseParams();
		parseDatas();
	}
	
	void parseUrl() {
		Elements pTagElements = body.getElementsByTag("p");
		for (Element element : pTagElements) {
			String text = element.text();
			if (text.contains("服务地址：")) {
				String url = text.replace("服务地址：", "");
				if (url.contains("{authUrl}")) {
					url = url.replace("{authUrl}", "");
					prefixUrlType = 0;
				} else if (url.contains("{commonUrl}")) {
					url = url.replace("{commonUrl}", "");
					prefixUrlType = 1;
				}
				serverUrl = url;
				return;
			}
		}
	}
	
	void parseParams() {
		Element paramTable = body.getElementById("params");
		if (paramTable == null)
			return;
		int i = 0;
		for (Element tr : paramTable.getElementsByTag("tr")) {
			if (i == 0) {//header 
				i++;
				continue;
			}
			Param currentParam = new Param();
			int paramTDIndex = 0;
			for (Element td : tr.getElementsByTag("td")) {
				String tdText = td.text();
        		switch (paramTDIndex) {
				case 0:
					currentParam.key = tdText;
					break;
				case 1:
					if (tdText.contains("是")) {
						currentParam.optional = false;
					} else {
						currentParam.optional = true;
					}
					break;
				case 2:
					currentParam.comment = tdText;
					paramList.add(currentParam);
					break;
				default:
					break;
				}
        		paramTDIndex++;
			}
		}
	}
	
	void parseDatas() {
		Element dataTable = body.getElementById("datas");
		if (dataTable == null)
			return;
		int i = 0;
		for (Element tr : dataTable.getElementsByTag("tr")) {
			if (i == 0) {//header 
				i++;
				continue;	
			}
			int dataDescriptionIndex = 0;
			Data currentData = new Data();
			for (Element td : tr.getElementsByTag("td")) {
				String text = td.text();
				switch (dataDescriptionIndex) {
				case 0:
					currentData.key = text;
					break;
				case 1:
					currentData.comment = text;
					dataList.add(currentData);
					break;
				default:
					break;
				}
				dataDescriptionIndex++;
			}
		}
	}

	void printString(String string) {
		try {
			if (encode == null)
				encode = "GBK";
			System.out.println(new String(string.getBytes(encode), System
					.getProperty("file.encoding")));
		} catch (Exception e) {

		}
	}
}
