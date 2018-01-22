package tools;

public class RegexTools {

	public static boolean pregMatch(String pattern, String content) {
	    return content.matches(pattern);
	}
}
