package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class testQidian {
    private String bookName;

    public static void main(String[] args) throws IOException {
        testQidian qd = new testQidian();
        List<String> addressList = qd.getFreeLink("https://www.qidian.com/free");
        qd.writeList(addressList);
    }
    //д������
    public void writeList(List<String> addressList) throws IOException {
        for(String address:addressList){
            List<String> allUrl = getAllUrl(address);
            for (String s : allUrl) {
                List<String> text = getText(s);
                writeToFile(text);
                System.out.println("--------------��¼��");
            }
            System.out.println("------------------------------------------");
            System.out.println("------------------------------------------");
        }
    }


    //��ȡ���ҳ��������鼮����
    public List<String> getFreeLink(String address) throws IOException {
        URL url = new URL(address);
        List<String> addressList = new ArrayList<>();
        Document doc = Jsoup.parse(url, 10000);
        Elements bookImageText = doc.getElementsByClass("book-img-text");
        Elements lis = bookImageText.get(0).child(0).getElementsByTag("li");
        for(Element li:lis){
            Elements a = li.getElementsByTag("a");
            String href = a.get(0).attr("href");
            href = "https:" + href +"#Catalog";
            addressList.add(href);
        }
        return addressList;
    }
    //д���ļ�
    public void writeToFile(List<String> text) throws IOException {
        String fileName = bookName + ".txt";
        File f = new File("E:\\测试文件夹\\测试爬起点小说\\", fileName);
        if( f.lastModified()<new Date().getTime()-60000){
            f.delete();
        }
        if (!f.exists()) {
            f.createNewFile();
        }
        FileWriter fw = new FileWriter(f, true);
        BufferedWriter bw = new BufferedWriter(fw);
        for (int i = 0; i < text.size(); i++) {
            if (i == 0) {
                bw.write(text.get(0));
                bw.newLine();
            } else {
                bw.write("  " + text.get(i));
                bw.newLine();
            }
        }
        bw.close();
        fw.close();
    }

    //��ȡÿҳ����
    public List<String> getText(String address) throws IOException {
        List<String> text = new ArrayList<>();
        URL url = new URL(address);
        Document doc = Jsoup.parse(url, 10000);

        Elements eles = doc.getElementsByClass("content-wrap");
        text.add(eles.get(0).text());
        System.out.println(eles.get(0).text());
        Elements ps = doc.getElementsByClass("read-content j_readContent");
        Elements p = ps.get(0).getElementsByTag("p");
        for (Element e : p) {
            String s = e.text();
            text.add(s);
        }
        return text;
    }

    //��ȡ��������ַ
    public List<String> getAllUrl(String addUrl) throws IOException {
        List<String> allAddress = new ArrayList<>();
        URL url = new URL(addUrl);
        Document doc = Jsoup.parse(url, 10000);
        //��ȡ���±���
        Elements booktitle = doc.getElementsByClass("book-info");
        String filename = booktitle.get(0).getElementsByTag("em").get(0).text();
        bookName = filename;
        //��ȡÿ�½�����
        Elements c = doc.select("ul[class=cf]");
        for (int i = 0; i < c.size(); i++) {
            Element e = c.get(i);
            Elements lis = e.getElementsByTag("li");
            for (Element li : lis) {
                String href = li.getElementsByTag("a").get(0).attr("href");
                href = "https:" + href;
                allAddress.add(href);
            }
            System.out.println(allAddress);
        }
        return allAddress;
    }

    //�ַ����ֶ�
    public List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    public List<String> getStrList(String inputString, int length, int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length,
                    (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    public String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }
}
