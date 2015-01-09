import org.openrdf.model.*;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.*;
import org.openrdf.repository.*;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.manager.RemoteRepositoryManager;
import org.openrdf.repository.sparql.*;
import java.io.*;
import java.util.*;

public class Homework6ConstructTriples {
	
	public static void main(String args[])
	{
		String serverUrl = "http://localhost:6655/openrdf-sesame";
		RemoteRepositoryManager manager = new RemoteRepositoryManager(serverUrl);
		RepositoryConnection con =null;
		Repository myRepository;
		Scanner sc = null;
		try {
			manager.initialize();
			myRepository = manager.getRepository("Homework6");
			ValueFactory f = myRepository.getValueFactory();
			con = myRepository.getConnection();
			
			URI collegeUniv = f.createURI("http://schema.org/CollegeOrUniversity");
			URI univName = f.createURI("https://schema.org/name");
			URI latitude = f.createURI("https://schema.org/latitude");
			URI longitude = f.createURI("https://schema.org/longitude");
			URI place = f.createURI("https://schema.org/Place");
			URI location = f.createURI("https://schema.org/location");
			URI address = f.createURI("https://schema.org/address");
			
			sc = new Scanner(new File(args[0]));
			String s = "";
			while(sc.hasNextLine())
			{
				s=sc.nextLine();
				String ar[] = s.split("@@#");
				System.out.println(s);
				con.add(f.createURI(ar[0]),RDF.TYPE,collegeUniv,new Resource[3]);
				BNode b = f.createBNode();
				con.add(b,RDF.TYPE, place, new Resource[3]);
				if("NA".equals(ar[2]))
					con.add(b,address,f.createLiteral(""), new Resource[3]);	
				else
				con.add(b,address,f.createURI(ar[2]), new Resource[3]);
				if("NA".equals(ar[3]))
					con.add(b,latitude,f.createLiteral(""), new Resource[3]);
				else
				{
					ar[3] = ar[3].replaceAll("\"","");
					con.add(b,latitude,f.createLiteral(Double.parseDouble(ar[3].substring(0,5))), new Resource[3]);
				}
				if("NA".equals(ar[4]))
					con.add(b,longitude,f.createLiteral(""), new Resource[3]);
				else
				{
					ar[4] = ar[4].replaceAll("\"","");
					con.add(b,longitude,f.createLiteral(Double.parseDouble(ar[4].substring(0,5))), new Resource[3]);
				}
				con.add(f.createURI(ar[0]),univName,f.createLiteral(ar[1]), new Resource[3]);
				con.add(f.createURI(ar[0]), location, b, new Resource[3]);
				
			
				
			}
			
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
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
			sc.close();
		}
		
	}

}
