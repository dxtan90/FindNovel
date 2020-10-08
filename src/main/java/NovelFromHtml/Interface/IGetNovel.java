package NovelFromHtml.Interface;

import java.io.IOException;
import java.util.List;

public interface IGetNovel {

    /**
     * 反射机制的获取小说列表
     * @param novel_address 小说地址
     * @param threadGetTextListClass 获取单章小说内容方法的类
     * @return 小说所有章节内容
     */
    List<String> getNovel(String novel_address, Class<AThreadGetTextList> threadGetTextListClass) throws IOException, IllegalAccessException;
}
