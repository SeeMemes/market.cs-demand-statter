package cs.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Objects;

public class SkinInfoEntity {
    private static final Logger log = LoggerFactory.getLogger(SkinInfoEntity.class);
    private final String name;
    private final LocalDateTime time;
    private Double sellPrice;
    private Currency cur;

    public SkinInfoEntity(String name, String priceString, String currency) {
        this.name = name;
        this.time = LocalDateTime.now();
        this.sellPrice = Double.valueOf(priceString);
        try {
            this.cur = Currency.valueOf(currency);
        } catch (IllegalArgumentException e) {
            log.info("Problem with: " + currency);
            this.cur = Currency.USD;
        }
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public double getPrice() {
        return sellPrice/cur.getAmount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkinInfoEntity that = (SkinInfoEntity) o;
        return name.equals(that.name) && time.equals(that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, time);
    }

    @Override
    public String toString() {
        return "SkinInfoEntity{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", sellPrice=" + getPrice() + cur +
                '}';
    }
}
