import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;


public class Tester {
	String devanagari;
	public Tester(String str) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub		
		
		Transliterator transliterator = new Transliterator("hindi_syllables.txt");
		devanagari = transliterator.transliterate(str);
	}
	public String toText()
	{
		return devanagari;
	}

}
