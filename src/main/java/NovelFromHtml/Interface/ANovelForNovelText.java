package NovelFromHtml.Interface;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public abstract class ANovelForNovelText implements IGetNovel{
    /**
     * 最大线程数
     */
    private int MAX_THREAD = 5;


    /**
     * 获取所有章节地址
     * @param novel_address 文章地址
     * @return 所有章节地址
     * @throws IOException 转地址异常
     */
    public abstract List<String> getAllSectionAddress(String novel_address) throws IOException;


    /**
     * 获得文章所有章节文本
     * @param novel_address 文章地址
     * @return 章节文本列表
     * @throws IOException
     */
    @Override
    public List<String> getNovel(String novel_address, Class<AThreadGetTextList> threadGetTextListClass) throws IOException, IllegalAccessException{
        //获取文章获取线程构造函数
        Constructor<AThreadGetTextList> constructor = null;
        try {
            constructor = threadGetTextListClass.getConstructor(int.class,int.class,int.class, List.class);
        } catch (NoSuchMethodException e) {
            System.out.println("获取文章获取线程构造函数失败");
        }

        //返回的list
        List<String> novelList = new LinkedList<>();

        //每个线程分配的章节数
        int ThreadSection = 0;

        //总地址列表
        List<String> allSectionAddress = getAllSectionAddress(novel_address);

        //取得更适配的线程数
        ThreadSection = allSectionAddress.size()/MAX_THREAD+1;
        //分线程总数
        int thread_number = (int)(Math.ceil(allSectionAddress.size()/(double)ThreadSection));

        //声明线程数
        Thread[] threads = new Thread[thread_number];
        //声明对象数
        AThreadGetTextList[] classList =  new AThreadGetTextList[thread_number];

        //分发线程
        for(int nowThread=0;nowThread<thread_number;nowThread++){
            //创建文章获取线程对象实例
            try {
                assert constructor != null;
                classList[nowThread] = constructor.newInstance(nowThread, thread_number, ThreadSection, allSectionAddress);
            } catch (InstantiationException e) {
                System.out.println("构造线程实例失败");
            } catch (InvocationTargetException e) {
                System.out.println("被调用方法异常");
                e.printStackTrace();
            }
            //创建线程实例
            threads[nowThread] = new Thread(classList[nowThread],nowThread+"");
            //线程开启
            threads[nowThread].start();
        }
        //爬取信息监测
        boolean toEnd = false;
        while(!toEnd){
            toEnd = true;
            for(Thread thread:threads){
                if(thread.isAlive()){
                    toEnd = false;
                }
            }
            System.out.println("\n\n\n\n\n\n\n\n\n");
            for(AThreadGetTextList nowClass:classList){
                nowClass.printNowPresent();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //链接总信息
        for(AThreadGetTextList nowClass:classList){
            novelList.addAll(nowClass.getThreadTextList());
        }

        return novelList;
    }


    public void setMAX_THREAD(int MAX_THREAD) {
        this.MAX_THREAD = MAX_THREAD;
    }
}
