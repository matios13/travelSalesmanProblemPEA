package travelSalesman;

import java.io.File;
import java.security.KeyStore.Entry.Attribute;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class GenerateProblem {

	public ArrayList<City> randomProblemGenerator(int numberOfCities){
		Random generator = new Random();
		ArrayList<City> listOfCity=new ArrayList<City>();
		for(int i=1;i<=numberOfCities;++i){
			
			ArrayList<Integer> distances= new ArrayList<Integer>();
			
			for(int j=0;j<numberOfCities-i;++j){
				distances.add(generator.nextInt(9)+1);
			}
			listOfCity.add(new City(distances,i));
			
		}
		return listOfCity;
	}
	
	public ArrayList<City> generateFromXML(String name){
		ArrayList<City> cities =  new ArrayList<City>();
		File fXmlFile = new File("C:\\tspLib\\"+name+".xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (Exception e) {
			System.out.println("Problem");
			e.printStackTrace();
			return null;
		}
		
				
		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("vertex");
		NodeList edgeList;

		// For each vertex, get all "edge" children
		for (int i = 0; i < nList.getLength(); i++)  {
		    edgeList = ((Element)nList.item(i)).getElementsByTagName("edge");
		    ArrayList<Integer> costs = new ArrayList<Integer>();
		    // For each edge under this vertex, do something
		    for (int j = i; j < edgeList.getLength(); j++) {
		    	
		    	Double test = new Double ((((Element)edgeList.item(j)).getAttribute("cost")));
		    	costs.add(new Integer((int)test.doubleValue()));
		        
		    }
		    cities.add(new City(costs,i+1));
		}
		return cities;
	}
	
	public int getNumberOfCities(String name){
		ArrayList<City> cities =  new ArrayList<City>();
		File fXmlFile = new File("C:\\tspLib\\"+name+".xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (Exception e) {
			System.out.println("Problem");
			e.printStackTrace();
			return 0;
		}
		
				
		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("vertex");
		return nList.getLength();
	}
		
}
