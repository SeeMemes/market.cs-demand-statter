package cs.stats.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs.stats.data.entities.tm.TmSkinInfoEntity;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class SellsDataController {
    private static final Logger log = LoggerFactory.getLogger(SellsDataController.class);
    private final BlockingQueue<String> caseBattleOpenedItems = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> boughtItems = new LinkedBlockingQueue<>();
    private final Map<String, TmSkinInfoEntity> infoMap = new HashMap<>();
    private final ObjectMapper objectMapper;


    public SellsDataController() {
        this.objectMapper = new ObjectMapper();
    }

    public void addSkinToBoughtItems (String messageFromMarketCS) {
        try {
            JsonNode rootNode = objectMapper.readTree(messageFromMarketCS);
            String itemDataString =
                    StringEscapeUtils.unescapeJson(
                                    StringEscapeUtils.unescapeJson(
                                            rootNode.get("data").toString()
                                    ))
                            .replaceAll("\"", "");
            String[] data = itemDataString
                    .substring(1, itemDataString.length() - 1)
                    .split(",");

            TmSkinInfoEntity infoEntity = infoMap.get(data[5]);
            if (Objects.isNull(infoEntity)) {
                infoEntity = new TmSkinInfoEntity(data[2], data[5], data[4], data[7]);
                infoMap.put(data[5], infoEntity);
            }
            else {
                infoEntity.saveToSellHistory(data[4], data[7]);
            }
            System.out.println(infoEntity);
        } catch (StringIndexOutOfBoundsException | JsonProcessingException e1) {
            log.error("-- Cannot work with message: \"" + messageFromMarketCS + "\"");
        }
    }
}
