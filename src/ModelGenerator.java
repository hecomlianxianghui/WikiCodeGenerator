import java.util.List;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupString;

/**
 * 生成模型文件
 * @author lianxianghui
 */
public class ModelGenerator {
	List<Data> dataList;
	String className;
	String projectName;
	String basePath;
	STGroup dotMFileSTGroup;
	void generateFile() {
		if (dataList == null || className == null || projectName == null)
			return;
		String fileContent = Utils.getFileContent(basePath+"/template/modelDotMFile.stg");
		dotMFileSTGroup = new STGroupString(fileContent);
		ST dotHFileST = getDotHFileST();
		ST dotMFileST = getDotMFileST();
//		System.out.println(dotHFileST.render());
//		System.out.println(dotMFileST.render());
		Utils.writeFileContent(basePath+"output/"+className+".h", dotHFileST.render());
		Utils.writeFileContent(basePath+"output/"+className+".m", dotMFileST.render());
	}

	private ST getDotHFileST() {
		String fileContent = Utils.getFileContent(basePath+"/template/modelDotHFile.st");
		STGroup group = new STGroupString(fileContent);
		ST dotHFile = group.getInstanceOf("modelDotHFile");
		ST headerComment = Utils.getHeaderCommentST(true, className, projectName);
		//set className, superClassName, properties, dotHHeaderComment
		dotHFile.add("className", className);
		dotHFile.add("superClassName", "NSObject");
		dotHFile.add("dotHHeaderComment", headerComment);
		for (Data data : dataList) {
			dotHFile.add("properties", getPropertyST(data));
		}
		return dotHFile;
	}

	private ST getPropertyST(Data data) {
		String fileContent = Utils.getFileContent(basePath+"/template/property.st");
		STGroup group = new STGroupString(fileContent);
		//type, name, comment
		ST property = group.getInstanceOf("property");
		property.add("type", "NSString *");
		property.add("name", data.key);
		property.add("comment", data.comment);
		return property;
	}

	private ST getDotMFileST() {
		ST dotMFile = dotMFileSTGroup.getInstanceOf("modelDotMFile");
		ST headerComment = Utils.getHeaderCommentST(false, className, projectName);
		//set className, dotMHeaderComment
		dotMFile.add("className", className);
		dotMFile.add("dotMHeaderComment", headerComment);
		
		for (Data data : dataList) {
			dotMFile.add("objFields", getObjField(data));
		}
		return dotMFile;
	}
	
	private ST getObjField(Data data) {
		//fieldName
		ST st = dotMFileSTGroup.getInstanceOf("objField");
		st.add("fieldName", data.key);
		return st;
	}
	

}
