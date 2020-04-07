package com.coveros.training;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.Assert.assertEquals;

public class Mytest {
  private WebDriver driver;

  @Before
  public void setUp() {
    driver = new ChromeDriver();
  }

  @After
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void test1() {
    driver.get("http://localhost:8080/demo/library.html");
    driver.findElement(By.cssSelector(".button-form:nth-child(4) > input")).click();
    driver.findElement(By.linkText("Return")).click();
    driver.findElement(By.id("register_book")).click();
    driver.findElement(By.id("register_book")).sendKeys("some book");
    driver.findElement(By.id("register_book_submit")).click();
    driver.findElement(By.linkText("Return")).click();
    driver.findElement(By.id("register_borrower")).click();
    driver.findElement(By.id("register_borrower")).sendKeys("some borrower");
    driver.findElement(By.id("register_borrower_submit")).click();
    driver.findElement(By.linkText("Return")).click();
    driver.findElement(By.id("lend_book")).click();
    driver.findElement(By.id("lend_book")).sendKeys("some book");
    driver.findElement(By.id("lend_borrower")).sendKeys("some borrower");
    driver.findElement(By.id("lend_book_submit")).click();
    final String result = driver.findElement(By.id("result")).getText();
    assertEquals("SUCCESS", result);
  }


}
