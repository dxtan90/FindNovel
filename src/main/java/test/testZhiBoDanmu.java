package test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class testZhiBoDanmu {
    public static void main(String[] args) throws IOException, InterruptedException {
        Connection connect = Jsoup.connect("https://api.live.bilibili.com/xlive/web-room/v1/dM/gethistory").ignoreContentType(true);

        connect.data("roomid", "14843023");
        connect.data("csrf_token", "");
        connect.data("csrf", "");
        connect.data("visit_id", "");

        while (true) {
            Document doc = connect.post();

            JSONObject jsonObject = JSONObject.parseObject(doc.body().text());
            JSONObject data = JSONObject.parseObject(jsonObject.getString("data"));
            JSONArray rooms = data.getJSONArray("room");
            for (int i = 0; i < rooms.size(); i++) {
                JSONObject js = JSONObject.parseObject(rooms.get(i).toString());
                String text = js.getString("text");
                String name = js.getString("nickname");
                System.out.println(name + ":\t" + text);
            }
            System.out.println("----------------------");
            Thread.sleep(1000);
        }


    }
}
