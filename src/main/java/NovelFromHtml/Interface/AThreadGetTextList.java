package NovelFromHtml.Interface;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public abstract class AThreadGetTextList implements Runnable {

    //当前线程编号
    protected int nowThread;
    //每线程的章节数量
    protected int ThreadSection;
    //所有章节的列表
    protected List<String> allSectionAddress;
    //当前线程进度
    protected String nowPresent;
    //目标章节
    protected int orderSection;
    //起始章节
    protected int originSection;
    //当前线程的文章列表
    protected  List<String> ThreadTextList;
    //构造器
    public AThreadGetTextList(int nowThread, int thread_number, int threadSection, List<String> allSectionAddress) {
        this.nowThread = nowThread;
        ThreadSection = threadSection;
        this.allSectionAddress = allSectionAddress;

        originSection  = nowThread*ThreadSection;
        orderSection = nowThread==thread_number-1?allSectionAddress.size():(nowThread+1)*ThreadSection;
        nowPresent = "0%";
    }

    /**
     * 执行当前线程的章节文本获取任务
     * @return 当前线程的任务文本列表
     */
    public List<String> getTextListByNowThread() throws IOException{
        //当前线程所存放的文章内容
        List<String> ThreadTextList = new LinkedList<>();
        //当前线程任务所到地址
        for(int nowSec = originSection;nowSec<orderSection;nowSec++){
            String section_address = allSectionAddress.get(nowSec);
            String sectionText = getSectionText(section_address);
            ThreadTextList.add(sectionText);
            if(nowSec%5==0&&nowSec!=0){
                double persent = (nowSec-originSection)/(double)(orderSection-originSection);
                DecimalFormat df = new DecimalFormat("00%");
                nowPresent = df.format(persent);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        nowPresent = "100%";
        return ThreadTextList;
    }

    /**
     * 需要实现的从章节地址中获取章节内容String
     * @param section_address 章节地址
     * @return 章节内容
     * @throws IOException 转嘀址异常
     */
    public abstract String getSectionText(String section_address) throws IOException ;

    /**
     * 打印当前进度
     */
    public void printNowPresent(){
        System.out.print("线程"+nowThread+":\t负责  ");
        System.out.printf("%-5d-%-5d",originSection,orderSection);
        System.out.print("  章节\t已完成:\t"+nowPresent+"\n");
    }

    @Override
    public void run() {
        try {
            ThreadTextList = getTextListByNowThread();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<String> getThreadTextList() {
        return ThreadTextList;
    }

}
