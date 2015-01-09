import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.manager.RemoteRepositoryManager;


public class TriplesFrom {
	
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
			   String queryString =
				   "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				   "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>"+
				   "SELECT ?x ?p ?y WHERE { ?x ?p ?y } ";
			   
			   TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
				  TupleQueryResult result = tupleQuery.evaluate();
				  while (result.hasNext()) {  // iterate over the result
						BindingSet bindingSet = (BindingSet) result.next();
				  
						System.out.println(bindingSet.getValue("x")+"\t"+bindingSet.getValue("p")+"\t"+bindingSet.getValue("y"));
				  
				  
				  }
			   
			   
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				con.close();
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
