package NovelFromHtml.GetDingDianNovel;

import Bean.NovelInformation;
import NovelFromHtml.GetDingDianNovel.utils.NovelForNovelText;
import NovelFromHtml.Interface.AThreadGetTextList;
import NovelFromHtml.Interface.IGetNovelBySearch;
import NovelWriteDown.NovelToFile;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DingDianSearch implements IGetNovelBySearch {

    public static void main(String[] args) throws IOException {
        DingDianSearch dd = new DingDianSearch();
        dd.domain("斗罗大陆");

    }

    @Override
    public NovelInformation getNovelAddress(String SearchAddress) throws IOException {
        Connection con = Jsoup.connect(SearchAddress).userAgent("Mozilla/5.0 (Windows NT 10.0; …) Gecko/20100101 Firefox/74.0");
        Connection.Response response = con.execute();
        URL url = response.url();
        if(url.toString().matches("https://www.e1w.net/book.*")){
            System.out.println("------------顶点网站已查到该书籍-----------");
            return getNovelInformationByNovelAddress(url.toString());
        }else {
            List<NovelInformation> novelInformations = parseNovelAddress(SearchAddress);
            if (novelInformations.size() == 0) {
                System.out.println("------------顶点网站未查到该书籍-----------");
                return null;
            }
            System.out.println("----------顶点网站查找到以下结果-----------");
            System.out.println("------------0  : 退出-------------");
            System.out.printf("%-50s\t\t%-30s\t\t%-30s\n", "书名", "作者", "类型");
            int now = 1;
            for (NovelInformation novel : novelInformations) {
                System.out.printf("%-20d%-30s\t%-30s%-30s\n", now++, novel.getNovel_name(), novel.getAuthor_name(), novel.getType());
            }

            System.out.println("请输入书籍编号");
            Scanner sc = new Scanner(System.in);
            int novelNo = sc.nextInt();

            if (novelNo < 0 || novelNo > novelInformations.size() || novelNo == 0) return null;
            return novelInformations.get(novelNo - 1);
        }
    }

    @Override
    public String parseHtmlEncoding(String novelName){
        if(novelName==null)return null;
        try {
           return   URLEncoder.encode(novelName,"gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    //可改进多线程搜索
    @Override
    public List<NovelInformation> parseNovelAddress(String address) throws IOException {
        List<NovelInformation> novelInformationList = new ArrayList<>();
        Connection con = Jsoup.connect(address).userAgent("Mozilla/5.0 (Windows NT 10.0; …) Gecko/20100101 Firefox/74.0").postDataCharset("gbk");
        Document doc = con.post();

        int pages = Integer.parseInt(doc.getElementById("pagestats").text().replaceAll("1/",""));

        for(int page=1;page<=pages;page++){
            Element table = doc.select("table[class=grid]").get(0);
            Elements trs = table.select("tr");

            for(int i=1;i<trs.size();i++){
                Element tr = trs.get(i);
                String href = tr.child(0).child(0).attr("href");

                NovelInformation novelInformation = getNovelInformationByNovelAddress(href);
                novelInformationList.add(novelInformation);
            }
            con = Jsoup.connect(address+"&page="+(page+1)).userAgent("Mozilla/5.0 (Windows NT 10.0; …) Gecko/20100101 Firefox/74.0").postDataCharset("gbk");
            doc = con.get();
        }

        return novelInformationList;
    }

    @Override
    public NovelInformation getNovelInformationByNovelAddress(String novel_address) throws IOException {
        NovelInformation novelInformation = new NovelInformation();
        Document doc = Jsoup.parse(new URL(novel_address), 10000);
        String novel_name = doc.select("h1").get(0).text().replaceAll("全文阅读", "").trim();
        Element table = doc.getElementById("at");
        Elements td = table.select("tr").get(0).select("td");
        String novel_type = td.get(0).child(0).text();
        String author_name = td.get(1).text();

        novelInformation.setNovel_name(novel_name);
        novelInformation.setAuthor_name(author_name);
        novelInformation.setType(novel_type);
        novelInformation.setNovel_address(novel_address);
        return novelInformation;
    }

    @Override
    public String getSearchAddress(String novel_name) {
        String url = parseHtmlEncoding(novel_name);
        return "https://www.e1w.net/modules/article/search.php?searchtype=articlename&searchkey="+url;
    }

    @Override
    public boolean domain(String NovelName) {
        try {
            System.out.println("顶点网站正在查找......");
            String searchAddress = getSearchAddress(NovelName);
            NovelInformation info = getNovelAddress(searchAddress);
            if(info!=null){
                NovelToFile file = new NovelToFile(info);
                if(file.fileExist()){
                    System.out.println("小说已存在,是否覆盖：y/n");
                    Scanner sc = new Scanner(System.in);
                    String next = sc.next();
                    if("n".equals(next))return true;
                }
                String catalogAddress = info.getNovel_address().replaceAll("book","read").replaceAll(".html","/index.html");
                NovelForNovelText getAllSection = new NovelForNovelText();
                Class<AThreadGetTextList> threadGetTextListClass = (Class<AThreadGetTextList>) Class.forName("NovelFromHtml.GetDingDianNovel.utils.ThreadGetTextList");
                List<String> novelLists = getAllSection.getNovel(catalogAddress,threadGetTextListClass);
                file.writeDownNovel(novelLists);
                return true;
            }
        } catch (IOException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
