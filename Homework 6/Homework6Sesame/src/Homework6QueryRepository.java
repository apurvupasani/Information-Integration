import java.util.TreeMap;

import org.openrdf.OpenRDFException;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.manager.RemoteRepositoryManager;

class GeoPoint
{
	private String univURI="";
	private String univName = "";
	private String latValue = "";
	private String longValue = "";
	private String city ="";
	
	public GeoPoint(String univURI, String univName, String city, String latValue, String longValue)
	{
		this.univURI = univURI;
		this.univName = univName;
		this.latValue = latValue;
		this.longValue = longValue;
		this.city = city;
	}
	
	public String getCity()
	{
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public String getUnivURI() {
		return univURI;
	}
	public void setUnivURI(String univURI) {
		this.univURI = univURI;
	}
	public String getUnivName() {
		return univName;
	}
	public void setUnivName(String univName) {
		this.univName = univName;
	}
	public String getLatValue() {
		return latValue;
	}
	public void setLatValue(String latValue) {
		this.latValue = latValue;
	}
	public String getLongValue() {
		return longValue;
	}
	public void setLongValue(String longValue) {
		this.longValue = longValue;
	}
	public String toString() {
		return "GeoPoint [univURI=" + univURI + ", univName=" + univName
				+ ", latValue=" + latValue + ", longValue=" + longValue + "]";
	}
	
	
}

public class Homework6QueryRepository {
	
	public static String getValue(Value val)
	{
		if(val ==null)
			return "";
		else 
			return val.toString();
	}
	

  public static double calculateHaverSineDistance(GeoPoint g1, GeoPoint g2)
  {
	  
	  double radiusOfEarth = 3958.756;
	  
	  if("\"\"".equals(g1.getLatValue()) || "\"\"".equals(g1.getLongValue()))
		  return -1;
	  else if("\"\"".equals(g2.getLatValue()) || "\"\"".equals(g2.getLongValue()))
		  return -1;
	  else
	  {
		  double g1Lat = Double.parseDouble(g1.getLatValue().split("\\^\\^")[0].replaceAll("\"",""));
		  double g1Long = Double.parseDouble(g1.getLongValue().split("\\^\\^")[0].replaceAll("\"",""));
		  
		  double g2Lat = Double.parseDouble(g2.getLatValue());
		  double g2Long = Double.parseDouble(g2.getLongValue());
		
		  double g1LatRadians =   Math.toRadians(g1Lat);
		  double g2LatRadians = Math.toRadians(g1Long);
		  
		  double diffLatRadians = Math.toRadians(g2Lat - g1Lat);
		  double diffLongRadians = Math.toRadians(g2Long - g1Long);
		  
		  double a = Math.pow(Math.sin(diffLatRadians/2),2) +
	        Math.abs(Math.cos(g1LatRadians) * Math.cos(g2LatRadians)) *
	        Math.pow(Math.sin(diffLongRadians/2),2);
	
		  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		  	  
		  return radiusOfEarth*c;
		  
	  }
	  
	  
  }
	
	
	
	public static void main(String args[])
	{
		
		String serverUrl = "http://localhost:6655/openrdf-sesame";
		RemoteRepositoryManager manager = new RemoteRepositoryManager(serverUrl);
		RepositoryConnection con =null;
		Repository myRepository=null;
		
		try {
			manager.initialize();
			myRepository = manager.getRepository("Homework6");
			   con = myRepository.getConnection();
			   try {
				  TreeMap<String,GeoPoint> tree = new Tre; 
				  GeoPoint UCLA = new GeoPoint("http://dbpedia.org/resource/University_of_California,_Los_Angeles", "University of California, Los Angeles","http://dbpedia.org/resource/Los_Angeles" ,"34.072224", "-118.444099"); 
				  String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>"+
" Select ?univ ?univname ?c ?lat ?long "+
" WHERE{ "+
" ?univ <https://schema.org/name> ?univname . "+
" ?univ <https://schema.org/location> ?b . "+
" ?b <https://schema.org/address> ?c . "+
" ?b <https://schema.org/latitude> ?lat ."+
" ?b <https://schema.org/longitude> ?long . "+
" } ";
				  TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

				  TupleQueryResult result = tupleQuery.evaluate();
				  try {
					  int count = 0;
			              while (result.hasNext()) {  // iterate over the result
						BindingSet bindingSet = (BindingSet) result.next();
						GeoPoint objPoint  = new GeoPoint(getValue(bindingSet.getValue("univ")), getValue(bindingSet.getValue("univName")), getValue(bindingSet.getValue("c")), getValue(bindingSet.getValue("lat")), getValue(bindingSet.getValue("long")));
						double dist = calculateHaverSineDistance(objPoint, UCLA);
						if(dist !=-1 && dist <=200)
						{
							System.out.println(objPoint.getUnivURI()+"\t"+objPoint.getUnivName()+"\t"+objPoint.getLatValue()+"\t"+objPoint.getLongValue()+"\t"+dist);
						count++;
						}}
			              System.out.println(count);
				  }
				  finally {
				      result.close();
				  }
			   }
			   finally {
			      con.close();
			   }
			}
			catch (Exception e) {
			   e.printStackTrace();
			}
		
	}
	
}
