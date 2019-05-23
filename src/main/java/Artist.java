import java.util.ArrayList;
import java.util.List;

public class Artist {
    String name;
    List albums = new ArrayList<Album>();
    public Artist(String name, List albums){
        this.name = name;
        this.albums = new ArrayList<Album>(albums);
    }

    public Artist(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List albums) {
        this.albums = albums;
    }
}
