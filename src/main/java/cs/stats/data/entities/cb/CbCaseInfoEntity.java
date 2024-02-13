package cs.stats.data.entities.cb;

import java.util.HashMap;
import java.util.Map;

public class CbCaseInfoEntity {
    private final String caseName;
    private final Map<String, CbDropInfoEntity> dropList;
    private int openedCount;

    public CbCaseInfoEntity(String caseName) {
        this.caseName = caseName;
        this.dropList = new HashMap<>();
        this.openedCount = 1;
    }

    public String getCaseName() {
        return caseName;
    }

    public Map<String, CbDropInfoEntity> getDropList() {
        return dropList;
    }

    public CbDropInfoEntity getDropEntity(String dropName) {
        return dropList.get(dropName);
    }

    public void putItemToDropList(String itemName, CbDropInfoEntity item) {
        dropList.put(itemName, item);
    }

    public void incrementOpenedCount() {
        this.openedCount++;
    }


}
