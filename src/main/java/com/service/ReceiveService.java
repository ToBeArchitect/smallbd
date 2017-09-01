package com.service;

import com.model.PageInfoEntity;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReceiveService {
    private static final Logger log = Logger.getLogger(ReceiveService.class);

    public void receiveMsg(String url) {
        try {
            PageInfoEntity pageInfoEntity = new PageInfoEntity();

            Document doc = Jsoup.connect(url).get();
            //获取名称
            String name = getTitle(url);
            /*获取图片*/
            String imageUrl = getPicture(doc);
            /*获取基础信息*/
            String information = getInformation(doc);
            /*获取描述信息*/
            String description = getDescription(doc);


            pageInfoEntity.setName(name);
            pageInfoEntity.setImageUrl(imageUrl);
            pageInfoEntity.setDescription(description);
            pageInfoEntity.setInformation(information);

            if ("".equals(imageUrl) || "".equals(name) || "".equals(description) || "".equals(information)) {
                log.warn("[Something is empty] [" + url + "]" + pageInfoEntity);
            }
            log.info(pageInfoEntity);
        } catch (Exception e) {
            log.error(e.getMessage()+e.getCause());
        }
    }
    /*获取基础信息*/
    private String getInformation(Document doc) {
        Elements names = doc.select("div.main-content div.basic-info .basicInfo-item").select(".name");
        Elements values = doc.select("div.main-content div.basic-info .basicInfo-item").select(".value");
        String information = "";
        for (int i = 0; i < names.size(); i++) {
            information += names.get(i).text() + ":" + values.get(i).text() + ",";
        }
        if (!information.equals("")){
            information = information.substring(0, information.length() - 1);
        }
        information.replaceAll(" ","");
        return information;
    }

    /*获取标题，从url中获取*/
    private String getTitle(String url) {
        //http://baike.baidu.com/item/%E5%B0%BC%E6%A0%BC%E4%B9%B0%E6%8F%90%C2%B7%E7%83%AD%E5%90%88%E6%9B%BC/10276274
        String[] split = url.split("/");
        String name = split[split.length - 2];
        try {
            name = URLDecoder.decode(name, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getCause());
        }
        return name;
    }

    /*获取描述信息*/
    private String getDescription(Document doc) {
        List<String> conditions = new ArrayList<String>();
        conditions.add("body > div.body-wrapper > div.content-wrapper > div > div.main-content > div.lemma-summary");
        conditions.add("#posterCon > dd.desc > div");
        conditions.add("div.body-wrapper > div.content-wrapper > div > div.main-content > div.main_tab.main_tab-defaultTab.curTab > div.lemma-summary");
        //非精确查询条件
        conditions.add("div.poster-top > div.lemma-summary");
        Elements descElements;
        String desc = "";
        for (int i = 0; (i < conditions.size()) && (desc.equals("")); i++) {
            descElements = doc.select(conditions.get(i));
            desc = descElements.text();
        }
        return desc;
    }

    /*获取图片*/
    private String getPicture(Document doc) {
        List<String> conditions = new ArrayList<String>();
        //图片筛选条件
        conditions.add("body > .body-wrapper > .feature_poster > div > div > a > img");
        conditions.add(".body-wrapper > .content-wrapper > div > .side-content > .summary-pic > a > img");
        conditions.add(".body-wrapper > .after-feature-poster > div > dl.lemmaWgt-albumList-poster > dd > a > img");
        conditions.add("#slider_works > ul > li:nth-child(1) > a > img");
        conditions.add("div.body-wrapper> div.content-wrapper > div > div.main-content > div > div.lemmaWgt-moviePictures > div > ul > li > a > img");
        conditions.add("body > div.body-wrapper > div.feature_poster > div > div.poster-right > div > a > img");
        conditions.add(".image-link > img");
        conditions.add("a.lemma-album> div.album-wrap > img");
        Elements imageElements;
        String imgUrl = "";
        for (int i = 0; (i < conditions.size()) && (imgUrl.equals("")); i++) {
            imageElements = doc.select(conditions.get(i));
            imgUrl = imageElements.attr("src");
            if (!imgUrl.startsWith("http:")) {
                continue;
            }
        }
        return imgUrl;
    }


}
