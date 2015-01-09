import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.openrdf.model.Value;
import org.openrdf.query.*;
import org.openrdf.repository.*;
import org.openrdf.repository.sparql.*;




public class Homework6FetchData {
	
	
	public static String getValue(Value val)
	{
		if(val ==null)
			return "NA";
		else 
			return val.toString();
	}
	
	public static void main(String args[])
	{
		SPARQLRepository dbpediaEndpoint = new SPARQLRepository("http://dbpedia.org/sparql");
		RepositoryConnection conn=null;
		BufferedWriter bw = null;
		  try {
			  dbpediaEndpoint.initialize();
			  conn = dbpediaEndpoint.getConnection();
			  String sparqlQueryString1= "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
				"PREFIX dbontology: <http://dbpedia.org/ontology/> "+
				"PREFIX res: <http://dbpedia.org/resource/> "+
				"PREFIX dbprop: <http://dbpedia.org/property/> "+
				"PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> "+
				"PREFIX dbpprop: <http://dbpedia.org/property/> "+
				"SELECT ?univ ?name ?loc str(?lat) as ?lati str(?long) as ?longi WHERE  {"+
				"?univ dbontology:state <http://dbpedia.org/resource/California> . "+
				"?univ dbontology:type ?type . "+
				"?univ rdfs:label ?name " +"FILTER(LANG(?name)=\"en\") . "+
				"OPTIONAL {?univ dbontology:city ?loc} . "+
				"OPTIONAL{?univ dbprop:location ?loc}. "+
				"OPTIONAL{?univ geo:lat ?lat}. "+
				" OPTIONAL {?univ geo:long ?long} . "+
				"filter(?type = res:Private_university). "+
				"} "+
				"ORDER BY (?univ) "+
				"LIMIT 500";
		     TupleQuery query = conn.prepareTupleQuery(QueryLanguage.SPARQL, sparqlQueryString1);
		     TupleQueryResult result = query.evaluate(); 

		     File objFile = new File(args[0]);
			 FileWriter fw = new FileWriter(objFile.getAbsoluteFile());
		     bw = new BufferedWriter(fw);

		     while(result.hasNext()) {
		        // and so on and so forth, see sesame manual/javadocs 
		        // for details and examples
		    	 BindingSet bindingSet = (BindingSet) result.next();
		    	   
		    	    Value name = bindingSet.getValue("name");
		    	    String nam = getValue(name);
		    	    
		    	    Value location = bindingSet.getValue("loc");
		    	    String loc = getValue(location);
		    	    	
		    	 	Value latitude = bindingSet.getValue("lati");
		    	 	String lat=getValue(latitude);
					Value longitude = bindingSet.getValue("longi");
					String longi = getValue(longitude);
		    	 bw.write(bindingSet.getValue("univ")+"@@#"+ nam +"@@#"+loc+"@@#"+lat+"@@#"+longi+"\n");
		     }
		  }
		  catch(Throwable e){e.printStackTrace();}
		  finally {
		    try {
		    	bw.close();
				conn.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		  return;
	}

}
