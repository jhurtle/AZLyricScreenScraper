public class Song {
    String songName;
    String songLyrics;

    public Song(String songName, String songLyrics){
        this.songName = songName;
        this.songLyrics = songLyrics;
    }
    public Song(){

    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongLyrics() {
        return songLyrics;
    }

    public void setSongLyrics(String songLyrics) {
        this.songLyrics = songLyrics;
    }
}
