package Bean;

import java.io.Serializable;

public class NovelInformation implements Serializable {
    private String novel_name;
    private String author_name;
    private String novel_address;
    private String type;

    @Override
    public String toString() {
        return "NovelInformation{" +
                "novel_name='" + novel_name + '\'' +
                ", author_name='" + author_name + '\'' +
                ", novel_address='" + novel_address + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNovel_name() {
        return novel_name;
    }

    public void setNovel_name(String novel_name) {
        this.novel_name = novel_name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getNovel_address() {
        return novel_address;
    }

    public void setNovel_address(String novel_address) {
        this.novel_address = novel_address;
    }
}
