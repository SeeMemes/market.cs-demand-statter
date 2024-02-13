package cs.stats.data.entities;

import cs.stats.util.enums.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class TmSkinInfoEntity {
    private static final Logger log = LoggerFactory.getLogger(TmSkinInfoEntity.class);
    private final String en_Name;
    private final String ru_Name;
    private final List<TmPriceInfoEntity> sellHistory;

    public TmSkinInfoEntity(String ru_Name, String en_Name, String sellPrice, String currency) {
        this.ru_Name = ru_Name;
        this.en_Name = en_Name;
        this.sellHistory = Arrays.asList(new TmPriceInfoEntity(sellPrice, currency));
    }

    public String getRu_Name() {
        return ru_Name;
    }

    public String getEn_Name() {
        return en_Name;
    }

    public List<TmPriceInfoEntity> getSellHistory() {
        return sellHistory;
    }

    public void saveToSellHistory (String sellPrice, String currency) {
        CompletableFuture.runAsync(() -> sellHistory.add(new TmPriceInfoEntity(sellPrice, currency)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TmSkinInfoEntity that = (TmSkinInfoEntity) o;
        return en_Name.equals(that.en_Name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(en_Name);
    }

    @Override
    public String toString() {
        return "TmSkinInfoEntity{" +
                "en_Name='" + en_Name + '\'' +
                ", ru_Name='" + ru_Name + '\'' +
                ", sellHistory=" + sellHistory +
                '}';
    }

    private static class TmPriceInfoEntity {
        private static final Logger log = LoggerFactory.getLogger(QueuedSkinEntity.class);
        private final LocalDateTime time;
        private final Double sellPrice;
        private Currency cur;

        public TmPriceInfoEntity(String sellPrice, String cur) {
            this.time = LocalDateTime.now();
            this.sellPrice = Double.valueOf(sellPrice);
            try {
                this.cur = Currency.valueOf(cur);
            } catch (IllegalArgumentException e) {
                log.info("-- Problem with: " + cur);
                this.cur = Currency.USD;
            }
        }

        private LocalDateTime getTime() {
            return time;
        }

        private double getPrice() {
            return sellPrice / cur.getAmount();
        }

        @Override
        public String toString() {
            return "TmPriceInfoEntity{" +
                    "time=" + time +
                    ", sellPrice=" + sellPrice +
                    ", cur=" + cur +
                    '}';
        }
    }
}
