package NovelFromHtml.GetBiQuGeNovel_1;

import Bean.NovelInformation;
import NovelFromHtml.GetBiQuGeNovel_1.utils.NovelForNovelText;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class allTypeRank {

    public static void main(String[] args) throws IOException {
        allTypeRank main = new allTypeRank();
        main.doMain("https://www.52bqg.com/paihangbang/");
    }

    /**
     * 获取Rank地址的全部类型的全部排行小说信息
     * @param rankAddress rank地址
     * @return 所有类型的map
     * @throws IOException 转地址异常
     */
    public Map<String,List<NovelInformation>> getTypeRank(String rankAddress) throws IOException {
        Connection con = Jsoup.connect(rankAddress);
        Document doc = con.get();
        //创建对应类型rankMap
        Map<String,List<NovelInformation>> map = new HashMap<>();
        //获取所有类型
        Elements tables = doc.select("table[class=tbo]");
        for(Element table:tables){
            //获取当前类型Rank
            Elements lis = table.select("ul[class=fen-top]").get(0).children();
            //创建Rank列表
            List<NovelInformation> list = new ArrayList<>();
            //获取当前类型名称
            String type = table.select("span[class=btitle]").get(0).text();
            for(Element li:lis){
                //获取文章信息
                String novel_address = li.child(0).attr("href");
                String novel_name = li.child(0).text();
                //注入信息
                NovelInformation novel = new NovelInformation();
                novel.setNovel_name(novel_name);
                novel.setNovel_address(novel_address);
                novel.setType(type);
                //加入列表
                list.add(novel);
            }
            //将当前列表放入Map
            map.put(type,list);
        }
        return map;
    }

    /**
     * 主执行方法
     * @param rankAddress
     */
    public void doMain(String rankAddress){
        try {
            //获取所有小说
            Map<String, List<NovelInformation>>  typeRank = getTypeRank(rankAddress);
            Set<Map.Entry<String, List<NovelInformation>>> set = typeRank.entrySet();

            //遍历所有小说
            for(Map.Entry<String, List<NovelInformation>> e:set){
                //遍历某一类型小说列表
                List<NovelInformation> novels = e.getValue();
                for(NovelInformation novel:novels){
                    //当前小说线程开启
                    new Thread(new NovelForNovelText(novel),novel.getNovel_name()).start();
                    System.out.println("开始爬取："+novel.getNovel_name());
                    while(true){
                        Map<Thread, StackTraceElement[]> Threads = Thread.getAllStackTraces();
                        if(Threads.size()<=20){
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
