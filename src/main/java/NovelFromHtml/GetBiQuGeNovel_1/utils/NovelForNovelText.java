package NovelFromHtml.GetBiQuGeNovel_1.utils;

import Bean.NovelInformation;
import NovelFromHtml.Interface.INovelForNovelText;
import NovelWriteDown.NovelToFile;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NovelForNovelText implements Runnable, INovelForNovelText {
    protected NovelInformation novel;

    public NovelForNovelText(NovelInformation novel) {
        this.novel = novel;
    }

    /**
     * 获取所有章节地址
     * @param novel_address 文章地址
     * @return 所有章节地址
     * @throws IOException
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
        System.out.println(allSectionAddress);
        return allSectionAddress;
    }

    /**
     * 获取某章节文本
     * @param section_address 章节地址
     * @return 某章节文本
     * @throws IOException
     */
    public String getSectionText(String section_address) throws IOException {
        Connection con = Jsoup.connect(section_address).userAgent("Mozilla/5.0 (Windows NT 10.0; ??) Gecko/20100101 Firefox/74.0");
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
    }

    /**
     * 获得文章所有章节文本
     * @param novel_address 文章地址
     * @return 章节文本列表
     * @throws IOException
     */
    public List<String> getNovel(String novel_address) throws IOException  {
        List<String> novelList = new ArrayList<>();
        List<String> allSectionAddress = getAllSectionAddress(novel_address);
        for(int i=0;i<allSectionAddress.size();i++){
            try {
                String section_address = allSectionAddress.get(i);
                String sectionText = getSectionText(section_address);
                novelList.add(sectionText);
                if(i%20==0){
                    System.out.println("had get:\t"+novel.getNovel_name()+"\t"+i+"\tsection");
                }
            }catch (Exception e){
                i=i-1;
                continue;
            }

        }
        return novelList;
    }


    @Override
    public void run() {
        try {
            List<String>  novelList = getNovel(this.novel.getNovel_address());
            new NovelToFile(novel).writeDownNovel(novelList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NovelInformation getNovel() {
        return novel;
    }

    public void setNovel(NovelInformation novel) {
        this.novel = novel;
    }

}
