package cs.stats;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SellStatsRepository {
    private final Queue<String> caseBattleOpenedItems = new ArrayDeque<>();
    private final Queue<SkinInfoEntity> boughtItems = new ArrayDeque<>();


    public SellStatsRepository() {

    }

    public void addToBoughtItems (SkinInfoEntity infoEntity) {
        System.out.println(infoEntity);
        boughtItems.add(infoEntity);
    }
}
