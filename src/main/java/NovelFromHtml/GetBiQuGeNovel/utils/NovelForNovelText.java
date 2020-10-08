package NovelFromHtml.GetBiQuGeNovel.utils;

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
     * @throws IOException io
     */
    public List<String> getAllSectionAddress(String novel_address) throws IOException {
        Connection con = Jsoup.connect(novel_address).userAgent("Mozilla/5.0 (Windows NT 10.0; ??) Gecko/20100101 Firefox/74.0");
        Document doc = con.get();

        Elements dd = doc.select("dd");

        List<String> allSectionAddress = new ArrayList<>();

        for(Element d:dd){
            if(d.childNodeSize()==1){
                String href = d.child(0).attr("href");
                href  = novel_address + href;
                allSectionAddress.add(href);
            }
        }
        return allSectionAddress;
    }
}
