import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class search_string{
	
	public static boolean isMatra(char letter)
	{
		if (letter=='ा' || letter=='ि' || letter=='ी' || letter=='ु' || letter=='ू' || letter=='ॅ' || letter=='ॆ' || letter=='े' || letter=='ै' || letter=='ॉ' || letter=='ॊ' || letter=='ो' || letter=='ौ' ||  letter=='ँ' || letter=='ं' || letter=='ः' || letter=='़' || letter=='्' || letter=='।' || letter=='ृ' || letter=='ॄ' || letter=='ॣ' || letter=='ॢ' || letter=='ऽ' || letter=='अ' || letter=='आ' || letter=='इ' || letter=='ई' || letter=='उ' || letter=='ऊ' || letter=='ए' || letter=='ऐ' || letter=='ऑ' || letter=='ओ' || letter=='औ')
			return true;
		else
			return false;
	}
	public static String transR2H(String s, int pindex, int index) throws FileNotFoundException, UnsupportedEncodingException
	{
		String h = "";
				if(pindex != index)
				{
					String urlString = s.substring(pindex, index);

						
					Tester t = new Tester(urlString);
					if(t.devanagari.indexOf("\\B") >= 0)
					{
						h += t.devanagari.substring(t.devanagari.indexOf("\\B")+3,t.devanagari.length());
						
					}
					else
					{
					try {
						//System.out.print("<<"+urlString+">>");
						
						URL url = new URL("http://transliteration.yahooapis.com/v1/transliterate?word="+urlString+"&lang=hi-IN&dummy=YUI.Env.DataSource.callbacks.yui_3_9_1_3_1414033003493_189");  
				    	URLConnection connection = url.openConnection();
			          	connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			         	//connection.setRequestProperty("Cookie","fbm_210461222454874=base_domain=.ktj.in; GCSCU_406502212247_H3=C=406502212247.apps.googleusercontent.com:S=d1ca5dc50137937970a5766617c228e57c602dc6..52ad:I=1390494016:X=1390497616; G_AUTHUSER_U3=0; sessionid=ek5dicbzu4m7zsxc96lnbd5iiq7aln4r; _ga=GA1.2.921284729.1382003895; csrftoken=pDfwckvKwVmpwrvCFopm8iqPiqlvAFD9");
			          //	connection.connect();
			          	connection.setDoOutput(true); 
						OutputStreamWriter out1 = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");   
						out1.flush();   
						out1.close();
						BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
				        String line = "";
				        String uwrite = "";
				       while((line=in.readLine())!=null) {
				    	   uwrite = line.substring(line.indexOf("\\\"r\\\": \\")+9,(line.indexOf("|") >= 0)?line.indexOf("|"):line.indexOf("\\\"}"));
				          h += uwrite;
				       }
						
				        in.close();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						//System.out.println(" MalformedURLException");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						//System.out.println(" UnsupportedEncodingException");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//System.out.println(" IOException");
					} 
					}
				}
				else
				{
					
						h += s.charAt(index);
					//System.out.print(s.charAt(index));
					
				}
				return h;

	}
	
	
	
	

	
	public static String transH2R(String s, int pindex, int index) throws FileNotFoundException, UnsupportedEncodingException
	{
		String r = "";
				if(pindex != index)
				{
					String letters = s.substring(pindex, index);

					for(int i = 0; i < letters.length(); i++)
					{
						char letter = letters.charAt(i);
						
						if (letter=='क') {r+= "k";}
						else if (letter=='ख') {r += "kh";}
						else if (letter=='ग') {r += "g";}
						else if (letter=='घ') {r += "gh";}
						else if (letter=='ङ') {r += "ng";}
						else if (letter=='च') {r += "ch";}
						else if (letter=='छ') {r += "ch";}
						else if (letter=='ज') {r += "j";}
						else if (letter=='झ') {r += "jh";}
						else if (letter=='ञ') {r += "ny";}
						else if (letter=='ट') {r += "t";}
						else if (letter=='ठ') {r += "th";}
						else if (letter=='ड') {r += "d";}
						else if (letter=='ढ') {r += "dh";}
						else if (letter=='ण') {r += "n";}
						else if (letter=='त') {r += "t";}
						else if (letter=='थ') {r += "th";}
						else if (letter=='द') {r += "d";}
						else if (letter=='ध') {r += "dh";}
						else if (letter=='न') {r += "n";}
						else if (letter=='प') {r += "p";}
						else if (letter=='फ') {r += "f";}
						else if (letter=='ब') {r += "b";}
						else if (letter=='भ') {r += "bh";}
						else if (letter=='म') {r += "m";}
						else if (letter=='य') {r += "y";}
						else if (letter=='र') {r += "r";}
						else if (letter=='ल') {r += "l";}
						else if (letter=='व') {r += "v";}
						else if (letter=='श') {r += "sh";}
						else if (letter=='ष') {r += "s";}
						else if (letter=='स') {r += "s";}
						else if (letter=='ह') {r += "h";}
						else if (letter=='क़') {r += "k";}
						else if (letter=='ख़') {r += "kh";}
						else if (letter=='ग़') {r += "g";}
						else if (letter=='ऩ') {r += "n";}
						else if (letter=='ड़') {r += "d";}
						else if (letter=='ढ') {r += "dh";}
						else if (letter=='ढ़') {r += "rh";}
						else if (letter=='ऱ') {r += "r";}
						else if (letter=='य़') {r += "ye";}
						else if (letter=='ळ') {r += "l";}
						else if (letter=='ऴ') {r += "ll";}
						else if (letter=='फ़') {r += "f";}
						else if (letter=='ज़') {r += "z";}
						else if (letter=='ऋ') {r += "ri";}
						else if (letter=='ा') {r += "aa";}
						else if (letter=='ि') {r += "i";}
						else if (letter=='ी') {r += "i";}
						else if (letter=='ु') {r += "u";}
						else if (letter=='ू') {r += "u";}
						else if (letter=='ॅ') {r += "e";}
						else if (letter=='ॆ') {r += "e";}
						else if (letter=='े') {r += "e";}
						else if (letter=='ै') {r += "ai";}
						else if (letter=='ॉ') {r += "o";}
						else if (letter=='ॊ') {r += "o";}
						else if (letter=='ो') {r += "o";}
						else if (letter=='ौ') {r += "au";}
						else if (letter=='अ') {r += "a";}
						else if (letter=='आ') {r += "aa";}
						else if (letter=='इ') {r += "i";}
						else if (letter=='ई') {r += "i";}
						else if (letter=='उ') {r += "u";}
						else if (letter=='ऊ') {r += "oo";}
						else if (letter=='ए') {r += "e";}
						else if (letter=='ऐ') {r += "ai";}
						else if (letter=='ऑ') {r += "au";}
						else if (letter=='ओ') {r += "o";}
						else if (letter=='औ') {r += "au";}
						else if (letter=='ँ') {r += "n";}
						else if (letter=='ं') {r += "n";}
						else if (letter=='ः') {r += "ah";}
						else if (letter=='़') {r += "e";}
						else if (letter=='्') {r += "";}
						else if (letter=='०') {r += "0";}
						else if (letter=='१') {r += "1";}
						else if (letter=='२') {r += "2";}
						else if (letter=='३') {r += "3";}
						else if (letter=='४') {r += "4";}
						else if (letter=='५') {r += "5";}
						else if (letter=='६') {r += "6";}
						else if (letter=='७') {r += "7";}
						else if (letter=='८') {r += "8";}
						else if (letter=='९') {r += "9";}
						else if (letter=='।') {r += ".";}
						else if (letter=='ऍ') {r += "e";}
						else if (letter=='ृ') {r += "ri";}
						else if (letter=='ॄ') {r += "rr";}
						else if (letter=='ॠ') {r += "r";}
						else if (letter=='ऌ') {r += "l";}
						else if (letter=='ॣ') {r += "l";}
						else if (letter=='ॢ') {r += "l";}
						else if (letter=='ॡ') {r += "l";}
						else if (letter=='ॿ') {r += "b";}
						else if (letter=='ॾ') {r += "d";}
						else if (letter=='ॽ') {r += "";}
						else if (letter=='ॼ') {r += "j";}
						else if (letter=='ॻ') {r += "g";}
						else if (letter=='ॐ') {r += "om";}
						else if (letter=='ऽ') {r += "'";}
					//	else if (letter=='e.a') {r += "a";}
						else if (letter=='\n') {r += "\n";}
						else {r += letter;}
						if((i+1 != letters.length()) && !(isMatra(letters.charAt(i+1))) && !(isMatra(letter)))
							r += "a";
					}
				
				}
				else
				{
						r += s.charAt(index);
				
					//System.out.print(s.charAt(index));
				}
				//System.out.print("<<"+r);
				return r;

	}
	
	
	
	

	private int flag;
	private String str_arr;
	
	public search_string(String ln ,int fl)
	{
		this.str_arr = ln;
		this.flag = fl;
	}
	public String text()  throws FileNotFoundException, UnsupportedEncodingException
	{
		   String hcontent = "";
		   String rcontent = "";
		   System.setProperty("http.proxyHost","10.3.100.207");
				System.setProperty("http.proxyPort", "8080");
				System.setProperty("socksProxySet", "true");
				 System.setProperty("socksProxyHost","10.3.100.207");
				 System.setProperty("socksProxyPort","8080");
				 System.setProperty("https.proxyHost", "10.3.100.207");
				 System.setProperty("https.proxyPort", "8080");
		   String s=str_arr;
			
		   if( s!= null && s.compareTo("") != 0 && flag == 1){
				   int index = 0;
				   int pindex = 0;
				   while(index < s.length())
				   {
					   pindex = index;
					   while(index < s.length() && Character.UnicodeBlock.of(s.charAt(index)).toString() == "DEVANAGARI")
						   index++;
					   rcontent += transH2R(s,pindex,index);
					   if(pindex == index)
						   index++;
				   } 
				 return rcontent;
			}
			else if(flag == 1)
				return s;

		 
		   s = null;
		   s = str_arr;

		   if( s!= null && s.compareTo("") != 0 && flag == 0){
				   int index = 0;
				   int pindex = 0;
				   while(index < s.length())
				   {
					   pindex = index;
					   char ch;  
					   while(index < s.length() &&  (((ch = s.charAt(index)) <= 90 && ch >= 65) || (ch >= 97 && ch <= 122)))
						  index++;
					   hcontent += transR2H(s,pindex,index);
					   if(pindex == index)
						   index++;
				   } 
				  return hcontent;
			    }
			else if(flag == 0)
			   return s;
			else return "Error";

		 
		   
		  

		} 

}
