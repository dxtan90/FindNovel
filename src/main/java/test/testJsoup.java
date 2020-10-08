package test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class testJsoup {

    public static void main(String[] args) throws IOException {

        URL url = new URL("");
        Document document = Jsoup.parse(url, 10000);
        String html = document.toString();
        String regex = "\"cid=\\d+&";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(html);
        String cid = "";

        if (m.find()) cid = m.group().replaceAll("[^\\d]", "");


        System.out.println(cid);

        String cidXml = "http://comment.bilibili.com/" + cid + ".xml";
        URL url2 = new URL(cidXml);
        Document xml = Jsoup.parse(url2, 10000);

        Elements elements = xml.getElementsByTag("d");
        int number = 0;
        for (Element element : elements) {
            number++;
            String s = element.text();
            if (number % 10 == 0) {
                System.out.print(number + "\t");
            }
            System.out.println(s);

        }

    }
}
