package com;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;

public class BauerMedia {

    //this class is used for starting and building reports
    ExtentReports report;
    //this class is used to create HTML report file
    ExtentHtmlReporter htmlReporter;
    //this will  define a test, enables adding logs, authors, test steps
    ExtentTest extentLogger;

    Response response;

    @BeforeTest
    public void beforeTest(){
        //initialize the class
        report = new ExtentReports();

        //create a report path
        String projectPath = System.getProperty("user.dir");
        String path = projectPath + "/test-output/report.html";

        //initialize the html reporter with the report path
        htmlReporter = new ExtentHtmlReporter(path);

        //attach the html report to report object
        report.attachReporter(htmlReporter);

        htmlReporter.config().setReportName("Bauer Media API task");

        //set environment information
        report.setSystemInfo("Environment","QA");
        report.setSystemInfo("Browser","https://listenapi.planetradio.co.uk/api9.2/stations/gb" );
        report.setSystemInfo("OS",System.getProperty("os.name"));

        response= RestAssured.given().
                get("https://listenapi.planetradio.co.uk/api9.2/stations/gb");
    }
    @AfterTest
    public void afterTest(){
        //this is when the report is actually created
        report.flush();

    }


    @Test
    public void stationId(){
        //give name to current test
        extentLogger= report.createTest("stationId verification Test");

        //test steps
        extentLogger.info("assign response to jsonpath");
        JsonPath jsonPath = response.jsonPath();

        extentLogger.info("create List for all stationID");
        List<Object> stationIDlist = new ArrayList<>();

        extentLogger.info("add values from jsonpath to list");
        stationIDlist.addAll(jsonPath.getList("stationId"));


        for (int i=0; i<stationIDlist.size();i++) {
            Assert.assertTrue(stationIDlist.get(i) instanceof Integer);
            extentLogger.pass("stationId contains required characters");
        }
    }
    @Test
    public void stationCode(){
        extentLogger= report.createTest("stationCod verification Test");

        extentLogger.info("assign response to jsonpath");
        JsonPath jsonPath = response.jsonPath();

        extentLogger.info("create List for all stationCode");
        List<String> stationCode = new ArrayList<>();

        extentLogger.info("add values from jsonpath to list");
        stationCode.addAll(jsonPath.getList("stationCode"));


        for (int i=0; i<stationCode.size();i++) {
            Assert.assertTrue(stationCode.get(i).length()==3);
            extentLogger.pass("stationCode contains required characters");
        }
    }
    @Test
    public void stationType(){
        extentLogger= report.createTest("stationType verification Test");

        extentLogger.info("assign response to jsonpath");
        JsonPath jsonPath = response.jsonPath();

        extentLogger.info("create List for all stationType");
        List<String> stationType = new ArrayList<>();

        extentLogger.info("add values from jsonpath to list");
        stationType.addAll(jsonPath.getList("stationType"));


        for (int i=0; i<stationType.size();i++) {
            Assert.assertTrue(stationType.get(i).equals("radio"));
            extentLogger.pass("stationType contains required characters");
        }
    }
    @Test
    public void stationBrandCode(){
        extentLogger= report.createTest("stationBrandCode verification Test");

        extentLogger.info("assign response to jsonpath");
        JsonPath jsonPath = response.jsonPath();

        extentLogger.info("create List for all stationBrandCode");
        List<String> stationBrandCode= new ArrayList<>();

        extentLogger.info("add values from jsonpath to list");
        stationBrandCode.addAll(jsonPath.getList("stationBrandCode"));


        for (String s : stationBrandCode) {
            char [] charListofString= s.toCharArray();

            for (char c : charListofString) {
                System.out.println("c = " + c);
                Assert.assertTrue(Character.isUpperCase(c) || Character.isDigit(c) || c=='-' );
                extentLogger.fail("stationBrandCode does not contain required characters");
//                if(Character.isUpperCase(c) || Character.isDigit(c) || c=='-' ){
//                    extentLogger.pass("stationBrandCode contains required characters");
//
//                }else
//                    extentLogger.fail("stationBrandCode does not contain required characters");
            } //Failed because there are underscore not hyphens  AND  there are lower case letters
        }
       // extentLogger.fail("stationBrandCode does not contain required characters");
    }

    @Test
    public void stationDADIChannelID(){
        extentLogger= report.createTest("stationDADIChannelID verification Test");

        extentLogger.info("assign response to jsonpath");
        JsonPath jsonPath = response.jsonPath();

        extentLogger.info("create List for all stationDADIChannelID");
        List<String> stationDADIChannelId= new ArrayList<>();

        extentLogger.info("add values from jsonpath to list");
        stationDADIChannelId.addAll(jsonPath.getList("stationDADIChannelId"));

        extentLogger.info("assertion for stationDADIChannelID");
        for (String s : stationDADIChannelId) {
            char [] charListofString= s.toCharArray();

            for (char c : charListofString) {

                Assert.assertTrue(Character.isLowerCase(c) || Character.isDigit(c) || c=='-' );
                extentLogger.pass("stationBrandCode contains required characters");
            }
        }
    }
    @Test
    public void stationStreams(){
        extentLogger= report.createTest("stationDADIChannelID verification Test");

        extentLogger.info("assign response to jsonpath");
        JsonPath jsonPath = response.jsonPath();

        extentLogger.info("create List for all stationDADIChannelID");
        List<Object> stationStreamsList= new ArrayList<>();

        extentLogger.info("add values from jsonpath to list");
        stationStreamsList.addAll(jsonPath.getList("stationStreams"));

        extentLogger.info("create List for all streamType");
        List<String> streamTypeList=new ArrayList<>();

        boolean adtsFlag=true;
        boolean mp3Flag=true;

        for (int i = 0; i < stationStreamsList.size(); i++) {

            streamTypeList.addAll(jsonPath.getList("stationStreams.streamType[" + i + "]"));

            if(!streamTypeList.contains("adts")){
                adtsFlag=false;
            }
            if(!streamTypeList.contains("mp3")){
                mp3Flag=false;
            }
            Assert.assertTrue(adtsFlag && mp3Flag);
            extentLogger.pass("streamType in stationStreams contains required characters");
        }
    }
}

