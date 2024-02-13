package cs.stats.scanners.cb;

import cs.stats.data.entities.cb.CbCaseInfoEntity;
import cs.stats.data.entities.cb.CbDropInfoEntity;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CbScanner implements Runnable {
    private static Logger log = LoggerFactory.getLogger(CbScanner.class);
    private final WebDriver webDriver;
    private final String caseBattleAddress;
    private final Map<String, CbCaseInfoEntity> caseMap;

    public CbScanner(WebDriver webDriver, String caseBattleAddress) {
        this.webDriver = webDriver;
        this.caseBattleAddress = caseBattleAddress;
        this.caseMap = new HashMap<>();
    }

    @Override
    public void run() {
        webDriver.get(caseBattleAddress);

        // Feed parsing
        WebElement item;
        WebElement lastItem = null;
        WebElement dropCaseElement;
        WebElement dropNameElement;

        while (true) {
            try {
                try {
                    item = webDriver.findElement(By.className("item"));
                    if (!item.equals(lastItem)) {
                        dropCaseElement = item.findElement(By.className("drop-name"));
                        dropNameElement = item.findElement(By.className("user-item")).findElement(By.tagName("img"));

                        String caseName = dropCaseElement.getAttribute("innerHTML");
                        String lootName = dropNameElement.getAttribute("title");

                        lastItem = item;

                        setLoot(caseName, lootName);
                    }
                } catch (NoSuchElementException | NoSuchSessionException e0) {
                    log.info("Cb page has been lost");

                /*
                ### CLOUDFLARE BYPASS ###
                TO BYPASS CLOUDFLARE YOU NEED
                1) OPEN NEW TAB
                2) ENTER CB ADDRESS
                3) DO STEPS 1-2 UNTIL THE SITE LOADS
                AFTER IT LOADS THE APP WILL CLOSE ALL OTHER TABS AND WORK WITH IT
                 */
                    boolean caseBattleOpened = false;

                    while (!caseBattleOpened) {
                        Thread.sleep(5000);
                        try {
                            Set<String> windowHandles = webDriver.getWindowHandles();

                            for (String handle : windowHandles) {
                                webDriver.switchTo().window(handle);
                                if (webDriver.getTitle().contains("Case-Battle")) {
                                    caseBattleOpened = true;

                                    for (String otherHandle : windowHandles) {
                                        if (!otherHandle.equals(handle)) {
                                            webDriver.switchTo().window(otherHandle);
                                            webDriver.close();
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (NoSuchWindowException e2) {
                            break;
                        }
                    }

                    Set<String> windowHandles = webDriver.getWindowHandles();
                    // Switch to the tab
                    for (String handle : windowHandles) {
                        webDriver.switchTo().window(handle);
                        break;
                    }
                /*
                    ### END OF CLOUFLARE BYPASS ###
                */

                    log.info("Cb page has been found");
                }
                Thread.sleep(20);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void setLoot(String caseName, String lootName) {
        CbCaseInfoEntity caseEntity = caseMap.get(caseName);
        if (caseEntity != null) {
            caseEntity.incrementOpenedCount();
            CbDropInfoEntity dropEntity = caseEntity.getDropEntity(lootName);
            if (dropEntity != null) {
                dropEntity.incrementDropCount();
            } else {
                dropEntity = new CbDropInfoEntity(lootName);
                caseEntity.incrementOpenedCount();
                caseEntity.putItemToDropList(lootName, dropEntity);
            }
        } else {
            CbCaseInfoEntity cbCaseEntity = new CbCaseInfoEntity(caseName);
            CbDropInfoEntity lootEntity = new CbDropInfoEntity(lootName);
            cbCaseEntity.putItemToDropList(lootName, lootEntity);
            caseMap.put(caseName, cbCaseEntity);
        }
    }
}
