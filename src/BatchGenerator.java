package com.santander.nuxeo.importer;

import com.santander.nuxeo.Vault;
import com.santander.nuxeo.constants.Constants;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

public class BatchGenerator {

    public static void main(String[] args) throws Exception {

        Integer val = 5;
        BatchGenerator.generateBatch("/Users/juliansabos/Projects/Santander/minio_docker/data/bpevault01encr", Vault.VOC_ENTITY_RETAIL,
                "QUADIENT", val.toString(), Vault.SAN_UK_RETAIL_CUSTOMER_PATH, val, 50, 50);
        System.out.println("OK");
    }

    // TODO with().width().generatePattern();
    public static void generateBatch(String destinationFolder, String entity, String application, String id,
                                     String destination, int count, int nullExpiryDateRate, int nullValidityDateRate) throws Exception {

        RandomStringGenerator numberGenerator = new RandomStringGenerator.Builder().withinRange('0', '9').build();
        RandomStringGenerator stringGenerator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        final List<String> ENTITIES = Arrays.asList(Vault.VOC_ENTITY_RETAIL, Vault.VOC_ENTITY_CORP,
                Vault.VOC_ENTITY_CA);
        final List<String> COMMUNICATION_TYPES = Arrays.asList(Vault.VOC_COMMUNICATION_TYPE_EDOC,
                Vault.VOC_COMMUNICATION_TYPE_PAPER, Vault.VOC_COMMUNICATION_TYPE_SMS,
                Vault.VOC_COMMUNICATION_TYPE_EMAIL, Vault.VOC_COMMUNICATION_TYPE_PUSH);
        final List<String> CUSTOMER_TYPES = Arrays.asList("F", "J");

        String folderName = "batchid"+ id;
        File folder = new File(destinationFolder, folderName);
        folder.mkdir();

        for (int i = 0; i < count; i++) {

//85632aa2-8910-4243-984e-43e4b313cdb0_XFR.UAT.GA01.DSX.PDF.MRG02001.D1911.R
            //String pdfFileName = "document-" + i + ".pdf";
            String fileName = "85632aa2-8910-4243-984e-43e4b313cdb0_XFR.UAT.GA01.DSX.PDF.MRG02001.D" + i + ".R";

            PDDocument document = new PDDocument();
            PDPage blankPage = new PDPage();
            PDPageContentStream contentStream = new PDPageContentStream(document, blankPage);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
            contentStream.moveTextPositionByAmount(250, 665);
            contentStream.drawString("RTL_PRN_20180301090959_" + i + "_NA_MTSO00024_Count_L00");
            contentStream.endText();
            contentStream.close();
            document.addPage(blankPage);
            document.save(new File(folder, fileName + ".pdf"));
            document.close();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document dom = db.newDocument();

                Element docEle = dom.createElement("document");

                // EXT
                //addProperty(dom, docEle, "fileName", pdfFileName);
                //addProperty(dom, docEle, "externalId", "legacy-" + numberGenerator.generate(10));
                addProperty(dom, docEle, "title", "RTL_PRN_20180301090959_" + i + "_NA_MTSO00024_Count_L00");
                //addProperty(dom, docEle, "externalType", "legacy-type" + numberGenerator.generate(1));
                //addProperty(dom, docEle, "externalCreationDate", generateDate(-(int) (Math.random() * 1000), 0));
                // DOC
                //addProperty(dom, docEle, "expiryDate", generateDate((int) (Math.random() * 1000), nullExpiryDateRate));
                //addProperty(dom, docEle, "accountNumber", numberGenerator.generate(10));
                //addProperty(dom, docEle, "applicationGroup", stringGenerator.generate(10));
                //addProperty(dom, docEle, "applicationId", stringGenerator.generate(10));
                //addProperty(dom, docEle, "applicationName", stringGenerator.generate(10));
                //addProperty(dom, docEle, "brand", stringGenerator.generate(10));
                //addProperty(dom, docEle, "caseRef", stringGenerator.generate(10));
                //addProperty(dom, docEle, "channelIndicator", stringGenerator.generate(10));
                //addProperty(dom, docEle, "communicationType",
                //       COMMUNICATION_TYPES.get((int) (Math.random() * COMMUNICATION_TYPES.size())));
                //addProperty(dom, docEle, "companyCode", stringGenerator.generate(10));
                //addProperty(dom, docEle, "complaintId", stringGenerator.generate(10));
                // CUSTOMERS
                /*Element customersElements = dom.createElement("customers");
                int customerCount = (int) (Math.random() * 3);
                for (int c = 0; c < customerCount; c++) {
                    Element customerElement = dom.createElement("customer");
                    addProperty(dom, customerElement, "number", "C" + numberGenerator.generate(10));
                    addProperty(dom, customerElement, "type",
                            CUSTOMER_TYPES.get((int) (Math.random() * CUSTOMER_TYPES.size())));
                    addProperty(dom, customerElement, "readDate", ""); // TODO
                    customersElements.appendChild(customerElement);
                }
                docEle.appendChild(customersElements);*/
                // ..
                /*
                addProperty(dom, docEle, "entity", ENTITIES.get((int) (Math.random() * ENTITIES.size())));
                addProperty(dom, docEle, "internalAccountNumber", stringGenerator.generate(10));
                addProperty(dom, docEle, "item", "{}"); // TODO JSON
                addProperty(dom, docEle, "paymentRef", stringGenerator.generate(10));
                addProperty(dom, docEle, "priority", stringGenerator.generate(10));
                addProperty(dom, docEle, "productSubType", stringGenerator.generate(10));
                addProperty(dom, docEle, "productType", stringGenerator.generate(10));
                addProperty(dom, docEle, "recipient", stringGenerator.generate(10));
                addProperty(dom, docEle, "sortCode", stringGenerator.generate(10));
                addProperty(dom, docEle, "standardReference", stringGenerator.generate(10));
                addProperty(dom, docEle, "subApplication", stringGenerator.generate(10));
                addProperty(dom, docEle, "uniqueSysRef", stringGenerator.generate(10));
                addProperty(dom, docEle, "validityDate",
                        generateDate((int) (Math.random() * 1000), nullValidityDateRate));
*/
                dom.appendChild(docEle);

                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");


                tr.transform(new DOMSource(dom),
                        new StreamResult(new FileOutputStream(new File(folder, fileName + ".xml"))));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void addProperty(Document dom, Element element, String propertyName, String propertyValue) {
        Element newElement = dom.createElement(propertyName);
        newElement.appendChild(dom.createTextNode(propertyValue));
        element.appendChild(newElement);
    }

    private static String generateDate(int nbDayToAdd, int nullRate) {
        if (nullRate > 0) {
            int random = (int) (Math.random() * 100);
            if (nullRate >= random) {
                return "";
            }
        }
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE, nbDayToAdd);
        return Constants.DATE_FORMAT.format(date.getTime());
    }
}
