import java.util.ArrayList;
import java.util.List;

public class Album {
    String albumName;
    String year;
    List<Song> songs = new ArrayList();

    public Album(String albumName,String year,List songs){
        this.albumName = albumName;
        this.year = year;
        this.songs = new ArrayList<Song>(songs);
    }

    public Album(){

    }


    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List songs) {
        this.songs = songs;
    }
}
