package cs.stats.util.enums;

public enum Currency {
    USD(1000),
    EUR(1000),
    RUB(100);

    private final int amount;

    Currency(int amount) {
        this.amount = amount;
    }

    public int getAmount () {
        return this.amount;
    }
}
