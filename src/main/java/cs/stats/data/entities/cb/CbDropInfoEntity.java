package cs.stats.data.entities.cb;

public class CbDropInfoEntity {
    private final String itemName;
    private int dropCount;

    public CbDropInfoEntity(String itemName) {
        this.itemName = itemName;
        this.dropCount = 1;
    }


    public String getItemName() {
        return itemName;
    }

    public int getDropCount() {
        return dropCount;
    }

    public void incrementDropCount() {
        this.dropCount++;
    }
}
