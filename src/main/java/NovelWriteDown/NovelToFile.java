package NovelWriteDown;

import Bean.NovelInformation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class NovelToFile {
    private NovelInformation novel;

    public NovelToFile(NovelInformation novel) {
        this.novel = novel;
    }

    public NovelInformation getNovel() {
        return novel;
    }

    public void setNovel(NovelInformation novel) {
        this.novel = novel;
    }

    public void writeDownNovel(List<String> sections) throws IOException {
        File dir = new File("E:\\测试文件夹\\爬小说\\"+novel.getType());
        File file = new File(dir,novel.getNovel_name()+".txt");
        if(!dir.exists()){
            dir.mkdirs();
        }
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file, false);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(novel.getNovel_name()+":\t"+novel.getAuthor_name());
        bw.newLine();
        for(String section:sections){
            bw.write(section);
            bw.newLine();
        }
        System.out.println("\t\t\t"+novel.getNovel_name()+"写入完成");
        System.out.println("\t\t\t写入位置:"+file.getAbsolutePath());
        bw.close();
        fw.close();

    }

    /**
     * 判断小说是否存在
     * @return 是/否
     */
    public boolean fileExist(){
        File f = new File("E:\\测试文件夹\\爬小说\\"+novel.getType()+"\\"+novel.getNovel_name()+".txt");
        return f.exists();
    }
}
