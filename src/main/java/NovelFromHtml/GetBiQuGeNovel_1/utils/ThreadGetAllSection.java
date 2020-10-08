package NovelFromHtml.GetBiQuGeNovel_1.utils;

import Bean.NovelInformation;
import NovelFromHtml.Interface.AThreadGetTextList;
import NovelFromHtml.Interface.IGetNovel;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class ThreadGetAllSection extends NovelForNovelText implements IGetNovel {
    private static final Integer MAX_THREAD=20;
    private int ThreadSection = 300;

    public ThreadGetAllSection(NovelInformation novel) {
        super(novel);
    }

    @Override
    public List<String> getNovel(String novel_address) throws IOException {
        //返回的list
        List<String> novelList = new LinkedList<>();
        //总地址列表
        List<String> allSectionAddress = getAllSectionAddress(novel_address);
        //取得更适配的线程数
        ThreadSection = allSectionAddress.size()/MAX_THREAD+1;
        //分线程总数
        int thread_number = (int)(Math.ceil(allSectionAddress.size()/(double)ThreadSection));
        //创建线程数
        Thread[] threads = new Thread[thread_number];
        //创建对象数
        ThreadGetTextList[] classList = new ThreadGetTextList[thread_number];
        //分发线程
        for(int nowThread=0;nowThread<thread_number;nowThread++){
            classList[nowThread] = new ThreadGetTextList(nowThread, thread_number, ThreadSection, allSectionAddress);
            threads[nowThread] = new Thread(classList[nowThread],nowThread+"");
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
            for(ThreadGetTextList nowClass:classList){
                nowClass.printNowPresent();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //链接总信息
        for(ThreadGetTextList nowClass:classList){
            novelList.addAll(nowClass.getThreadTextList());
        }

        return novelList;
    }

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

}
