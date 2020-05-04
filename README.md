# eu-clinical-web-scraper
Scrapes all the info from a search on https://www.clinicaltrialsregister.eu

Ex.) https://www.clinicaltrialsregister.eu/ctr-search/search?query=amyotrophic+lateral+sclerosis&age=adult&phase=phase-two&phase=phase-three&phase=phase-four&dateFrom=2010-01-01&dateTo=2021-01-01

copy the url into the UI and select the number of pages you want to go through.

Here is the UI to search for trials and convert them to an excel sheet.
https://eu-clinical-trial-converter.herokuapp.com/




In txt file you can put multiple searches... Paste the url (without the page number) and then the page number next to it..

Example.) https://www.clinicaltrialsregister.eu/ctr-search/search?query=amyotrophic+lateral+sclerosis&age=adult&phase=phase-two&phase=phase-three&phase=phase-four&dateFrom=2010-01-01&dateTo=2021-01-01 3

You see the url and at the end is the page number...


In the project, UsChildren and NewEUListChildren are there for examples... They will create a merged cross referenced list.
