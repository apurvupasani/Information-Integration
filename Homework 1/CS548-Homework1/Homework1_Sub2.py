list = {"NEW YORK":"Solomon R. Guggenheim Museum","VENICE":"Peggy Guggenheim collection","BILBAO":"Guggenheim Museum Bilbao"}
text = "This museum is located in New York"
text = text.upper();
for item in list:
		if item in text:	
			print item.lower().title() + " "+list[item]
			break
