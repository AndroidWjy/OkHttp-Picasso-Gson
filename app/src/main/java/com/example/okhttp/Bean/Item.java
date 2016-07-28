package com.example.okhttp.Bean;


/**
 * bean对象
 * Created by Administrator on 2016/7/27.
 */
public class Item {
    private String subject;
    private String summary;
    private String cover;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        return "Item{" +
                "subject='" + subject + '\'' +
                ", summary='" + summary + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }
}
