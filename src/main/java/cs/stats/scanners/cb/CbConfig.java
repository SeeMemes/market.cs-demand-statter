package cs.stats.scanners.cb;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CbConfig {
    private static final Logger log = LoggerFactory.getLogger(CbConfig.class);

    @Bean
    public WebDriver webDriver(
            @Value("${cb.drivername}") String driverName
    ) {
        try {
            System.setProperty("webdriver.chrome.driver", driverName);
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.setExperimentalOption("useAutomationExtension", false);
            options.addArguments("--remote-allow-origins=*");
            options.addArguments(
                    "--disable-blink-features=AutomationControlled",
                    "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36 Edg/113.0.1774.57"
            );
            return new ChromeDriver(options);
        } catch (IllegalStateException e) {
            log.error("" + e.getMessage() + " " + e.getCause());
            return null;
        }
    }
}
