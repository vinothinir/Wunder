package Scenarios;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.By;
import org.testng.annotations.*;
import java.text.DecimalFormat;


import java.net.URL;
import java.net.MalformedURLException;
import java.util.logging.Level;

public class fastTip {
    private String reportDirectory = "reports";
    private String reportFormat = "xml";
    private String testName = "Untitled";
    protected AndroidDriver<AndroidElement> driver = null;
    String iconText;


    DesiredCapabilities dc = new DesiredCapabilities();

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        dc.setCapability("reportDirectory", reportDirectory);
        dc.setCapability("reportFormat", reportFormat);
        dc.setCapability("testName", testName);
        dc.setCapability(MobileCapabilityType.UDID, "041604dbf02a0402");
        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "org.traeg.fastip");
        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".MainActivity");
        driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), dc);
        driver.setLogLevel(Level.INFO);
    }

    @Test
    public void testScenarios() {
        driver.executeScript( "seetest:client.setDevice(\"adb:SM-G920F\")" );
        driver.startActivity( new Activity( "org.traeg.fastip", ".MainActivity" ) );
        //Test for home screen verification
        testCheckHomeScreen();
        //Test for bill amount verification
        testCheckBill();
        //Test for Settings screen verification
        testCheckSettings();
        }
    //Test for home screen verification
    public void testCheckHomeScreen() {
        if (driver.findElement( By.xpath( "//*[@id='action_bar_title']" ) ).isDisplayed() && driver.findElement( By.xpath( "//*[@id='home']" ) ).isDisplayed()&& driver.findElement( By.xpath( "//*[@id='menu_settings']" ) ).isDisplayed()){
            iconText = driver.findElement( By.xpath( "//*[@id='action_bar_title']" ) ).getText();
            if (iconText.equals( "FasTip" )) {
                System.out.println( "Test Home screen icon and text verified: Pass" );
            }
            else{
                System.out.println( "Test Home screen icon and text verified: Fail" );
            }
        }
    }

    //Test for Settings screen verification
    public void testCheckSettings() {
        driver.findElement( By.xpath( "//*[@id='menu_settings']" ) ).click();
        if (driver.findElement( By.xpath( "//*[@id='action_bar_title']" ) ).isDisplayed()){
            if (driver.findElement( By.xpath( "//*[@id='action_bar_title']" ) ).getText().equals( "Settings" )) {
                System.out.println( "Settings screen with label Setting is verified: Pass" );
            }
            else{
                System.out.println( "Settings screen with label Setting is verified: Fail" );
            }

        }
    }
    //Test for Bill Calculation verification
    public void testCheckBill() {
        String billAmt="150";
        String percent,tipamt,totalamt;
        Float totalcalcamt,tipcalcamt;
        DecimalFormat df = new DecimalFormat("0.00");

        driver.findElement( By.xpath( "//*[@id='billAmtEditText']" ) ).setValue( billAmt );
        driver.findElement( By.xpath( "//*[@id='calcTipButton']" ) ).click();
        percent=driver.findElement( By.xpath( "//*[@id='tipPctTextView']" ) ).getText();
        tipamt=driver.findElement( By.xpath( "//*[@id='tipAmtTextView']" ) ).getText();
        totalamt=driver.findElement( By.xpath( "//*[@id='totalAmtTextView']" ) ).getText();
        percent=percent.substring( 0,percent.length()-1 );
        System.out.println("Percentage:"+percent  );
        tipamt=tipamt.substring( 1 );
        System.out.println("Tip Amount:"+tipamt  );
        totalamt=totalamt.substring( 1 );
        System.out.println("Total Amount:"+totalamt  );
        tipcalcamt=Float.valueOf(billAmt)*Float.valueOf(percent)/100;
        //System.out.println("Calculated Tip Amount:"+df.format( tipcalcamt));
        totalcalcamt=Float.valueOf( billAmt )+tipcalcamt;
       // System.out.println("Calculated Total Amount:"+df.format( totalcalcamt));
        String tip=df.format( tipcalcamt );
        tip=tip.replace( ",","." );
        System.out.println("Calculated Tip Amount:"+ tip);
        String total=df.format( totalcalcamt );
        total=total.replace( ",","." );
        System.out.println("Calculated Total Amount:"+ total);
        if(tipamt.equals( tip ) && totalamt.equals(total  )){
            System.out.println( "Calculated tip amount and total amount is verified: Pass" );
        }
        else
        {
            System.out.println( "Calculated tip amount and total amount is verified: Failed" );
        }

    }

    @AfterMethod
    public void tearDown() {
        driver.closeApp();
        driver.quit();
    }
}