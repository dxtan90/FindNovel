package NovelFromHtml.GetBiQuGeNovel;

import Bean.NovelInformation;
import NovelFromHtml.GetBiQuGeNovel.utils.NovelForNovelText;
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

public class BiQuGeSearch implements IGetNovelBySearch {

    public static void main(String[] args){
        BiQuGeSearch main = new BiQuGeSearch();
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入书籍名称:");
        while(sc.hasNextLine()){
            String novel_name = sc.nextLine();
            if("exit".equals(novel_name))break;
            if("".equals(novel_name.trim()))continue;
            System.out.println("正在查找......");
            main.domain(novel_name);
            System.out.println("请输入书籍名称:");
        }
        sc.close();

    }


    /**
     * 通过查找关键词的响应地址获取到书籍地址
     * @param SearchAddress 关键词查找地址
     * @return 书籍地址
     * @throws IOException 转类型异常
     */
    public NovelInformation getNovelAddress(String SearchAddress) throws IOException{
        Connection con = Jsoup.connect(SearchAddress).userAgent("Mozilla/5.0 (Windows NT 10.0; …) Gecko/20100101 Firefox/74.0");
        Connection.Response response = con.execute();
        URL url = response.url();
        System.out.println(url.toString());
        if(url.toString().matches("https://www\5652bqg\56net/book.*")){
            System.out.println("------------笔趣阁网站已查到该书籍-----------");
            return getNovelInformationByNovelAddress(url.toString());
        }else{
            List<NovelInformation> novelInformations = parseNovelAddress(url.toString());
            if(novelInformations.size()==0){
                System.out.println("------------笔趣阁网站未查到该书籍-----------");
                return null;
            }
            System.out.println("----------笔趣阁网站查找到以下结果-----------");
            System.out.println("------------0  : 退出-------------");
            System.out.printf("%-50s\t\t%-30s\t\t%-30s\n","书名","作者","类型");
            int now = 1;
            for(NovelInformation novel:novelInformations){
               System.out.printf("%-20d%-30s\t%-30s%-30s\n",now++,novel.getNovel_name(),novel.getAuthor_name(),novel.getType());
            }

            System.out.println("请输入书籍编号");
            Scanner sc = new Scanner(System.in);
            int novelNo = sc.nextInt();

            if(novelNo<0||novelNo>novelInformations.size()||novelNo==0)return null;
            return novelInformations.get(novelNo-1);
        }
    }

    /**
     * 将书名转化为url地址格式
     * @param novelName 书名
     * @return url地址格式名称
     */
    public String parseHtmlEncoding(String novelName){
        if(novelName==null)return null;
        try {
            return URLEncoder.encode(new String(novelName.getBytes()), "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从搜索列表中解析出所有相关文章地址
     * @param address 搜索响应地址
     * @return 文章信息列表
     */
    public List<NovelInformation> parseNovelAddress(String address) throws IOException {
        Document doc = Jsoup.parse(new URL(address),10000);

        Elements lis = doc.select("div[class=novelslistss]").get(0).select("li");
        List<NovelInformation> list = new ArrayList<>();

        for(Element li:lis){
            String novel_type = li.child(0).text();
            String novel_name = li.child(1).child(0).text();
            String novel_address = li.child(1).child(0).attr("href");
            String author_name = li.child(3).text();
            
            NovelInformation novel = new NovelInformation();
            novel.setType(novel_type);
            novel.setAuthor_name(author_name);
            novel.setNovel_address(novel_address);
            novel.setNovel_name(novel_name);

            list.add(novel);
        }
        
        return list;
    }


    /**
     * 通过小说地址获取小说全部信息
     * @param novel_address 小说地址
     * @return 小说信息
     * @throws IOException 转地址
     */
    public NovelInformation getNovelInformationByNovelAddress(String novel_address) throws IOException {
        Document doc = Jsoup.parse(new URL(novel_address), 10000);

        String text = doc.select("div[class=con_top]").get(0).text();
        text = text.replaceAll(" ", "");
        String[] splits = text.split(">");
        String novel_type=splits[1];
        String novel_name=splits[2];
        String author_name = doc.select("div[id=info]").get(0).child(1).child(0).text();

        NovelInformation nov = new NovelInformation();
        nov.setNovel_address(novel_address);
        nov.setNovel_name(novel_name);
        nov.setAuthor_name(author_name);
        nov.setType(novel_type);
        return nov;
    }

    /**
     * 获取搜索地址
     * @param novel_name 搜索信息
     * @return 搜索地址
     */
    public String getSearchAddress(String novel_name) {
        //修改编码
        String novelName = parseHtmlEncoding(novel_name);
        //获得查询地址
        return "https://www.52bqg.com/modules/article/search.php?searchkey="+novelName;
    }
    /**
     * 主执行类
     * @param NovelName 书名
     */
    public boolean domain(String NovelName){
        try {
            System.out.println("笔趣阁网站正在查找......");
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
                NovelForNovelText getAllSection = new NovelForNovelText();
                Class<AThreadGetTextList> threadGetTextListClass = (Class<AThreadGetTextList>) Class.forName("NovelFromHtml.GetBiQuGeNovel.utils.ThreadGetTextList");
                List<String> novelLists = getAllSection.getNovel(info.getNovel_address(),threadGetTextListClass);
                file.writeDownNovel(novelLists);
                return true;
            }
        } catch (IOException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
