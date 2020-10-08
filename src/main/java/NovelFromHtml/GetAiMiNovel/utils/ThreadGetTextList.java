package NovelFromHtml.GetAiMiNovel.utils;

import NovelFromHtml.Interface.AThreadGetTextList;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ThreadGetTextList extends AThreadGetTextList {
    public static void main(String[] args) throws IOException {
        ThreadGetTextList t = new ThreadGetTextList(0, 0, 0, null);
        String sectionText = t.getSectionText("https://www.amxsw.com/am/97/97719/37873561.html");
        System.out.println(sectionText);
    }
    public ThreadGetTextList(int nowThread, int thread_number, int threadSection, List<String> allSectionAddress) {
        super(nowThread, thread_number, threadSection, allSectionAddress);
    }

    @Override
    public String getSectionText(String section_address) throws IOException {
        Connection con = Jsoup.connect(section_address).userAgent("Mozilla/5.0 (Windows NT 10.0; ??) Gecko/20100101 Firefox/74.0");
        Document doc = con.get();
        StringBuffer sb = new StringBuffer();

        String title = doc.select("div[class=nr_title]").get(0).child(0).text();
        sb.append("    ");
        sb.append(title);
        sb.append('\n');
        Elements ps = doc.getElementById("articlecontent").select("p");
        for(int i=0;i<ps.size()-1;i++){
            sb.append("  ");
            sb.append(ps.get(i).text());
            sb.append('\n');
        }
        return sb.toString();
    }
}
