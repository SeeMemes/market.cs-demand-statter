package cs.stats.data.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class QueuedSkinEntity {
    private static final Logger log = LoggerFactory.getLogger(QueuedSkinEntity.class);
    private final String name;
    private final LocalDateTime time;

    public QueuedSkinEntity(String name) {
        this.name = name;
        this.time = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
