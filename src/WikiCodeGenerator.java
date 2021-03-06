import java.io.File;
import java.util.List;

/**
 *
 * @author lianxianghui
 */
public class WikiCodeGenerator {
	static private String ENCODE = "UTF-8";
	// -f 文件名 -d 数据类名 -r 请求类名 -hasDataType 1有数据类型 0没有
	public static void main(String[] args) {
		String wikiFilePath = null;
		String className = null;
		String requestClassName = null;
		String projectName = Utils.getProjectName();
		boolean hasDataType = true;
		for (int i = 0; i < args.length; i = i + 2) {
			if (i + 1 >= args.length) {// 必须是成对的
				System.out.println("输入参数有误");
				return;
			}
			String argType = args[i];
			if (argType.equals("-f")) {
				wikiFilePath = args[i + 1];
				boolean isRelativePath = (wikiFilePath.charAt(0) != '/');
				if (isRelativePath)
					wikiFilePath = Utils.getBasePath() + wikiFilePath;
			} else if (argType.equals("-d")) {
				className = args[i + 1];
			} else if (argType.equals("-r")) {
				requestClassName = args[i + 1];
			} else if (argType.equals("-hasDataType")) {
				if (args[i + 1].equals("1")) {
					hasDataType = true;
				} else {
					hasDataType = false;
				}
			}
		}

		if (wikiFilePath == null) {
			System.out.println("请输入文件名");
			return;
		}
		String basePath = Class.class.getClass().getResource("/").getPath();
		String fileContent = Utils.getFileContent(wikiFilePath, ENCODE);
		WikiParser parser = new WikiParser();
		parser.encode = ENCODE;
		parser.hasDataType = true;
		parser.parseHtml(fileContent);
		List<Param> paramList = parser.paramList;
		List<Data> dataList = parser.dataList;
		// System.out.println(paramList);
		// System.out.println(dataList);

		createOutputDir();
		if (dataList.size() > 0 && className != null) {
			ModelGenerator modelGenerator = new ModelGenerator();
			modelGenerator.basePath = basePath;
			modelGenerator.projectName = projectName;
			modelGenerator.dataList = dataList;
			modelGenerator.className = className;
			modelGenerator.generateFile();

			DaoGenerator daoGenerator = new DaoGenerator();
			daoGenerator.basePath = basePath;
			daoGenerator.projectName = projectName;
			daoGenerator.dataList = dataList;
			daoGenerator.modelClassName = className;
			daoGenerator.generateFile();
		}
		if (requestClassName != null) {
			RequestGenerator requestGenerator = new RequestGenerator();
			requestGenerator.basePath = basePath;
			requestGenerator.className = requestClassName;
			requestGenerator.paramList = paramList;
			String url = parser.serverUrl;
			requestGenerator.url = url.substring(1, url.length());
			requestGenerator.projectName = projectName;
			requestGenerator.generateFile();
		}
	}

	private static void createOutputDir() {
		File outputDir = new File(Utils.getBasePath()+"output");
		if (!outputDir.exists())
			outputDir.mkdirs();
	}
}
