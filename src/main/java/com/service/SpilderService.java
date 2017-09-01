package com.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.BaikeUrlEntity;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.List;

@Service
public class SpilderService {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger log = Logger.getLogger(SpilderService.class);
    @Autowired
    private BloomFilterService bloomFilterService;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * @param index 爬虫开始的ID
     **/
    public void getUrl(Integer index) throws InterruptedException, IOException {
        String url = "http://baike.baidu.com/wikiui/api/zhixinmap?lemmaId=";

        Connection connection = Jsoup.connect(url + index).header("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        String body = connection.ignoreContentType(true).execute().body();
        if (body.equals("[]") || body.equals("false"))
            return;

        JsonNode jsonNode = MAPPER.readTree(body);

        int size = jsonNode.size();

        for (int j = 0; j < size; j++) {
            JsonNode listJson = jsonNode.get(j).get("data");
            Object temp = MAPPER.readValue(listJson.traverse(), MAPPER.getTypeFactory().constructCollectionType(List.class, BaikeUrlEntity.class));
            List<BaikeUrlEntity> baikeUrlEntitys = (List<BaikeUrlEntity>) temp;
            for (BaikeUrlEntity tempEntity :
                    baikeUrlEntitys) {
                String info = "[节点ID:" + index + "]" + "[Index:" + j + "]" + "[" + tempEntity.getTitle() + "]" + tempEntity.getUrl();
                if (bloomFilterService.add(tempEntity.getUrl())) {
                    //TODO:加入rabbmtMQ队列
                    rabbitTemplate.convertAndSend(tempEntity.getUrl());
                    
                    System.out.println(info);
                  
                } else {
                    log.debug("[fail]" + info);
                }
                jedisCluster.set("INFO", info);
            }
        }

    }

}
