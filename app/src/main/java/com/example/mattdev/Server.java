package com.example.mattdev;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Server {
    private String instance; // Server
    // Google Server data
    private String GPrefix = "https://maps.googleapis.com/maps/api/geocode/xml?address=";
    private final String GKey = "&key=AIzaSyAOaMNilE4OoVIaoaE3avYJDCOSoPyx3z0";
    // Openweather Server data
    private String OPWPrefix = "https://api.openweathermap.org/data/2.5/weather?q=";
    private String OPWFormat = "&mode=";
    private final String OPWKey = "&appid=d62ccb21b58a518a07a569e1843435d7";

    private boolean isCalled = false;
    public Server(String instance){
        this.instance = instance;
    }

    protected String getInstance(){ return this.instance; }
    protected Boolean calledServer(){ return this.isCalled; }

    protected void setCallStatus(boolean callStatus){ this.isCalled = callStatus; }

    public Place getCoordinates(String address){
        // Setting up connection
        try{
            String url = GPrefix + URLEncoder.encode(address, "UTF-8") + GKey;
            // System.out.println(url); Test if url is correct.
            URL server = new URL(url);
            HttpsURLConnection service = (HttpsURLConnection)server.openConnection();
            service.setDoInput(true);
            service.setRequestMethod("GET");
            service.setReadTimeout(1000);
            service.setConnectTimeout(1000);
            service.connect();
            int statusCode = service.getResponseCode();
            if(statusCode != 200)
                return null;

            // Build variables to Parse XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(service.getInputStream());
            Element root = doc.getDocumentElement();

            // XML parsing to get Locations
            NodeList latitudeList = root.getElementsByTagName("lat");
            NodeList longitudeList = root.getElementsByTagName("lng");
            if(latitudeList != null && latitudeList.getLength() > 0 && longitudeList != null && longitudeList.getLength() > 0)
                return (new Place(Double.parseDouble(latitudeList.item(0).getFirstChild().getNodeValue()), Double.parseDouble(longitudeList.item(0)
                        .getFirstChild().getNodeValue())));
        } catch (IOException | SAXException | ParserConfigurationException exception) {
            exception.printStackTrace();
            exception.getMessage();
            return null;
        }
        return null;
    }

    public String requestTemperature(String city, String mode){
        // Build variables and setting up connection
        Element weatherList;
        String value;
        try{
            String url = OPWPrefix + URLEncoder.encode(city, "UTF-8") + OPWFormat + URLEncoder.encode(mode, "UTF-8") + OPWKey;
            // System.out.println(url); Test if URL is correct.
            URL server = new URL(url);
            HttpsURLConnection service = (HttpsURLConnection)server.openConnection();
            service.setDoInput(true);
            service.setRequestMethod("GET");
            service.setReadTimeout(1000);
            service.setConnectTimeout(1000);
            service.connect();
            int statusCode = service.getResponseCode();
            if(statusCode != 200)
                return null;
            // Build variables to parse XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(service.getInputStream());
            Element root = doc.getDocumentElement();

            // XML parsing to get Temperature
            weatherList = (Element)root.getElementsByTagName("temperature").item(0);
            value = weatherList.getAttribute("value");
        }catch(IOException | SAXException | ParserConfigurationException exception){
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            return null;
        }
        return value;
    }
}
