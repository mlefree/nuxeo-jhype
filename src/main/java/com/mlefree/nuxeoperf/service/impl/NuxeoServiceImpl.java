package com.mlefree.nuxeoperf.service.impl;


import com.mlefree.nuxeoperf.service.NuxeoService;
import org.nuxeo.client.NuxeoClient;
import org.nuxeo.client.Operations;
import org.nuxeo.client.objects.Documents;
import org.nuxeo.client.objects.blob.Blob;
import org.nuxeo.client.objects.blob.FileBlob;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.awt.image.BufferedImage;


// todo https://spring.io/guides/gs/batch-processing/ ?
@Service
@Transactional
public class NuxeoServiceImpl implements NuxeoService {


    private String url = "http://localhost:8110/nuxeo";
    private NuxeoClient nuxeoClient;


    public NuxeoServiceImpl() {

        this.nuxeoClient = new NuxeoClient.Builder()
            .url(this.url)
            .authentication("Administrator", "Administrator")
            .connect();

        // To fetch all schemas
        //this.nuxeoClient =
        this.nuxeoClient.schemas("*");

    }

    @PreDestroy
    public void destroy() {

        // To shutdown  the client
        if (this.nuxeoClient != null) {
            this.nuxeoClient.disconnect();
        }

    }


    @Override
    public void searchSmall() {
        //this.url = url;

        Documents docs = this.nuxeoClient.operation("Repository.Query")
            .param("query", "SELECT * FROM Document")
            .execute();


        int count = docs.getResultsCount();
        System.out.println("#NUXEO count: " + count);
    }


    @Override
    public void importSmall() {

        for (int x = 2; x <= 4; x++) {

            Blob fileBlob = this.createRandom();

            nuxeoClient.operation(Operations.BLOB_ATTACH_ON_DOCUMENT)
                .voidOperation(true)
                .param("document", "/test/file" + x)
                .input(fileBlob)
                .execute();

            // int count = docs.getResultsCount();
            System.out.println("#NUXEO import done: " + x);
        }
    }


    private Blob createRandom() {

        File f = new File("MyFile.png");

        try {
            BufferedImage img = new BufferedImage(
                500, 500, BufferedImage.TYPE_INT_RGB);

            int r = 5;
            int g = 25;
            int b = 255;
            int col = (r << 16) | (g << 8) | b;
            for (int x = 0; x < 500; x++) {
                for (int y = 20; y < 300; y++) {
                    img.setRGB(x, y, col);
                }
            }
            ImageIO.write(img, "JPG", f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Blob streamBlob = new StreamBlob(InputStream stream, "MyFile.png");
        //File file = new File("MyFile.png");
        Blob fileBlob = new FileBlob(f);
        return fileBlob;
    }

    /*

    public void search() {

        // Execute query
        Documents docs = this.nuxeoClient.operation("Repository.Query")
                .param("query", "SELECT * FROM Document")
                .execute();
    }


    public void upload() {

// To upload|download blob(s)

        File file = new File("/path???");

        Blob fileBlob = new FileBlob(file);

        nuxeoClient.operation(Operations.BLOB_ATTACH_ON_DOCUMENT)
                .voidOperation(true)
                .param("document", "/folder/file")
                .input(fileBlob)
                .execute();

// or with stream
        Blob streamBlob = new StreamBlob(InputStream stream, String filename);
        nuxeoClient.operation(Operations.BLOB_ATTACH_ON_DOCUMENT)
                .voidOperation(true)
                .param("document", "/folder/file")
                .input(streamBlob)
                .execute();

        Blobs inputBlobs = new Blobs();
        inputBlobs.add(File file1);
        inputBlobs.add(new StreamBlob(InputStream stream, String filename2));
        Blobs blobs = nuxeoClient.operation(Operations.BLOB_ATTACH_ON_DOCUMENT)
                .voidOperation(true)
                .param("xpath", "files:files")
                .param("document", "/folder/file")
                .input(inputBlobs)
                .execute();

// you need to close the stream or to get the file
        Blob blob = nuxeoClient.operation(Operations.DOCUMENT_GET_BLOB)
                .input("folder/file")
                .execute();
    }

    public void batchUpload() {
        // Batch Upload Manager
        BatchUploadManager batchUploadManager = this.nuxeoClient.batchUploadManager();
        BatchUpload batchUpload = batchUploadManager.createBatch();
// Upload File
        File file = FileUtils.getResourceFileFromContext("sample.jpg");
        batchUpload = batchUpload.upload("1", file);

// Fetch/Refresh the batch file information from server
        batchUpload = batchUpload.fetchBatchUpload("1");

// Directly from the manager
        batchUpload = batchUpload.fetchBatchUpload(batchUpload.getBatchId(), "1");

// Upload another file and check files
        file = FileUtils.getResourceFileFromContext("blob.json");
        batchUpload.upload("2", file);
        List<BatchUpload> batchFiles = batchUpload.fetchBatchUploads();


        Document doc = new Document("file", "File");
        doc.set("dc:title", "new title");
        doc = nuxeoClient.repository().createDocumentByPath("/folder_1", doc);
        doc.set("file:content", batchUpload.getBatchBlob());
        doc = doc.updateDocument();

    }
*/

}
