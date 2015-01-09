from bs4 import BeautifulSoup
import requests;
import json;
import jsonpickle;
#Class to store the painting value
class Artist:
	def __init__(self,artistNo,artistName):
		self.artistNo = artistNo;		
		self.artistName = artistName;
		self.artistBirthDate = "";
		self.artistBirthPlace = "";
		self.artistBirthCountry = "";
		self.artistDeathDate = "";
		self.artistDeathPlace = "";
		self.artistDeathCountry="";
		self.biography = "";
	def getDetails(self):
		return "[ "+ self.artistName + " "+self.artistBirthDate+" "+self.artistBirthCountry+"]";

class Painting:
	
	def __init__(self,paintingNo,url):
	      	self.url = url		
		self.paintingNo = paintingNo;
		self.paintingName = "";
		self.artist = None;
		self.paintingDate = ""
		self.paintingLocation = ""
		self.paintingLocationPlace = ""
		self.paintingMaterial = "" 

	def getDetails(self):
		return "{"+self.url+" "+self.paintingName+" "+self.artist.getDetails()+" "+self.paintingLocationPlace +"}";
	
def formatArtistNameForKey(artistName):
	return artistName.replace(" ","").upper();

def checkArtistExists(artistName,artists):
	
	artistName = fortmatArtistNameForKey(artistName)
	if artistName in artists:
		return true;
	return false;

def getPaintingLocation(painting, text):
	list = {"NEW YORK":"Solomon R. Guggenheim Museum","VENICE":"Peggy Guggenheim collection","BILBAO":"Guggenheim Museum Bilbao"}
	text = text.upper();
        for item in list:
		if item in text:
			painting.paintingLocation = list[item]
			painting.paintingLocationPlace = item.lower().title()
			return
	item = "NEW YORK"
	painting.paintingLocation = list[item]
	painting.paintingLocationPlace = item.lower().title()

	

def getAdditionalInfo(painting,url):
	r  = requests.get(url);
	soup= BeautifulSoup(r.text);
	divData = soup.find("div",{"id":"collections-detail-date-bio"});
	artistData = divData.text.split('		')[0];
	firstDataPart = "";
	lastDataPart = "";
	if ";" in artistData:
		firstDataPart = artistData.split(";")[0] 
		lastDataPart = artistData.split(";")[1]
	else:
		firstDataPart = artistData

	birthArr = firstDataPart.split(",")
	if len(birthArr) == 3:
		val = birthArr[0].strip().split(" ")
		if len(val) == 2:
			painting.artist.artistBirthDate = val[1]
		painting.artist.artistBirthPlace = birthArr[1].strip()
		painting.artist.artistBirthCountry = birthArr[2].strip()
	elif len(birthArr) == 2:
		val = birthArr[0].strip().split(" ")
		if len(val) > 1:
			painting.artist.artistBirthDate = val[1]
		painting.artist.artistBirthPlace = birthArr[1].strip()
	elif len(birthArr) ==1:
		painting.artist.artistBirthDate = birthArr[0].strip().split(" ")[1]

	painting.artist.artistBirthDate = painting.artist.artistBirthDate.strip()
	if painting.artist.artistBirthDate !="" and painting.artist.artistBirthDate.isdigit() == False:
		painting.artist.artistBirthDate = "";
 
	if lastDataPart != "":
		
		deathArr = lastDataPart.split(",")

		if len(deathArr) == 3:
			painting.artist.artistDeathDate = deathArr[0].strip().split(" ")[1]
			painting.artist.artistDeathPlace = deathArr[1].strip()
			painting.artist.artistDeathCountry = deathArr[2].strip()
		elif len(deathArr)==2:
			painting.artist.artistDeathDate = deathArr[0].strip().split(" ")[1]
			painting.artist.artistDeathPlace = deathArr[1].strip()
		else:
			painting.artist.artistDeathDate = deathArr[0].strip().split(" ")[1]

	painting.artist.artistDeathDate = painting.artist.artistDeathDate.strip()
	if painting.artist.artistDeathDate !="" and painting.artist.artistDeathDate.isdigit() == False:
		painting.artist.artistDeathDate = "";

	# Getting the biography of the artist

	contentSoup= BeautifulSoup(requests.get("http://www.guggenheim.org"+divData.a.get("href")).text);
	bioContent = contentSoup.find("div",{"id":"bio"});
	paragraphs = bioContent.findAll("p")
	paras = ""

	for i in range(1,len(paragraphs)-1):
		paras+=paragraphs[i].text+"\n"
	
	painting.artist.biography = paras
	
	### Here we capture the preview text of the image to get painting material

	divPreviewImage = soup.find("div",{"id":"collections-preview-image"});
	previewText = divPreviewImage.text.strip()
	previewText1 = previewText.split(".")
	materialAndSize = previewText1[1].strip().split(",")
	size = ""
	size2=""
	if len(materialAndSize) > 2:
		size = materialAndSize[0:len(materialAndSize)-2]
		for e in size:
			size2+=e+", "
		painting.material = size2[0:len(size2)-1]
	else:
		painting.material = materialAndSize[0]
	
	# Find the location
	getPaintingLocation(painting, previewText1[2].strip())


def getIndividualPainting(counter,paintingList,artistCounter,paintingCounter):
	strURL = "http://www.guggenheim.org/"; 
	# Capture frame data	
	frameData = counter.find("div",{"class":"frame"});
	nextURL = strURL+frameData.a.get("href");
	painting = Painting(paintingCounter,frameData.img.get("src"));
	
	# Capture all caption data
	captionData =  counter.find("div",{"class":"caption"});	
	painting.paintingName = captionData.find("span",{"class":"artwork-title"}).a.i.text;
	painting.paintingDate = getPaintingDates(captionData.find("span",{"class":"artwork-date"}).text)

	#Check if artist exists or not. If it does, 
	artistName = captionData.find("span",{"class":"artist"}).text
	artist = Artist(artistCounter,artistName);
	artistCounter+=1
	painting.artist = artist	
	# Second level - Painting level
 	getAdditionalInfo(painting,nextURL)
	# Third level - Biography
	paintingList[paintingCounter] = painting
	paintingCounter+=1


def getPaintingDates(string):
	string = string.strip()
	if len(string.split(" ")) > 1 :
		s = string.split(" ")
		s[len(s)-1] = s[len(s)-1].replace("ca.","");
		s[len(s)-1] = s[len(s)-1].replace("]","");
		if ")" in s[len(s)-1]:
			return s[0].strip();
		if s[len(s)-1]=="Paris":
			return s[len(s)-2].replace(",","").strip()
			
		return s[len(s)-1]		

	else:
		return string;

def getPagePaintings(soup,painting,artistCounter,paintingCounter):
	products = soup.findAll("div",{"class":"artworks-item"});
	for counter in products:		
		paintingCounter = len(painting) + 1
		getIndividualPainting(counter,painting,artistCounter,paintingCounter);			

print "Start job"
# Request the landing page for the paintings 
r  = requests.get("http://www.guggenheim.org/new-york/collections/collection-online/artwork-types/195198");
#Copy the text into a variable
soup = BeautifulSoup(r.text);
#The intent of this excercise is to find the total no of pages which display painting information
spanCount = (soup.find("span",{"class":"x-of-y"}).string.split(' ')[3]);

paintingCounter = 1
artistCounter = 1
painting = {}
#artist = {}
# Loop on all pages starting from 1
print "Starting to fetch the records from the muesum website"

strPageString = "http://www.guggenheim.org/new-york/collections/collection-online/artwork-types/195198?page=";
for pageCounter in range(1,int(spanCount)):
	print "Working on page "+str(pageCounter)+ " of "+spanCount;	
	pageRequest = requests.get(strPageString+str(pageCounter));
        localSoup = BeautifulSoup(pageRequest.text); 
# Call function which extracts paintings from the page 
	getPagePaintings(localSoup,painting,artistCounter,paintingCounter);

print "Number of records found : "+str(len(painting))
	
# convert the entire string to json 
jsondata = jsonpickle.encode(painting, unpicklable=False)
print "Writing data to file"
open('Homework1Data.txt','w').close()
file = open('Homework1Data.txt','w')
file.write(jsondata);
print "Done"



