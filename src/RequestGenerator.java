import java.util.List;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupString;

/**
 * 请求代码生成
 * @author lianxianghui
 */
public class RequestGenerator {
	List<Param> paramList;
	String url;// 例如"link/getDuang.do" 
	String className;
	String projectName;
	STGroup group;
	String basePath;
	void generateFile() {
		if (paramList == null || className == null || projectName == null)
			return;
		String fileContent = Utils.getFileContent(basePath+"/template/requestFile.stg");
		group = new STGroupString(fileContent);
		ST dotHFileST = getDotHFileST();
		ST dotMFileST = getDotMFileST();
//		System.out.println(dotHFileST.render());
//		System.out.println(dotMFileST.render());
		Utils.writeFileContent(basePath+"output/"+className+".h", dotHFileST.render());
		Utils.writeFileContent(basePath+"output/"+className+".m", dotMFileST.render());
	}
	
	private ST getDotHFileST() {
		ST dotHFile = group.getInstanceOf("dotHFile");
		ST headerComment = Utils.getHeaderCommentST(true, className, projectName);
		//set className, superClassName, dotHHeaderComment, properties
		dotHFile.add("className", className);
		dotHFile.add("superClassName", "HttpReqBase");
		dotHFile.add("dotHHeaderComment", headerComment);
		for (Param param : paramList) {
			dotHFile.add("properties", getPropertyST(param));
		}
		return dotHFile;
	}
	
	private ST getPropertyST(Param param) {
		String fileContent = Utils.getFileContent(basePath+"/template/property.st");
		STGroup group = new STGroupString(fileContent);
		//type, name, comment
		ST property = group.getInstanceOf("property");
		property.add("type", "NSString *");
		property.add("name", param.key);
		String comment = param.comment + "(必需:" + (param.optional ? "否" : "是") + ")";
		property.add("comment", comment);
		return property;
	}
	
	private ST getDotMFileST() {
		ST dotMFile = group.getInstanceOf("dotMFile");
		ST headerComment = Utils.getHeaderCommentST(false, className, projectName);
		//set className, dotMHeaderComment, url, mustParams, dicSetValues
		dotMFile.add("className", className);
		dotMFile.add("dotMHeaderComment", headerComment);
		dotMFile.add("url", url);
		for (Param param : paramList) {
			if (!param.optional)
				dotMFile.add("mustParams", getMustParam(param));
		}
		for (Param param : paramList) {
			dotMFile.add("dicSetValues", getDicSetValue(param));
		}
		return dotMFile;
	}
	
	private ST getMustParam(Param param) {
		ST st = group.getInstanceOf("mustParam");
		//fieldName
		st.add("fieldName", param.key);
		return st;
	}
	
	private ST getDicSetValue(Param param) {
		ST st = group.getInstanceOf("dicSetValue");
		//fieldName
		st.add("fieldName", param.key);
		return st;
	}
}
