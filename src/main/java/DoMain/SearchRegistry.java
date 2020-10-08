package DoMain;

import NovelFromHtml.GetAiMiNovel.AiMiSearch;
import NovelFromHtml.GetBiQuGeNovel.BiQuGeSearch;
import NovelFromHtml.GetDingDianNovel.DingDianSearch;
import NovelFromHtml.Interface.IGetNovelBySearch;

import java.util.ArrayList;
import java.util.List;

public class SearchRegistry {
    private static List<IGetNovelBySearch> items = new ArrayList<>();

    private static void init(){
        addRegistry(new BiQuGeSearch());
        addRegistry(new DingDianSearch());
        addRegistry(new AiMiSearch());
    }

    public static List<IGetNovelBySearch> getRegistry(){
        init();
        return items;
    }


    public static void addRegistry(IGetNovelBySearch search){
        items.add(search);
    }
    public static void removeRegistry(IGetNovelBySearch search){
        items.remove(search);
    }
}
