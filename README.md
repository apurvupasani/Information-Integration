# Information-Integration

This is not a project as such but more of a collection of projects. Each Homework contains different features and can be used more for the purpose of hacking new applications or as a guide to create own applications

Academic Integrity : It is an academic integrity document. I would also expect anyone using these homeworks as means to create something of their own. I myself, have not or never will endorse any form of cheating or plagarism of any kind

Web Scrapper: Its a web scrapper in Python using BeautifulSoup library. There is JSoup which is Java equivalent of the same. The scrapper scrapes guggenheim museum website and is custom made for that. However it can be used as a guide to web scrapping.

RDF Graph : It contains an example for an RDF graph. The relationship and entities are written in Turtle and I used any23.org to create the required diagram. I would recommend anyone working on OWL or RDF to write the triples in turtle format and then convert it to RDF/XML or OWL/XML if required.

SPARQL queries: These contain queries to DBPedia . More importantly, a good guide for SPARQL queries. (God knows how much time I spent on writing these myself. Dont want others to experience the same pain)

Data Cleaning: Any data that is scrapped requires cleaning. Google Refine / Open Refine is a good tool which helps you with it

GAV/LAV rules: Theoritical constructs for helping integrated multiple data sources by means of federated queries

Triple Stores : How to extract data from online stores, create and query the local triple store using JENA/SESAME.

KARMA : How to map the dataset/datasets with existing ontology(s). Karma is an excellent tool which helps create new RDF for by reusing existing ontologies

OWL ontology : It helps as a guide to explain how to create a OWL ontology, given information. Ontology representation in OWL Full

Yahoo Pipes : While integrating data from multiple sources, it is usually unwise to create wrappers for each source. Yahoo Pipes is a good way of fetching, cleaning and merging the data from multiple sources ie REST APIs, excel sheets, CSVs. I believe that the first rule of data integration (programming is general) is to reuse existing software or piece of code and I believe that Yahoo Pipes is a great data integration tool.

Record Linkage: Consider 2 tuples in 2 different databases. How will you know if they are same or different. This is done using record linkage strategies. This assignment uses FRIL system which helps a great deal in record linkage.

Entity Extraction : Sometimes extracting information from unstructured/semistructured text is important. Entity extraction (Named Entity Recognition in NLP) is one way of doing that. For this assignment, I used an external web service which does named entity recognition for us. I scrapped news articles via google news (used JSoup for this) and sent the data to the web service which returned me with triples. I put these triples in a triple store and then queried the store to fetch the required entities.
