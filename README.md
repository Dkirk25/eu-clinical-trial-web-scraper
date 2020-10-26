# Eu-Clinical-Trial-Web-Scraper

## About
* ***All excel instances and files must be closed out for this to work!***
* Scrapes all the info from a search on https://www.clinicaltrialsregister.eu
* [GO HERE](http://eu-clinical.donkirk.net/)  

## Single Search
Copy the link from the search results from the clinical trials register page. 
Provide the number of pages you want scraped. If the search returns a max of 3 pages, then you can choose between 1-3 pages.
[Example](https://www.clinicaltrialsregister.eu/ctr-search/search?query=amyotrophic+lateral+sclerosis&age=adult&phase=phase-two&phase=phase-three&phase=phase-four&dateFrom=2010-01-01&dateTo=2021-01-01)  

![Single Search](https://github.com/Dkirk25/eu-clinical-trial-web-scraper/blob/master/pictures/single_search.JPG)

--------------------------------------------------------------------------------
## Bulk Searches
Similar to the Single search, the Bulk Search option allows the user to search more than one request in one go.
You need a text file(.txt) with the same parameters as the single search.  

Each line has the search url and page number  
In the example below you see the search url and the page number "1" because thats how many pages I want to scrape per request.

![Multiple Search](https://github.com/Dkirk25/eu-clinical-trial-web-scraper/blob/master/pictures/multiple_search.JPG "Multiple Search Example")

--------------------------------------------------------------------------------
## Cross Search
* Two excel files are required
    * US Excel File
    * EU Excel File
* Removes any records from the EU excel file that matches the US excel file based on:
  * Compares on US *OtherId* and EU *SponsorProtocolNumber* **EXACTLY**
  
Below these headers are **required!**

#### EU Excel Headers
* EudraCT Number
* Sponsor Protocol Number
* Start Date
* Sponsor Name
* Full Title
* Medical Condition
* Disease
* Population Age
* Gender
* Trial Protocol
* Trial Results
* Primary End Points
* Secondary End Points

#### US Excel Headers
* rank 
* ntcNumber 
* title 
* acronym 
* status 
* studyResults 
* condition 
* intervention 
* outcomeMeasure 
* sponsor 
* gender 
* age 
* phases 
* enrollment
* fundedBy
* studyType
* studyDesign
* otherId
* startDate
* primaryCompletionDate
* completionDate
* firstPosted
* resultFirstPosted
* lastUpdatePosted
* locations
* studyDocuments
* url
