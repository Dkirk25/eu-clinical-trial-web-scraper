//package com.clincaloutcome.client;
//
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class ParseProtocolTest {
//
//    @Mock
//    private SSLHelper sslHelper;
//
//    @Mock
//    private Document document;
//
//    @Mock
//    private Elements mockElements;
//
//    @Mock
//    private Jsoup jsoup;
//
//    @Mock
//    private Connection connection;
//
//    @InjectMocks
//    private ParseProtocol parseProtocol;
//
//    Elements elements = new Elements();
//
//    List<Element> elementList = new ArrayList<>();
//
//    private String eudraCT = "2010-021179-10";
//    private Map<String, List<String>> map = new HashMap<>();
//    private final Element number = new Element("<td colspan=\"3\"><span class=\"label\">Trial protocol:</span> <a href=\"/ctr-search/trial/2017-004459-21/IT\">IT</a> <span class=\"status\">(Ongoing)</span> </td>");
//    private String firstProtocol = "DE";
//    private String protocol = "DE (Prematurely Ended) BE (Prematurely Ended) GB (Prematurely Ended) ES (Prematurely Ended)";
//    private List<String> elementTextList;
//
//
//    @Before
//    public void setUp() {
//        number.text("Trial protocol: DE (Prematurely Ended) BE (Prematurely Ended) GB (Prematurely Ended) ES (Prematurely Ended)");
//        map.put("trialProtocol", new ArrayList<>());
//        map.put("primaryEndpoints", new ArrayList<>());
//        map.put("secondaryEndPoints", new ArrayList<>());
//        map.put("trialResults", new ArrayList<>());
//        map.get("trialProtocol").add(number.text());
//
//        List<String> testElements = Arrays.asList("\"<tr> \\n <td colspan=\"3\" class=\"cellBlue\">E.5 End points</td> \n</tr>",
//                "\"<tr> \\n <td colspan=\"3\" class=\"first\">E.5.1</td>\n <td class=\"second\">Primary end point(s)</td>\n <td class=\"third\"> \n  <table border=\"1\" width=\"100%\"> \n   <tbody>\n    <tr> \n     <td width=\"90%\">The primary outcome measure will be the safety assessment. The safety criteria will be:<br>• Occurrence of AEs,<br>• Physical examination,<br>• Laboratory tests,<br>• Vital signs and ECG,</td> \n    </tr> \n   </tbody>\n  </table> </td> \n</tr>",
//                "\"<tr> \\n <td colspan=\"3\" class=\"cellBlue\">E.6 and E.7 Scope of the trial</td> \n</tr>",
//                "\"<tr> \\n <td colspan=\"3\" class=\"cellBlue\">E.6</td>\n <td colspan=\"2\">Scope of the trial</td> \n</tr>");
//
//        elementTextList = Arrays.asList("E.5 End points", "E.5.1 Primary end point(s) The primary outcome measure will be the safety assessment. The safety criteria will be: • Occurrence of AEs, • Physical examination, • Laboratory tests, • Vital signs and ECG,", "E.6 and E.7 Scope of the trial", "E.6 Scope of the trial");
//
//        for (int i = 0; i < testElements.size(); i++) {
//            Element element1 = new Element(testElements.get(i));
//            element1.text(elementTextList.get(i));
//            elementList.add(element1);
//        }
//        elements.addAll(elementList);
//    }
//
//    @Test
//    public void handleTrialProtocol_containsPrimaryProtocol() throws IOException {
//        when(sslHelper.getConnection(anyString())).thenReturn(connection);
//        when(connection.get()).thenReturn(document);
//        when(document.select("#section-e > tbody > tr")).thenReturn(elements);
//
//    //    parseProtocol.handleTrialProtocol(eudraCT, number, map);
//        Assert.assertTrue(map.get("primaryEndpoints").get(0).contains("The primary outcome measure will be the safety assessment. The safety criteria will be:"));
//    }
//
//    @Test
//    public void handleTrialProtocol_containsSecondaryProtocol() throws IOException {
//        when(sslHelper.getConnection("https://www.clinicaltrialsregister.eu/ctr-search/trial/" + eudraCT + "/" + firstProtocol)).thenReturn(connection);
//
//        Element element1 = new Element("blah");
//        element1.text("E.5.2 Secondary end point(s) The primary outcome measure will be the safety assessment. The safety criteria will be: • Occurrence of AEs, • Physical examination, • Laboratory tests, ,• Vital signs and ECG");
//        elementList.add(element1);
//        elements.add(element1);
//
//        when(connection.get()).thenReturn(document);
//        when(document.select("#section-e > tbody > tr")).thenReturn(elements);
//
//    //    parseProtocol.handleTrialProtocol(eudraCT, number, map);
//        Assert.assertTrue(map.get("secondaryEndPoints").get(0).contains("The primary outcome measure will be the safety assessment. The safety criteria will be:"));
//    }
//}
