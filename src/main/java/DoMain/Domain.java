package DoMain;

import NovelFromHtml.Interface.IGetNovelBySearch;
import java.util.Scanner;

public class Domain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("exit退出");
        System.out.println("请输入书籍名称:");
        while(sc.hasNextLine()){
            String novel_name = sc.nextLine();
            if("exit".equals(novel_name))break;
            if("".equals(novel_name.trim()))continue;

            for(IGetNovelBySearch search:SearchRegistry.getRegistry()){
                if(search.domain(novel_name))break;
            }
            System.out.println("请输入书籍名称:");
        }
        sc.close();
    }

}
