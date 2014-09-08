package utilitiesBound;

public class StringUtils {
	public static String REGX_ALPHA = "^[\\p{L} .'-]+$";
	public static String REGX_DIGIT = "[0-9]+";
	
	public static String TrimDoubleSpaces(String word){
		if(word == null){
			return null;
		}
		String str;
		str = word.trim();
		str = str.replaceAll("\\s+", " "); // S'il y a 2 espaces au milieu
		return str;
	}
	
	public static String TrimFull(String word){
		if(word == null) return "";
		String str;
		str = word.trim();
		str = str.replaceAll("\\s+", ""); // Enleve tous les espaces
		return str;
	}
	
	public static String TrimForNumbers(String word){
		if(word == null) return "";
		String str;
		str = word.trim();
		//str = str.replaceAll("\\s+", ""); // Enleve tous les espaces
		str = str.replaceAll("[^\\d]", "");
		return str;
	}
	
	public static String setEmptyToNull(String str){
		if(str.isEmpty() || str == ""){
			return null;
		}
		else{
			return str;
		}
	}
}
