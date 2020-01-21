package ua.epicentr;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.*;


public class Testing {
    private ChromeDriver driver;
    private List<WebElement> webElements;
    private List<String> linkCard;

    private int countItem = 3;                           // number check card
    private final int PAGE_TEST_TWO = 2;               // number of pages to check for test 2
    private final int PAGE_TEST_THREE = 3;      // number of pages to check for test 3


    String driverPath = "/E:/IdeaProjects/task_temerix/chromedriver.exe";
    String baseUrl = "https://epicentrk.ua/shop/elektroinstrumenty/";


    @BeforeSuite
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", driverPath);
        driver = new ChromeDriver();
        System.out.println("The setup process is completed");
        homePage();
    }

    @Test() // Test #1
    public void firstTest() {
        System.out.println("TEST #1");
        homePage();

        driver.findElement(By.linkText("Дрели")).click();
        linkCard = getLink(By.cssSelector(".product-Wrap .card .card__info > .action ~ a[href]"));
        Collections.shuffle(linkCard);

        for (int i = 0; i < countItem; i++) {
            String  oldPrice = "Нет", newPrice = "Нет", namePage;
            driver.get(linkCard.get(i));
            namePage = driver.findElement(By.cssSelector("h1.nc")).getText();

            if (isElementExists(By.cssSelector(".old-price-value"))) {
                newPrice = driver.findElement(By.cssSelector(".price-wrapper")).getText();
                oldPrice = driver.findElement(By.cssSelector(".old-price-value")).getText();
            } else {
                oldPrice = driver.findElement(By.cssSelector(".price-wrapper")).getText();
            }
            System.out.println("Название товара: " + namePage +
                                "\nСсылка: " + linkCard.get(i) +
                                "\n  Цена : " + oldPrice + "\t" + "Акционная цена: " + newPrice );
//            driver.navigate().back();
        }
    }

    @Test(alwaysRun = true)   //Test #2
    public void secondTest() {
        System.out.println("TEST #2");
        homePage();
        driver.findElement(By.linkText("Перфораторы")).click();

        for (int i = 1; i <= PAGE_TEST_TWO; i++) {
            int cardGetPrice = 0, cardNumber = 0;
            cardGetPrice = getWebElements(By.cssSelector(".product-Wrap .card__price p[title*=\"грн\"]")).size();
            cardNumber = getWebElements(By.cssSelector(".product-Wrap .card__info")).size();

            if(cardGetPrice == cardNumber){
                System.out.println("На " + i + " cтранице все товары имеют цену");
            }
            else {
                System.out.println("На " + i + " cтранице " + (cardNumber-cardGetPrice) + " товара не имеют цены");
            }
            driver.findElement(By.cssSelector(".custom-pagination__button--next")).click();
        }
    }

    @Test(alwaysRun = true) //Test #3
    public void thirdTest(){
        System.out.println("TEST #3");
        homePage();
        driver.findElement(By.linkText("Шуруповерты")).click();

        for(int i = 1; i <= PAGE_TEST_THREE; i++){
        webElements = getWebElements(By.cssSelector(".columns .hit ~ .card__name"));

            for (WebElement element : webElements){
                System.out.println(element.getText() + "\tСсылка: " + element.findElement(By.cssSelector(".custom-link")).getAttribute("href"));
            }
        driver.findElement(By.cssSelector(".custom-pagination__button--next")).click();
        }
    }

    @Test(alwaysRun = true) //Test #4
    public void fourthTest() throws InterruptedException {
        System.out.println("TEST #4");
        homePage();
        driver.findElement(By.linkText("Болгарки")).click();
        webElements = driver.findElements(By.cssSelector(".columns .card__info"));

        for (int i = webElements.size() - 1; i > 0; i--) {
            if (!webElements.get(i).getText().contains("%")) {
                webElements.remove(i);
            }
        }

        Collections.shuffle(webElements);

        for(int i = 0; i < 10;i++){
            float currentPrice, newPrice,oldPrice;
            int  percent, substringIndex, substringFromIndex;

            substringIndex = webElements.get(i).findElement(By.cssSelector(".card__info .action")).getText().indexOf('-');
            substringFromIndex = webElements.get(i).findElement(By.cssSelector(".card__info .action")).getText().indexOf('%');

            String nameCard = webElements.get(i).findElement(By.cssSelector(".nc")).getText();
            currentPrice = Float.parseFloat((webElements.get(i).findElement(By.cssSelector(".card__price-sum")).getText().replaceAll("грн","")));
            oldPrice = Float.parseFloat(webElements.get(i).findElement(By.cssSelector(".card__price-sum--old")).getText());
            percent = Integer.parseInt(webElements.get(i).findElement(By.cssSelector(".card__info .action")).getText().substring(substringIndex,substringFromIndex));
            newPrice = (float) (oldPrice + (oldPrice * (percent * 1.00 / 100)));
            try {
                Assert.assertEquals(currentPrice, newPrice);
            } catch (AssertionError e){
                System.out.println("Название товара: " + nameCard);
                System.out.println("фактическая цена: " + currentPrice + "\t ожидаемая цена: " + newPrice );
            }
        }
    }

    @AfterSuite()
    public void afterTest() {
        driver.close();
        driver.quit();
    }


    private boolean isElementExists(By cssSelector) {
        return driver.findElements(cssSelector).size() > 0;
    }

    private List<WebElement> getWebElements(By cssSelector) {
        return driver.findElements(cssSelector);
    }

    private void homePage() {
        driver.get(baseUrl);
    }

    private List<String> getLink(By cssSelector) {
        linkCard = new LinkedList<String>();
        webElements = driver.findElements(cssSelector);

        for (int i = 0; i < webElements.size(); i++) {
            linkCard.add(i, webElements.get(i).getAttribute("href"));
        }
        return linkCard;
    }

}

//    @Test()  //Test #2 Variant 2
//    public void secondTest() {
//        homePage();
//        driver.findElement(By.linkText("Перфораторы")).click();
//        for (int i = 1; i <= CHECK_PAGE; i++) {
//            webElements = driver.findElements(By.cssSelector(".product-Wrap .card > .card__info"));
//            int countGetPrice = 0, countNoPrice = 0;
//            if(webElements.size() == 22)
//                for (int j = 0; j < webElements.size(); j++) {
//                    if (webElements.get(j).getText().contains("грн")) {
//                        countGetPrice++;
//                    } else {
//                        countNoPrice++;
//                    }
//                }
//            if (countGetPrice == webElements.size()) {
//                System.out.println("На " + i + " cтранице все товары имеют цену");
//            } else {
//                System.out.println("На " + i + " cтранице " + countNoPrice + " товаров не имеют цены");
//            }
//            driver.findElement(By.cssSelector(".custom-pagination__button--next")).click();
//        }
//    }
