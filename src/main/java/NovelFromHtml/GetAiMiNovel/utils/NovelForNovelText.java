package NovelFromHtml.GetAiMiNovel.utils;

import NovelFromHtml.Interface.ANovelForNovelText;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NovelForNovelText extends ANovelForNovelText {

    @Override
    public List<String> getAllSectionAddress(String novel_address) throws IOException {
        Connection con = Jsoup.connect(novel_address).userAgent("Mozilla/5.0 (Windows NT 10.0; ??) Gecko/20100101 Firefox/74.0");
        Document doc = con.get();
        List<String> secAddress = new ArrayList<>();
        Elements lis = doc.select("div[class=ml_list]").select("li");
        for(Element li:lis){
            secAddress.add("https://www.amxsw.com"+li.child(0).attr("href"));
        }
        return secAddress;
    }
}
