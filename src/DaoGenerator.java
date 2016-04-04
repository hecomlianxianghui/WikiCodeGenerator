import java.util.List;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupString;


/**
 * 生成Dao对象
 * @author lianxianghui
 */
public class DaoGenerator {
	List<Data> dataList;
	String modelClassName;
	String daoClassName;
	String projectName;
	STGroup daoFileGroup;
	STGroup sqlGroup;
	String basePath;
	void generateFile() {
		daoClassName = modelClassName+"Dao";
		if (dataList == null || modelClassName == null || daoClassName == null || projectName == null)
			return;
		String fileContent = Utils.getFileContent(basePath+"/template/daoFile.stg");
		daoFileGroup = new STGroupString(fileContent);
		
		ST dotHFileST = getDotHFileST();
		ST dotMFileST = getDotMFileST();
		Utils.writeFileContent(basePath+"output/"+daoClassName+".h", dotHFileST.render());
		Utils.writeFileContent(basePath+"output/"+daoClassName+".m", dotMFileST.render());
//		System.out.println(dotHFileST.render());
//		System.out.println(dotMFileST.render());
	}
	
	private ST getDotHFileST() {
		ST dotHFile = daoFileGroup.getInstanceOf("daoDotHFile");
		ST headerComment = Utils.getHeaderCommentST(true, daoClassName, projectName);
		//set className, superClassName, dotHHeaderComment
		dotHFile.add("className", modelClassName);
		dotHFile.add("superClassName", "NSObject");
		dotHFile.add("dotHHeaderComment", headerComment);
		return dotHFile;
	}
	
	private ST getCreateTableImpST() {
		//tableName, fields
		ST createTableImp = sqlGroup.getInstanceOf("createTableImp");
		createTableImp.add("tableName", modelClassName);
		int i = 0;
		for (Data data : dataList) {
			createTableImp.add("fields", getFieldST(data, (i == 0)));
			i++;
		}
		return createTableImp;
	}
	
	private ST getFieldST(Data data, boolean isPrimaryKey) {
		//fieldName, isPrimaryKey
		ST field = sqlGroup.getInstanceOf("field");
		field.add("fieldName", data.key);
		field.add("isPrimaryKey", isPrimaryKey);
		return field;
	}
	
	private ST getSaveST() {
		ST save = sqlGroup.getInstanceOf("save");
		//tableName, className, dicSetValues
		save.add("tableName", modelClassName);
		save.add("className", modelClassName);
		for (Data data : dataList) {
			save.add("dicSetValues", getDicSetValueST(data));
		}
		return save;
	}
	
	private ST getSaveListST() {
		ST save = sqlGroup.getInstanceOf("saveList");
		//tableName, className, dicSetValues
		save.add("tableName", modelClassName);
		save.add("className", modelClassName);
		for (Data data : dataList) {
			save.add("dicSetValues", getDicSetValueST(data));
		}
		return save;
	}
	
	private ST getDicSetValueST(Data data) {
		//fieldName
		ST dicSetValue = sqlGroup.getInstanceOf("dicSetValue");
		dicSetValue.add("fieldName", data.key);
		return dicSetValue;
	}
	
	private ST getDeleteST() {
		//tableName, className, pkName
		ST st = sqlGroup.getInstanceOf("delete");
		st.add("tableName", modelClassName);
		st.add("className", modelClassName);
		String pkName = dataList.get(0).key;
		st.add("pkName", pkName);
		return st;
	}
	
	private ST getDeleteAllST() {
		//tableName, className
		ST st = sqlGroup.getInstanceOf("deleteAll");
		st.add("tableName", modelClassName);
		st.add("className", modelClassName);
		return st;
	}
	
	private ST getFetchST() {
		//tableName, className, objFields
		ST st = sqlGroup.getInstanceOf("fetch");
		st.add("tableName", modelClassName);
		st.add("className", modelClassName);
		for (Data data : dataList) {
			st.add("objFields", getObjField(data));
		}
		return st;
	}
	
	private ST getObjField(Data data) {
		//fieldName
		ST st = sqlGroup.getInstanceOf("objField");
		st.add("fieldName", data.key);
		return st;
	}
	private ST getDotMFileST() {
		ST dotMFile = daoFileGroup.getInstanceOf("daoDotMFile");
		ST headerComment = Utils.getHeaderCommentST(false, daoClassName, projectName);
		//set className, dotMHeaderComment, createTableImp, save, saveList, delete, deleteAll, fetch
		dotMFile.add("className", modelClassName);
		dotMFile.add("dotMHeaderComment", headerComment);
		
		String fileContent = Utils.getFileContent(basePath+"/template/sql.stg");
		sqlGroup = new STGroupString(fileContent);
		dotMFile.add("createTableImp", getCreateTableImpST());
		dotMFile.add("save", getSaveST());
		dotMFile.add("saveList", getSaveListST());
		dotMFile.add("delete", getDeleteST());
		dotMFile.add("deleteAll", getDeleteAllST());
		dotMFile.add("fetch", getFetchST()); 
		return dotMFile;
	}
}
