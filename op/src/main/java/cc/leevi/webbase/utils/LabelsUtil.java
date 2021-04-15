package cc.leevi.webbase.utils;

import java.util.ArrayList;

public class LabelsUtil {
	public static ArrayList<String> lineList = new ArrayList<String>();

	static {
		// 关系列表
		lineList.add("COMMENTSACTOR");
		lineList.add("COMMENTSDIRECTOR");
		lineList.add("COMMENTSMOVIE");
		lineList.add("ACTORDIRECTOR");
		lineList.add("ACTORMOVIE");
		lineList.add("DIRECTORMOVIE");
	}

	/** 转换为汉字 */
	public static String toChinese(String _label) {
		if ("Actor".equals(_label) || "演员".equals(_label)) {
			_label = "演员";
		} else if ("Director".equals(_label) || "导演".equals(_label)) {
			_label = "导演";
		} else if ("Movie".equals(_label) || "电影".equals(_label)) {
			_label = "电影";
		} else if ("Comments".equals(_label) || "评论".equals(_label)) {
			_label = "评论";
		} else {
			return "";
		}
		return _label;
	}

	/** 转换为字母 */
	public static String toEnglish(String _label) {
		if ("Actor".equals(_label) || "演员".equals(_label)) {
			_label = "Actor";
		} else if ("Director".equals(_label) || "导演".equals(_label)) {
			_label = "Director";
		} else if ("Movie".equals(_label) || "电影".equals(_label)) {
			_label = "Movie";
		} else if ("Comments".equals(_label) || "评论".equals(_label)) {
			_label = "Comments";
		} else {
			return "";
		}
		return _label;
	}

	/** 将两个类型标签转换为关系 */
	public static String labelToLine(String _label1, String _label2) {
		for (String ls : lineList) {
			if (ls.contains(toEnglish(_label1).toUpperCase()) && ls.contains(toEnglish(_label2).toUpperCase())) {
				return ls;
			}
		}
		return "";
	}
}
