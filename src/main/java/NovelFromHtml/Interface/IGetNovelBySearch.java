package NovelFromHtml.Interface;

import Bean.NovelInformation;

import java.io.IOException;
import java.util.List;

/**
 * 此类主要是为搜索后提供小说信息服务list<novel>
 */
public interface IGetNovelBySearch {
    /**
     * 通过查找关键词的响应地址获取到书籍地址
     * @param SearchAddress 关键词查找地址
     * @return 书籍地址
     * @throws IOException 转类型异常
     */
    NovelInformation getNovelAddress(String SearchAddress) throws IOException;


    /**
     * 将书名转化为url地址格式
     * @param novelName 书名
     * @return url地址格式名称
     */
    String parseHtmlEncoding(String novelName);

    /**
     * 从搜索列表中解析出所有相关文章地址
     * @param address 搜索响应地址
     * @return 文章信息列表
     */
    List<NovelInformation> parseNovelAddress(String address) throws IOException;

    /**
     * 通过小说地址获取小说全部信息
     * @param novel_address 小说地址
     * @return 小说信息
     * @throws IOException 转地址错误
     */
    NovelInformation getNovelInformationByNovelAddress(String novel_address) throws IOException;


    /**
     * 获取搜索地址
     * @param novel_name 搜索信息
     * @return 搜索地址
     */
    String getSearchAddress(String novel_name);

    /**
     * 主入口
     * @return 查询成功则True否则false
     * @param NovelName 小说名称
     */
    boolean domain(String NovelName);

}
