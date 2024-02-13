package cs.stats.scanners.cb;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.concurrent.Future;

public class CbParser {
    private static final Logger log = LoggerFactory.getLogger(CbParser.class);

    @Bean
    @Async
    public Future<CbScanner> parseExecutor(
            @Autowired(required = false) WebDriver webDriver,
            @Value("${cb.address}") String caseBattleAddress
    ) {
        if (webDriver != null) {
            CbScanner cbScanner = new CbScanner(webDriver, caseBattleAddress);
            cbScanner.run();
            return new AsyncResult<>(cbScanner);
        }
        else {
            return new AsyncResult<>(null);
        }
    }
}
