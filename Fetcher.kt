import com.google.gson.Gson
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.*
import kotlin.collections.ArrayList


// Constants
const val YOUTUBE_URL:String = "https://www.youtube.com/results?search_query=";
const val YOUTUBE_VIDEO_URL:String = "https://www.youtube.com";

/*
    remove the spaces in a given arrayList
    @return the arraylist
 */
fun ArrayList<String>.removeSpaces() : ArrayList<String> {
    var new = ArrayList<String>();
    for (i in this) {
        if (!i.isBlank())
            new.add(i);
    }
    return new;
}

/*
    Represents a single found source
    @param link the link
 */
data class YoutubeVideoFetcher(var link:String) {
    init {
        this.link = this.youtubeURL();
    }
    fun youtubeURL():String = YOUTUBE_VIDEO_URL.plus(link);
}

/*
    The main class to perform search functions
 */
class YouTubeFetcher() {
    companion object {
        fun search(query:String) : ArrayList<YoutubeVideoFetcher>{
            var listOfLinks:ArrayList<String> = ArrayList<String>();
            var ytubeVideoFetcher = ArrayList<YoutubeVideoFetcher>();
            var doc: Document = Jsoup.connect("https://www.youtube.com/results?search_query=${query}").userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://www.google.com")
                .get();
            println("Accessing: " + doc.title())
            var parsedData = Jsoup.parse(doc.html())
            var links = parsedData.select("a[href]")
            for (link in links) {
                if (link.toString().equals(" ")) {
                    continue
                }
                listOfLinks.add(link.getElementsByClass("yt-uix-tile-link yt-ui-ellipsis yt-ui-ellipsis-2 yt-uix-sessionlink      spf-link ").attr("href").toString())
            }
            listOfLinks = listOfLinks.removeSpaces()
            for (link in listOfLinks) {
                ytubeVideoFetcher.add(YoutubeVideoFetcher(link))
            }
            return ytubeVideoFetcher;
        }
    }
}