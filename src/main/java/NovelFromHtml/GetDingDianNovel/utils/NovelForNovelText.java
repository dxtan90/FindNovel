package NovelFromHtml.GetDingDianNovel.utils;

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
    /**
     * 获取所有章节地址
     * @param novel_address 文章地址
     * @return 所有章节地址
     * @throws IOException
     */
    public List<String> getAllSectionAddress(String novel_address) throws IOException {
        Connection con = Jsoup.connect(novel_address).userAgent("Mozilla/5.0 (Windows NT 10.0; ??) Gecko/20100101 Firefox/74.0");
        Document doc = con.get();

        Elements dd = doc.select("td[class=L]");
        System.out.println(dd.get(0).childNodeSize());
        List<String> allSectionAddress = new ArrayList<>();

        for(Element d:dd){
            if(d.children().size()==1){
                Element a = d.child(0);
                if(a.hasAttr("href")){
                    String href =  a.attr("href");
                    String section = novel_address.replaceAll("index.html",href);
                    allSectionAddress.add(section);
                }
            }
        }
        return allSectionAddress;
    }
}
