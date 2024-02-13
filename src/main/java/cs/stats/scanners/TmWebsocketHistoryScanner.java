package cs.stats.scanners;

import cs.stats.data.SellsDataController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

@Component
@ClientEndpoint
public class TmWebsocketHistoryScanner {
    private static final Logger log = LoggerFactory.getLogger(TmWebsocketHistoryScanner.class);
    private Session session;
    private final SellsDataController sellStatsRepository;

    public TmWebsocketHistoryScanner(
            @Autowired SellsDataController sellStatsRepository
    ) {
        this.sellStatsRepository = sellStatsRepository;
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            URI uri = URI.create("wss://wsn.dota2.net/wsn/");

            container.connectToServer(this, uri);
        } catch (Exception ignored) {
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("WebSocket connected.");
        sendRequest();
    }

    @OnMessage
    public void onMessage(String message) {
        CompletableFuture.runAsync(() -> sellStatsRepository.addSkinToBoughtItems(message));
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
