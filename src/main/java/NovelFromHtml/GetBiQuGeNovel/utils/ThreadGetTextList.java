package NovelFromHtml.GetBiQuGeNovel.utils;

import NovelFromHtml.Interface.AThreadGetTextList;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class ThreadGetTextList extends AThreadGetTextList{

    public ThreadGetTextList(int nowThread, int thread_number, int threadSection, List<String> allSectionAddress) {
        super(nowThread, thread_number, threadSection, allSectionAddress);
    }


    public String getSectionText(String section_address){
        try{
            Connection con = Jsoup.connect(section_address).userAgent("Mozilla/5.0 (Windows NT 10.0; ??) Gecko/20100101 Firefox/74.0").timeout(30000);
            Connection.Response response = con.response();
            if(response.statusCode()==404){
                System.out.println(section_address+"不可访问");
                return "";
            }
            Document doc = con.get();

            String contentText;

            String sectionName = doc.select("div[class=bookname]").get(0).child(0).text();

            Element novel = doc.select("div[id=content]").get(0);

            Elements brs = novel.select("br");
            for(Element e:brs)e.text(" ");
            String novelText = novel.text();
            novelText = "  "+novelText.replaceAll(" ", "\n  ");
            contentText = "    "+sectionName+"\n"+novelText;
            return contentText;
        }catch (Exception e){
            System.out.println(section_address+"访问失败");
            return "";
        }
    }

}
