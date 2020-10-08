package NovelFromHtml.GetAiMiNovel;

import Bean.NovelInformation;
import NovelFromHtml.GetAiMiNovel.utils.NovelForNovelText;
import NovelFromHtml.Interface.AThreadGetTextList;
import NovelFromHtml.Interface.IGetNovelBySearch;
import NovelWriteDown.NovelToFile;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AiMiSearch implements IGetNovelBySearch {
    public static void main(String[] args) throws IOException {
        AiMiSearch main = new AiMiSearch();
        main.domain("从荒岛开始");
    }
    @Override
    public NovelInformation getNovelAddress(String SearchAddress) throws IOException {
        List<NovelInformation> novelInformations = parseNovelAddress(SearchAddress);
        if(novelInformations.size()==0){
            System.out.println("------------艾米网站未查到该书籍-----------");
            return null;
        }
        System.out.println("----------艾米网站查找到以下结果-----------");
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
    //该网站适配中文地址搜索编码，无需解码
    @Override
    public String parseHtmlEncoding(String novelName) {
        return novelName;
    }

    @Override
    public List<NovelInformation> parseNovelAddress(String address) throws IOException {
        List<NovelInformation> novelInformationList = new ArrayList<>();
        Connection con = Jsoup.connect(address).userAgent("Mozilla/5.0 (Windows NT 10.0; …) Gecko/20100101 Firefox/74.0").postDataCharset("gbk");
        Document doc = con.post();

        Elements trs = doc.getElementById("rankinglist").select("tr");
        for(int i=1;i<trs.size();i++){
            NovelInformation novelInformation = new NovelInformation();
            Element tr = trs.get(i);
            novelInformation.setNovel_address("https://www.amxsw.com"+tr.select("a").get(0).attr("href"));
            novelInformation.setNovel_name(tr.child(0).text());
            novelInformation.setAuthor_name(tr.child(2).text());
            novelInformation.setType(tr.child(3).text());
            novelInformationList.add(novelInformation);
        }
        return novelInformationList;
    }
    //该网站小说信息在搜索界面可直接解析，无需再重写进入小说页面解析信息方法
    @Override
    public NovelInformation getNovelInformationByNovelAddress(String novel_address) throws IOException {
        return null;
    }

    @Override
    public String getSearchAddress(String novel_name) {
        return "https://www.amxsw.com/search/?searchkey="+novel_name;
    }

    @Override
    public boolean domain(String NovelName) {
        try {
            System.out.println("艾米网站正在查找......");
            String searchAddress = getSearchAddress(NovelName);
            NovelInformation info = getNovelAddress(searchAddress);
            if (info != null) {
                NovelToFile file = new NovelToFile(info);
                if (file.fileExist()) {
                    System.out.println("小说已存在,是否覆盖：y/n");
                    Scanner sc = new Scanner(System.in);
                    String next = sc.next();
                    if ("n".equals(next)) return true;
                }
                NovelForNovelText getAllSection = new NovelForNovelText();
                getAllSection.setMAX_THREAD(2);
                System.out.println(info.getNovel_address());
                Class<AThreadGetTextList> threadGetTextListClass = (Class<AThreadGetTextList>) Class.forName("NovelFromHtml.GetAiMiNovel.utils.ThreadGetTextList");
                List<String> novelLists = getAllSection.getNovel(info.getNovel_address(), threadGetTextListClass);
                file.writeDownNovel(novelLists);
                return true;
            }
        } catch (IOException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
