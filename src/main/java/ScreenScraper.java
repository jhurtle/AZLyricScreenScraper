import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class ScreenScraper {

    public ScreenScraper(){

    }

    public Song getSong(Document doc,String songName){
        Element element = doc.body();
        Element e;
        //check for the feat class in doc
        try {
            e = element.getElementsByClass("feat").get(0).nextElementSibling().
                    nextElementSibling().nextElementSibling();
        }
        catch(IndexOutOfBoundsException i) {
            //if no feat class get ringtone
            try{
            e = element.getElementsByClass("ringtone").get(0).nextElementSibling();
            while(e != null && !e.tagName().equalsIgnoreCase("div")) {
                e = e.nextElementSibling();
            }
            }
            catch(Exception x){
                System.out.println("Error in lyric scraping\nError:  " + x.toString());
                return new Song();
            }
        }
        e.select("br").append("\n");
        //e.select("p").prepend("\\n\\n");
        String s = e.html().replaceAll("\\\\n","\n");
        return new Song(songName,Jsoup.clean(s,"", Whitelist.none(), new Document.OutputSettings().prettyPrint(false)));
    }

    public Artist getArtistAlbums(String htmlRoot,String artistName)throws InterruptedException,MalformedURLException{
        Document doc;
        try{
            doc = Jsoup.connect(htmlRoot).get();
        }
        catch(IOException io){
            System.out.println( "Artist Page not found\nError: " + io.toString());
            return null;
        }
        //get related Artist
        Elements links = doc.select("a[href]");
        int i = 28;
        List relatedArtist = new ArrayList();
        while(!links.get(i).className().equalsIgnoreCase("btn btn-xs btn-default sorting")){
            relatedArtist.add(links.get(i).text());
            i++;
        }
        Element element = doc.body();
        Elements albumElements = element.getElementsByClass("album");
        List<Album> albumList = new ArrayList();
        for(Element album: albumElements){
            String albumName = album.text().substring(album.text().indexOf(" "),album.text().lastIndexOf(" "));
            String year = album.text().substring(album.text().lastIndexOf(" "),album.text().length());
            List<Song> songs = new ArrayList();
            Element iter = album.nextElementSibling();
            //System.out.println(iter.text());
            while(!iter.className().equalsIgnoreCase("album")){
                if(!iter.className().equalsIgnoreCase("comment")
                        && !iter.tagName().equalsIgnoreCase("br")
                        && !iter.text().equalsIgnoreCase("")) {
                    String urlArtist = artistName;
                    String urlSong = iter.text();
                    urlArtist = urlArtist.toLowerCase();
                    urlArtist = urlArtist.replaceAll("['|(|)| |/|.|?|\\-|,|?|!]","");
                    urlSong = urlSong.toLowerCase();
                    urlSong = urlSong.replaceAll("['|(|)| |/|.|?|\\-|,|?|!]","");
                    String songHTML = "https://www.azlyrics.com/lyrics/" + urlArtist +"/"+urlSong + ".html";

                    Document newDoc;
                    try {
                        newDoc = Jsoup.connect(songHTML).get();
                        songs.add(getSong(newDoc,iter.text()));
                        System.out.println("Success: " + artistName + ": " + iter.text());
                    }
                    catch(IOException x) {
                        System.out.println("Song not found\nError: "+x.toString());
                    }
                }
                iter = iter.nextElementSibling();
                System.out.println(iter.tagName());
                //wait 30 seconds between each request
                Thread.sleep(30000);
            }
            albumList.add(new Album(albumName,year,songs));
        }

        return new Artist(artistName, albumList);
    }

}
