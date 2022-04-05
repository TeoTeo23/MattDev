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


public class GoogleServer {
    private String prefix = "https://maps.googleapis.com/maps/api/geocode/xml?address=";
    private final String key = "&key=AIzaSyAOaMNilE4OoVIaoaE3avYJDCOSoPyx3z0";

    public GoogleServer(){}

    public Place getCoordinates(String address){
        // Setting up connection
        try{
            String url = prefix + URLEncoder.encode(address, "UTF-8") + key;
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
}
