package NovelFromHtml.Interface;

import java.io.IOException;
import java.util.List;


/**
 * 此类是通脱小说信息novel获取到小说全文本List<String>服务
 */
public interface INovelForNovelText {
    /**
     * 获取所有章节地址
     * @param novel_address 文章地址
     * @return 所有章节地址
     * @throws IOException 转地址异常
     */
    List<String> getAllSectionAddress(String novel_address) throws IOException;


    /**
     * 获取某章节文本
     * @param section_address 章节地址
     * @return 某章节文本
     * @throws IOException 转地址异常
     */
    String getSectionText(String section_address) throws IOException;

    /**
     * 获得文章所有章节文本
     * @param novel_address 文章地址
     * @return 章节文本列表
     * @throws IOException 转地址异常
     */
    List<String> getNovel(String novel_address) throws IOException;

}
