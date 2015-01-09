package main;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import java.io.*;
public class HelloWorld {


	public static void main(String args[])
	{
		String sparqlQueryString1= "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
		"PREFIX dbontology: <http://dbpedia.org/ontology/> "+
		"PREFIX res: <http://dbpedia.org/resource/> "+
		"PREFIX dbprop: <http://dbpedia.org/property/> "+
		"PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> "+
		"PREFIX dbpprop: <http://dbpedia.org/property/> "+
		"SELECT ?univ ?loc ?lat ?long WHERE  {"+
		"?univ dbontology:state <http://dbpedia.org/resource/California> . "+
		"?univ dbontology:type ?type . "+
		"OPTIONAL {?univ dbontology:city ?loc} . "+
		"OPTIONAL{?univ dbprop:location ?loc}. "+
		"OPTIONAL{?univ geo:lat ?lat}. "+
		" OPTIONAL {?univ geo:long ?long} . "+
		"filter(?type = res:Private_university). "+
		"} "+
		"ORDER BY (?univ) "+
		"LIMIT 500";
		QueryExecution qexec = null;
		BufferedWriter bw = null;
		OutputStream fout= null;
		try
		{
			//fout = new FileOutputStream(new File("QueryOutputFormatted.txt")); 
			Query query = QueryFactory.create(sparqlQueryString1);
			qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

			ResultSet results = qexec.execSelect();
			File objFile = new File(args[0]);
			FileWriter fw = new FileWriter(objFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);

			while(results.hasNext())
			{   
				QuerySolution q = results.next();
				Literal latitude = q.getLiteral("?lat");
				String lat = "";
				if(latitude != null)
				{
					lat = latitude.toString();
				}
				Literal longitude = q.getLiteral("?lat");
				String longi = "";
				if(longitude != null)
				{
					longi = longitude.toString();
				}
				
				bw.write(q.getResource("?univ")+"\t"+q.getResource("?loc")+"\t"+lat+"\t"+longi+"\n");
			}
			//ResultSetFormatter.out(fout, results, query);       


		}
		catch(Throwable e){e.printStackTrace();}
		finally{
			try {
				bw.close();
				qexec.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
