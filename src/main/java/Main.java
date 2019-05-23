import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static java.lang.System.out;

import javax.print.Doc;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException{
        ScreenScraper scraper = new ScreenScraper();
        //System.out.print(scraper.getArtistAlbums("https://www.azlyrics.com/v/vancejoy.html","vancejoy"));
        String cvsFile = System.getProperty("user.dir")+"//AZLyricScreenScraper//TopArtists.txt";
        BufferedReader br = null;
        String siteURL = "https://www.azlyrics.com/";
        List<Artist> artistList = new ArrayList();
        String artistName;
        try{
            br = new BufferedReader(
                    new FileReader(cvsFile));
            out.println("Successs");
        }
        catch(IOException e){
            out.println("Fail"+e.toString());
        }
        while((artistName = br.readLine())!= null){
            out.println(artistName);
            String[] nameSplit = artistName.split(" ");
            String[] letter = new String[nameSplit.length];
            for(int i = 0; i < nameSplit.length;i++){
                letter[i] = String.valueOf(nameSplit[i].charAt(0)).toLowerCase();
            }
            try{
                for(String s: letter){
                    String urlArtist = artistName.replaceAll("['|(|)| |/|.|?|\\-|,|?|!|&| ]","").toLowerCase();
                    String url = siteURL + s+"/"+urlArtist+".html";
                    out.println("TEST: "+url);
                    Artist temp = scraper.getArtistAlbums(urlArtist,url);
                    if(temp != null){
                        artistList.add(temp);
                        break;
                    }
                }
            }
            catch(IOException e){

            }

        }
        //create directory
        Gson gson = new GsonBuilder().create();
        String location = System.getProperty("user.dir")+"//Artists";
        File file = new File(location);
        if(!file.exists()){
            file.mkdir();
            out.println("Artist Directory Created");
        }
        for(Artist artist: artistList){
            String artistLocation = location + "//" + artist.getName();
            File artistDirectory = new File(artistLocation);
            if(!artistDirectory.exists()){
                artistDirectory.mkdir();
                out.println(artist.getName()+" Directory Created");
            }
            for(Album album: artist.getAlbums()){
                String albumLocation = artistLocation + "//" + album.getAlbumName();
                File albumDirectory = new File(albumLocation);
                if(!albumDirectory.exists()){
                    albumDirectory.mkdir();
                    out.println(album.getAlbumName() + "Directory Created");
                }
                for(Song song: album.getSongs()){
                    String songLocation = albumLocation + "//" + song.getSongName();
                    File songDirectory = new File(songLocation);
                    if(!songDirectory.exists()){
                        songDirectory.mkdir();
                        out.println(song.getSongName() + "Directory Created");
                    }
                    Writer writer = new FileWriter(songLocation + "//"+song.getSongName()+".json");
                    gson.toJson(song,writer);
                    writer.close();
                }
                Writer writer = new FileWriter(albumLocation + "//"+album.getAlbumName()+".json");
                gson.toJson(album,writer);
                writer.close();
            }
            Writer writer = new FileWriter(artistLocation + "//"+artist.getName()+".json");
            gson.toJson(artist,writer);
            writer.close();
        }

    }
}
