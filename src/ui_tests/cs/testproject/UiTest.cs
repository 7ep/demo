using System.Collections.Generic;
using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using NUnit.Framework;

namespace Tests
{


    [TestFixture]
    public class UiTest
    {

        private IWebDriver driver;
        public IDictionary<string, object> vars { get; private set; }
        private IJavaScriptExecutor js;

        [SetUp]
        public void SetUp()
        {
            driver = new ChromeDriver();
            js = (IJavaScriptExecutor)driver;
            vars = new Dictionary<string, object>();
        }

        [TearDown]
        protected void TearDown()
        {
            driver.Quit();
        }

        [Test]
        public void Test1()
        {
            driver.Navigate().GoToUrl("http://localhost:8080/demo/library.html");
            driver.FindElement(By.CssSelector(".button-form:nth-child(4) > input")).Click();
            driver.FindElement(By.LinkText("Return")).Click();
            driver.FindElement(By.Id("register_book")).Click();
            driver.FindElement(By.Id("register_book")).SendKeys("some book");
            driver.FindElement(By.Id("register_book_submit")).Click();
            driver.FindElement(By.LinkText("Return")).Click();
            driver.FindElement(By.Id("register_borrower")).Click();
            driver.FindElement(By.Id("register_borrower")).SendKeys("some borrower");
            driver.FindElement(By.Id("register_borrower_submit")).Click();
            driver.FindElement(By.LinkText("Return")).Click();
            driver.FindElement(By.Id("lend_book")).Click();
            driver.FindElement(By.Id("lend_book")).SendKeys("some book");
            driver.FindElement(By.Id("lend_borrower")).SendKeys("some borrower");
            driver.FindElement(By.Id("lend_book_submit")).Click();
            Assert.That(driver.FindElement(By.Id("result")).Text, Is.EqualTo("SUCCESS"));
        }
    }
}