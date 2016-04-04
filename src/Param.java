
/**
 * 请求参数
 * @author lianxianghui
 */
class Param {
	@Override
	public String toString() {
		return "Param [key=" + key + ", optional=" + optional + ", comment="
				+ comment + "]";
	}
	String key;
	boolean optional;//可选的
	String comment;
}
