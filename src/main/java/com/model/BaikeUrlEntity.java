package com.model;

import java.io.Serializable;

public class BaikeUrlEntity implements Serializable{

    private String title;
    private Long lemmaId;
    private String url;
    private String pic;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getLemmaId() {
        return lemmaId;
    }

    public void setLemmaId(Long lemmaId) {
        this.lemmaId = lemmaId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "BaikeUrlEntity{" +
                "title='" + title + '\'' +
                ", lemmaId=" + lemmaId +
                ", url='" + url + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
