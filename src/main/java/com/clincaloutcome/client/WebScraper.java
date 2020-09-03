package com.clincaloutcome.client;

import com.clincaloutcome.builder.Utils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class WebScraper {

    @Autowired
    private ParseProtocol parseProtocol;

    public List<String> iterateRowInTable(Element element) {
        List<String> listMap = new ArrayList<>();
        Elements rowElements = element.select("tr");

        if (rowElements.size() == 8) {
            for (int i = 0; i < rowElements.size(); i++) {
                if (i == 4) {
                    listMap.add("N/A");
                } else {
                    parseRowElement(element, listMap, rowElements, i);
                }
            }
        } else {
            for (int i = 0; i < rowElements.size(); i++) {
                if (i != 4 && i != 5) {
                    parseRowElement(element, listMap, rowElements, i);
                }
            }
        }

        return formatResults(listMap);
    }

    private void parseRowElement(Element element, List<String> listMap, Elements rowElements, int i) {
        if (!rowElements.get(i).select("td").isEmpty()) {
            iterateThroughTd(rowElements.get(i).select("td"), listMap);
        } else {
            if (element.text().contains(":")) {
                String[] value = element.text().split(":");
                listMap.add(value[1]);
            } else {
                listMap.add(element.text());
            }
        }
    }

    private List<String> formatResults(List<String> listMap) {
        List<String> newList = new ArrayList<>();
        int count = 0;
        StringBuilder combinedDiseaseDetail = new StringBuilder();
        List<String> listOfDiseases = new ArrayList<>();

        for (int i = 0; i < listMap.size(); i++) {
            String value = StringUtils.trim(listMap.get(i));
            if (i > 5 && i < listMap.size() - 4) {
                if (count < 5) {
                    List<String> listOfDiseaseTitles = Arrays.asList("Version", "SOC Term", "Classification Code", "Term", "Level");

                    combinedDiseaseDetail.append(listOfDiseaseTitles.get(count)).append(": ").append(Utils.nullCheck(value)).append(", ");
                    count++;

                    if (count == 5) {
                        listOfDiseases.add(combinedDiseaseDetail.toString());
                        combinedDiseaseDetail = new StringBuilder();
                        count = 0;
                    }
                }
            } else {
                if (i == listMap.size() - 4) {
                    StringBuilder disease = new StringBuilder();
                    for (String diseaseDetail : listOfDiseases) {
                        disease.append(diseaseDetail).append("\n");
                    }
                    newList.add(disease.toString());
                }
                newList.add(value);
            }
        }

        parseProtocol.handleTrialProtocol(newList.get(newList.size() - 2), newList);

        return newList;
    }

    protected void iterateThroughTd(Elements td, List<String> listMap) {
        for (Element element : td) {
            String elementValue = element.text();

            if (elementValue.contains(":")) {
                String[] value = elementValue.split(":");
                if (value.length > 1) {
                    listMap.add(value[1]);
                } else {
                    listMap.add(value[0]);
                }
            } else {
                listMap.add(elementValue);
            }
        }
    }
}
