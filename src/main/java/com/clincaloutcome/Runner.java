package com.clincaloutcome;


import com.clincaloutcome.builder.Utils;
import com.clincaloutcome.builder.WebBuilder;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Runner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);
    private static void executeSingleSearch(Scanner sc, WebBuilder webBuilder) {
        System.out.println("Enter Url: ");
        String url = sc.nextLine();
        System.out.println("How Many Pages: ");
        String pages = sc.nextLine();
        // Process Single Request
        webBuilder.singleBuilder(url, pages);
    }

//    private static void executeBulkSearch(Scanner sc, WebBuilder webBuilder) {
//        System.out.println("What is the file name: ");
//        String bulkFile = sc.nextLine();
//        Utils utils = new Utils();
//        // Process Bulk File.
//
//        if (utils.isValidFileFormat(bulkFile, ".txt")) {
//            webBuilder.bulkBuilder(bulkFile);
//        } else {
//            LOGGER.error("File type must end with .txt!");
//        }
//    }

    private static void executeCrossReference(Scanner sc, WebBuilder webBuilder) {
        System.out.println("Enter US Clinical Trial:");
        String usTrial = "UsChildren.xlsx";
        System.out.println("Enter EU Clinical Trial:");
        String euTrial = "NewEUListChildren.xlsx";
        Utils utils = new Utils();
        // Process Cross Reference
        if (utils.isValidFileFormat(usTrial, ".xlsx") && utils.isValidFileFormat(euTrial, ".xlsx")) {
            webBuilder.crossBuilder(usTrial, euTrial);
        } else {
            LOGGER.error("One or more files must end with .xlsx!");
        }
    }
}
