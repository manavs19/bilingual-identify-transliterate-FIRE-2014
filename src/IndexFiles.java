import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.hi.HindiAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.xml.sax.SAXException;
 


@SuppressWarnings("deprecation")
public class IndexFiles {

	/**
	 * @param args
	 * @throws Throwable 
	 * @throws SAXException 
	 */
	static int sc = 0;
/*	public static void main(String[] args) throws SAXException, Throwable {
		Scanner in = new Scanner(System.in);
		int a=4;
		String b="";
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		while(a>0)
		{
			System.out.println("Enter your choice of corpus: ");
			System.out.println("1: Roman script (Use existing index). ");
			System.out.println("2: Roman script (Recreate index). ");
			System.out.println("3: Devanagri script (Use existing index). ");
			System.out.println("4: Devanagri script (Recreate index). ");
			System.out.println("0: Exit code. ");
			a = in.nextInt();
			if(a<=0)
				System.exit(0);
			System.out.println("Enter the string to search: ");
			b = bf.readLine();
			make_index(a);
			search_index(b, a);
			
		}
	}
*/	
	public static ArrayList<Document> searchWrapper(String user_query, int corpus_type) 
	{				
		try {
			return search_index(user_query, corpus_type);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private static ArrayList<Document> search_index(String user_query, int corpus_type) throws IOException, Throwable, SAXException
	{
		File stpwrd = new File("stopwords_roman.txt");
		Analyzer analyzer = null;
		IndexReader reader = null;
		String query_str = " ";
		MultiFieldQueryParser parser = null;	
		switch (corpus_type)
		{
			case(1):
					reader = DirectoryReader.open(FSDirectory.open(new File("roman_index")));
					analyzer = new StopAnalyzer(Version.LUCENE_46, stpwrd);
					parser = new MultiFieldQueryParser(Version.LUCENE_46, new String[] {"song_title_english", "song_content_english"}, analyzer);
					query_str = (new search_string(user_query,1).text());
					break;
			case(2):
					reader = DirectoryReader.open(FSDirectory.open(new File("roman_index")));
					analyzer = new StopAnalyzer(Version.LUCENE_46, stpwrd);
					parser = new MultiFieldQueryParser(Version.LUCENE_46, new String[] {"song_title_english", "song_content_english"}, analyzer);
					query_str = (new search_string(user_query,1).text());
					break;
			case(3):
					reader = DirectoryReader.open(FSDirectory.open(new File("devanagri_index")));
					analyzer = new HindiAnalyzer(Version.LUCENE_46);
					parser = new MultiFieldQueryParser(Version.LUCENE_46, new String[] {"song_title_hindi", "song_content_hindi"}, analyzer);
					query_str = (new search_string(user_query,0).text());
					break;
			default:
					reader = DirectoryReader.open(FSDirectory.open(new File("devanagri_index")));
					analyzer = new HindiAnalyzer(Version.LUCENE_46);
					parser = new MultiFieldQueryParser(Version.LUCENE_46, new String[] {"song_title_hindi", "song_content_hindi"}, analyzer);
					query_str = (new search_string(user_query,0).text());
					break;
		}
		System.out.println(query_str);
		IndexSearcher searcher = new IndexSearcher(reader);
    	Query query = parser.parse(query_str);
    	TopScoreDocCollector collector = TopScoreDocCollector.create(100, true);
    	searcher.search(query, collector);    
//    	int numTotalHits = collector.getTotalHits();
    	ScoreDoc[] docs = collector.topDocs().scoreDocs;
    	ArrayList<Document> results = new ArrayList<Document>();
    	
    	for (int i = 0; i < docs.length; i++) 
    	{
    	  Document result = searcher.doc(docs[i].doc);
    	  results.add(result);
//    	  System.out.println((i+1) + "###" + result.get("doc_title")+ "###" + result.get("song_title_hindi")+ "###" + result.get("song_title_english"));
    	}
    	
    	return results;        
	}
	
	private static void make_index(int corpus_type) throws IOException, Throwable, SAXException
	{
		String input_file = "subtask2-data.xml";
		File stpwrd = new File("stopwords_roman.txt");
		Analyzer analyzer = null;
		Directory directory = null;
		switch (corpus_type)
		{

		case(1):
				return;
		case(2):
				directory = FSDirectory.open(new File("roman_index"));
				analyzer = new StopAnalyzer(Version.LUCENE_46, stpwrd);
				break;
		case(3):
				return;
		default:
				directory = FSDirectory.open(new File("devanagri_index"));
				analyzer = new HindiAnalyzer(Version.LUCENE_46);
				break;
		}
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
		IndexWriter iwriter = new IndexWriter(directory, config);
		iwriter.deleteAll();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
        MyHandler handler = new MyHandler();
        saxParser.parse(new File(input_file), handler);
        List<textdoc> docList = handler.getDocList();
        int cnt = 1;
        for(textdoc d : docList)
        {
        	Document doc = new Document();
    		doc.add(new LongField("id", cnt++, Field.Store.YES));
    		doc.add(new TextField("doc_title", d.get_doc_title(), Field.Store.YES));
    		if(corpus_type == 3 || corpus_type == 4)
    		{
    			doc.add(new TextField("song_title_hindi", d.get_song_title_hindi(), Field.Store.YES));
    			doc.add(new StoredField("song_title_english", d.get_song_title_english()));

    		}
    		else
    		{
    			doc.add(new TextField("song_title_english", d.get_song_title_english(), Field.Store.YES));
    			doc.add(new StoredField("song_title_hindi", d.get_song_title_hindi()));

    		}
    		if(corpus_type == 3 || corpus_type == 4)
    		{
    			doc.add(new TextField("song_content_hindi", d.get_song_content_hindi(), Field.Store.NO));
    		}
    		else
    		{
    			doc.add(new TextField("song_content_english", d.get_song_content_english(), Field.Store.NO));
    		}
    		iwriter.addDocument(doc);
        }
		iwriter.close();
	}

}
