import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
 
public class MyHandler extends DefaultHandler {
 
    //List to hold Employees object
    private List<textdoc> docList = null;
    private textdoc doc = null;
 
    public List<textdoc> getDocList() {
        return docList;
    }
 
 
    StringBuilder text = new StringBuilder();
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
 
        if (qName.equalsIgnoreCase("doc")) {
            doc = new textdoc();
            //initialize list
            if (docList == null)
                docList = new ArrayList<>();
        } else if (qName.equalsIgnoreCase("id")) {
            text = new StringBuilder();
        } else if (qName.equalsIgnoreCase("doc_title")) {
            text = new StringBuilder();
        } else if (qName.equalsIgnoreCase("song_title_hindi")) {
        	text = new StringBuilder();
        } else if (qName.equalsIgnoreCase("song_title_english")) {
        	text = new StringBuilder();
        } else if (qName.equalsIgnoreCase("song_content_hindi")) {
        	text = new StringBuilder();
        } else if (qName.equalsIgnoreCase("song_content_english")) {
        	text = new StringBuilder();
        } 
    }
 
 
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
      
        if (qName.equalsIgnoreCase("doc")) {
            docList.add(doc);
        } else if (qName.equalsIgnoreCase("id")) {
            doc.set_id(Integer.parseInt(text.toString().trim()));
        } else if (qName.equalsIgnoreCase("doc_title")) {
            doc.set_doc_title(text.toString());
        } else if (qName.equalsIgnoreCase("song_title_hindi")) {
            doc.set_song_title_hindi(text.toString());
        } else if (qName.equalsIgnoreCase("song_title_english")) {
            doc.set_song_title_english(text.toString());
        } else if (qName.equalsIgnoreCase("song_content_hindi")) {
            doc.set_song_content_hindi(text.toString());
        } else if (qName.equalsIgnoreCase("song_content_english")) {
            doc.set_song_content_english(text.toString());
        }
    }
 
 
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
    	text.append(ch, start, length);
    }
}