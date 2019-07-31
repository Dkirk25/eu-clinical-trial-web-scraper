package com.clincaloutcome;



import com.clincaloutcome.builder.WebBuilder;

import java.util.Scanner;

public class Runner {

    public static void main(String[] args) {
        WebBuilder webBuilder = new WebBuilder();

        Scanner sc = new Scanner(System.in);

        System.out.println("Trying to cross reference two files? (y)es or (n)o:");
        String crossRef = sc.nextLine();

        if (crossRef.equalsIgnoreCase("y")) {
            executeCrossReference(sc, webBuilder);
        } else {
            System.out.println("Upload a txt File? (y)es or (n)o: ");
            String f = sc.nextLine();

            if (f.equalsIgnoreCase("y")) {
                executeBulkSearch(sc, webBuilder);
            } else {
                System.out.println("Use Single Search? (y)es or (n)o: ");
                String single = sc.nextLine();

                if (single.equalsIgnoreCase("y")) {
                    executeSingleSearch(sc, webBuilder);
                }
            }
        }
    }

    private static void executeBulkSearch(Scanner sc, WebBuilder webBuilder) {
        System.out.println("What is the file name: ");
        String bulkFile = sc.nextLine();
        // Process Bulk File.
        webBuilder.bulkBuilder(bulkFile);
    }


    private static void executeSingleSearch(Scanner sc, WebBuilder webBuilder) {
        System.out.println("Enter Url: ");
        String url = sc.nextLine();
        System.out.println("How Many Pages: ");
        String pages = sc.nextLine();
        // Process Single Request
        webBuilder.singleBuilder(url, pages);
    }

    private static void executeCrossReference(Scanner sc, WebBuilder webBuilder) {
        System.out.println("Enter US Clinical Trial:");
        String usTrial = sc.nextLine();
        System.out.println("Enter EU Clinical Trial:");
        String euTrial = sc.nextLine();
        // Process Cross Reference
        webBuilder.crossBuilder(usTrial, euTrial);
    }
}
