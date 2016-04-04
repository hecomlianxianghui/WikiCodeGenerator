import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupString;

/**
 * 工具类
 * 
 * @author lianxianghui
 */
public class Utils {
	static String author = null;
	static String projectName = null;
	static String basePath = null;
	
	public static String getFileContent(String filePath) {
		return Utils.getFileContent(filePath, "UTF-8");
	}

	public static String getFileContent(String filePath, String encode) {
		try {
			BufferedReader bis = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(filePath)), encode));
			StringBuilder stringBuilder = new StringBuilder();
			String lineContent;

			while ((lineContent = bis.readLine()) != null) {
				stringBuilder.append(lineContent).append("\n");
			}
			bis.close();
			return stringBuilder.toString();
		} catch (Exception e) {
			return null;
		}
	}

	public static void writeFileContent(String filePath, String fileContent) {
		try {
			FileWriter fw = new FileWriter(filePath, false);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(fileContent);
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void loadConfigData() {
		try {
			String path = getBasePath() + "config.ini";
			FileInputStream fin = new FileInputStream(path); // 打开文件
			Properties props = new Properties(); // 建立属性类
			props.load(fin); // 读入文件
			author = props.get("author").toString();
			projectName = props.get("projectName").toString();
			fin.close();
		} catch (Exception e) {
			author = "";
			projectName = "";
		}
	}

	public static String getAuthor() {
		if (author == null)
			loadConfigData();
		return author;
	}

	static String getBasePath() {
		if (basePath == null)
			basePath = Class.class.getClass().getResource("/").getPath();
		return basePath;
	}
	
	static String getProjectName() {
		if(projectName == null)
			loadConfigData();
		return projectName;
	}

	public static ST getHeaderCommentST(boolean isDotHFile, String className,
			String projectName) {
		String fileContent = Utils.getFileContent(getBasePath()
				+ "/template/headerComment.st");
		STGroup group = new STGroupString(fileContent);
		ST headerComment = group.getInstanceOf("headerComment");
		// set fileName, projectName, author, date, year
		String fileName = className;
		if (isDotHFile)
			fileName += ".h";
		else
			fileName += ".m";
		// 获取date字符串和year字符串
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yy/M/d");
		String date = sdf.format(now);
		sdf = new SimpleDateFormat("yyyy");
		String year = sdf.format(now);

		headerComment.add("fileName", fileName);
		headerComment.add("projectName", projectName);
		headerComment.add("author", getAuthor());
		headerComment.add("date", date);
		headerComment.add("year", year);
		return headerComment;
	}
}
