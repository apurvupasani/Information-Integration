from bs4 import BeautifulSoup
import requests;

r  = requests.get("http://www.guggenheim.org/new-york/collections/collection-online/artwork/300");
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

birthYear="";
birthCity="";
birthCountry="";
birthArr = firstDataPart.split(",")
if len(birthArr) == 3:
	birthYear = birthArr[0].strip().split(" ")[1]
	birthCity = birthArr[1].strip()
	birthCountry = 	birthArr[2].strip()
else:
	birthYear = birthArr[0].strip().split(" ")[1]
	birthCity = birthArr[1].strip()

print birthYear +" " +birthCity+" "+birthCountry;

if lastDataPart != "":
	deathYear="";
	deathCity="";
	deathCountry="";
	deathArr = lastDataPart.split(",")

	if len(deathArr) == 3:
		deathYear = deathArr[0].strip().split(" ")[1]
		deathCity = deathArr[1].strip()
		deathCountry = 	deathArr[2].strip()
	else:
		deathYear = deathArr[0].strip().split(" ")[1]
		deathCity = deathArr[1].strip()

	print deathYear +" " +deathCity+" "+deathCountry;

contentSoup= BeautifulSoup(requests.get("http://www.guggenheim.org"+divData.a.get("href")).text);
bioContent = contentSoup.find("div",{"id":"bio"});
paragraphs = bioContent.findAll("p")
paras = ""

for i in range(1,len(paragraphs)-1):
	paras+=paragraphs[i].text+"\n"
#print paras

### Here we capture the preview text of the image

divPreviewImage = soup.find("div",{"id":"collections-preview-image"});
previewText = divPreviewImage.text.strip()
previewText = previewText.split(".")
materialAndSize = previewText[1].strip().split(",")
size = ""
size2=""
if len(materialAndSize) > 2:
	size = materialAndSize[0:len(materialAndSize)-2]
	for e in size:
		size2+=e+", "
else:
	size = materialAndSize[0]
size








