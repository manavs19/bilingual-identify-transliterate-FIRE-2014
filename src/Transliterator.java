import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class Transliterator {

	private static HashMap<String, String> syllableMap = new HashMap<String, String>();
	public static int a = 0, b = 0, c = 0, d = 0;

	public Transliterator(String syllableFile){
		createSyllableMap(syllableFile);
	}
	
	private static void createSyllableMap(String syllableFile) {
		// TODO Auto-generated method stub

		FileInputStream fis;
		BufferedReader br;
		String line = "";
		try {
			fis = new FileInputStream(syllableFile);
			br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

			line = br.readLine();
			while (line != null) {
				line = line.trim();
				if (line.isEmpty()){
					line = br.readLine();
					continue;
				}
				int pos1 = line.indexOf('\t');
				int pos2 = line.indexOf(' ');
				int pos = pos2 == -1 ? pos1 : pos2;
				if(pos==-1){
					line = br.readLine();
					continue;
				}
				String english = line.substring(0, pos);
				english = english.toLowerCase();
				String hindi = line.substring(pos + 1);
				syllableMap.put(english, hindi);
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static List<String> syllabify(String s) {
		List<Integer> breakpoints = new ArrayList<Integer>();

		if (!isVowel(s.charAt(s.length() - 1))
				&& !((s.charAt(s.length() - 1)) == 'y')) {
			s = s.concat("a");
		}

		int firstVowel = s.length();

		for (int i = 0; i < s.length(); i++)
			if (isVowel(s.charAt(i))) {
				firstVowel = i;
				break;
			}

		// System.out.print(firstVowel +" ");
		// kamalnayan
		for (int i = firstVowel; i < s.length(); i++) {
			for (int j = i + 1; j < s.length(); j++) {
				if (isVowel(s.charAt(j))) {

					if ((j - i) == 2) {
						breakpoints.add(i);
						breakpoints.add(i + 1);
					} else if ((j - i) >= 3) {
						breakpoints.add(i + 1);
						breakpoints.add(i + 2);
					}

					i = j - 1;

					break;
				}
			}
		}

		for (int i = 1; i < s.length() - 1; i++) {
			if ((((s.charAt(i) == 's') || (s.charAt(i) == 'b')
					|| (s.charAt(i) == 'p') || (s.charAt(i) == 't')
					|| (s.charAt(i) == 'k') || (s.charAt(i) == 'g')
					|| (s.charAt(i) == 'c') || (s.charAt(i) == 'l')
					|| (s.charAt(i) == 'j') || (s.charAt(i) == 'd')) && (s
					.charAt(i + 1) == 'h'))
					|| ((s.charAt(i) == 'g') && (s.charAt(i + 1) == 'y'))
					|| ((i <= s.length() - 4) && (s.charAt(i) == 'k')
							&& (s.charAt(i + 1) == 's') && (s.charAt(i + 2) == 'h'))
					|| ((s.charAt(i) == 't') && (s.charAt(i + 1) == 'r'))
					|| ((s.charAt(i) == 't') && (s.charAt(i + 1) == 'v'))) {
				if (breakpoints.contains((Integer) (i + 1))) {
					breakpoints.remove((Integer) (i + 1));
					breakpoints.add(i - 1);
				}
				i += 2;
			}
		}

		breakpoints.add(0);
		breakpoints.add(s.length() - 1);

		breakpoints = new ArrayList<Integer>(new LinkedHashSet<Integer>(
				breakpoints));
		Collections.sort(breakpoints);

		if (breakpoints.size() % 2 == 1 && breakpoints.size() > 1) {
			breakpoints.add(1, breakpoints.get(1) - 1);
		}

		if (s.endsWith("eina")) {
			breakpoints.remove(breakpoints.size() - 3);
			breakpoints.remove(breakpoints.size() - 1);

			s = s.substring(0, s.length() - 1);
		}
		List<String> list = new ArrayList<String>();

		if (breakpoints.size() > 1)
			for (int i = 0; i < breakpoints.size(); i += 2)
				list.add(s.substring(breakpoints.get(i),
						breakpoints.get(i + 1) + 1));
		else
			list.add(s.substring(breakpoints.get(0), breakpoints.get(0) + 1));
		return list;
	}

	private static boolean isVowel(char c) {
		if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u')
			return true;
		else
			return false;
	}

	private static String convert(String romanWord, List<String> list) {
		String s = "";
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			if(syllableMap.containsKey(string))
				s += syllableMap.get((String) string);
			else
			{
				c++;
				return romanWord+"\\E";
			}
			//System.out.println(list.toString() + "  " + string);
		}
		return romanWord+"\\B="+s;
	}
	
	
	/**
	 * @param romanWord
	 * @return devanagriWord
	 */
	public String transliterate(String romanWord) {	
		String lowerRoman = romanWord.toLowerCase();
		String devanagriWord = "";
		if (syllableMap.containsKey(lowerRoman)) {
			a++;
			devanagriWord = syllableMap.get(lowerRoman);
			if(devanagriWord.endsWith("**"))
				return romanWord+"\\E";
			else
				return romanWord+"\\B="+devanagriWord;
//			System.out.println(romanWord);
		} else {
			b++;
			List<String> l = syllabify(lowerRoman);
//			System.out.println(l.toString());
			return convert(romanWord, l);
		}		
	}

	public String correction(String englishWord){
		if(syllableMap.containsKey(englishWord.toLowerCase())){
			String devanagriWord = syllableMap.get(englishWord.toLowerCase());
			if(devanagriWord.endsWith("**"))
				return englishWord+"\\E";
			else
			{
				d++;
				return englishWord+"\\B="+devanagriWord;
			}
		}
		else
			return englishWord+"\\E";
	}

}
