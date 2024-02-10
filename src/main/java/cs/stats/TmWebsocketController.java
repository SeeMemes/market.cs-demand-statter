package cs.stats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@Component
@ClientEndpoint
public class TmWebsocketController {
    private Session session;
    private final ObjectMapper objectMapper;
    private final SellStatsRepository sellStatsRepository;

    public TmWebsocketController(
            @Autowired SellStatsRepository sellStatsRepository
    ) throws DeploymentException, IOException {
        this.objectMapper = new ObjectMapper();
        this.sellStatsRepository = sellStatsRepository;
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = URI.create("wss://wsn.dota2.net/wsn/");

        container.connectToServer(this, uri);
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("WebSocket connected.");
        sendRequest();
    }

    @OnMessage
    public void onMessage(String message) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(message);
        String itemDataString =
                StringEscapeUtils.unescapeJson(
                        StringEscapeUtils.unescapeJson(
                                rootNode.get("data").toString()
                        ))
                        .replaceAll("\"", "");
        String[] data = itemDataString
                .substring(1, itemDataString.length() - 1)
                .split(",");

        SkinInfoEntity infoEntity = new SkinInfoEntity(data[5], data[4], data[7]);
        sellStatsRepository.addToBoughtItems(infoEntity);
    }

    @OnClose
    public void onClose() throws IOException {
        System.out.println("WebSocket closed.");
        session.close();
    }

    private void sendRequest() {
        String request = "history_go";
        session.getAsyncRemote().sendText(request);
    }
}
