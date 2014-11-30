
public class textdoc 
{
	private int id;
    private String doc_title, song_title_hindi, song_title_english, song_content_hindi, song_content_english;
     
    public int get_id() {
        return id;
    }
    public void set_id(int num) {
        this.id = num;
    }
    public String get_doc_title() {
        return doc_title;
    }
    public void set_doc_title(String name) {
        this.doc_title = name;
    }
    public String get_song_title_hindi() {
        return song_title_hindi;
    }
    public void set_song_title_hindi(String name) {
        this.song_title_hindi = name;
    }
    public String get_song_title_english() {
        return song_title_english;
    }
    public void set_song_title_english(String name) {
        this.song_title_english = name;
    }
    public String get_song_content_hindi() {
        return song_content_hindi;
    }
    public void set_song_content_hindi(String name) {
        this.song_content_hindi = name;
    }
    public String get_song_content_english() {
        return song_content_english;
    }
    public void set_song_content_english(String name) {
        this.song_content_english = name;
    }
}
