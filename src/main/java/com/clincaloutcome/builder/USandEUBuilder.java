package com.clincaloutcome.builder;

import com.clincaloutcome.model.EUClinical;
import com.clincaloutcome.model.USClinical;
import com.poiji.bind.Poiji;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Component
public class USandEUBuilder {

    public List<EUClinical> extractMatchesFromBothLists(String usFile, String euFile) {
        // Take list of US Clinical CSV file
        List<USClinical> usClinicalList = readExcelFile(usFile, USClinical.class);

        List<USClinical> usListWithoutDuplicates = getListWithoutDuplicates(usClinicalList);

        // Take list of EU Clinical Excel file
        List<EUClinical> euClinicalList = readExcelFile(euFile, EUClinical.class);

        List<EUClinical> distinctEUList1 = euClinicalList.stream()
                .collect(collectingAndThen(toCollection(() ->
                                new TreeSet<>(
                                        Comparator.comparing(EUClinical::getSponsorProtocolNumber,
                                                Comparator.nullsFirst(Comparator.naturalOrder())))),
                        ArrayList::new));

        return removeUSProtocolsFromEUList(distinctEUList1, usListWithoutDuplicates);
    }

    private <T> List<T> readExcelFile(String file, Class<T> requestClass) {
        return Poiji.fromExcel(new File(file), requestClass);
    }

    private ArrayList<USClinical> getListWithoutDuplicates(List<USClinical> usClinicalList) {
        return usClinicalList.stream()
                .collect(collectingAndThen(toCollection(() ->
                                new TreeSet<>(
                                        Comparator.comparing(USClinical::getOtherId,
                                                Comparator.nullsFirst(Comparator.naturalOrder())))),
                        ArrayList::new));
    }

    private List<EUClinical> removeUSProtocolsFromEUList(List<EUClinical> euList, List<USClinical> usList) {
        List<EUClinical> newEuList = new ArrayList<>(euList);

        for (EUClinical euClinical : euList) {
            for (USClinical otherId : usList) {
                if (otherId.getOtherId().contains("|")) {
                    String[] words = otherId.getOtherId().split("\\|");
                    List<String> wordArrayList = new ArrayList<>(Arrays.asList(words));
                    for (String word : wordArrayList) {
                        if (word.equalsIgnoreCase(euClinical.getSponsorProtocolNumber())) {
                            newEuList.remove(euClinical);
                        }
                    }
                } else if (otherId.getOtherId().equalsIgnoreCase(euClinical.getSponsorProtocolNumber())) {
                    newEuList.remove(euClinical);
                }
            }
        }
        return newEuList;
    }
}
