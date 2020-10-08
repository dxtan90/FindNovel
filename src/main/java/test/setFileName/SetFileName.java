package test.setFileName;

import java.io.File;

public class SetFileName {
    public static void main(String[] args) {
        setFileName();
    }
    public static void setFileName(){
        File file = new File("E:\\测试文件夹\\files");
        File[] files = file.listFiles();
        for(File f:files)System.out.println(f.getName());
    }
}
