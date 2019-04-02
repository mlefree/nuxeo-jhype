package com.mlefree.nuxeoperf.service.impl;

import com.mlefree.nuxeoperf.batch.model.Trade;
import com.mlefree.nuxeoperf.service.NuxeoService;
import org.nuxeo.client.NuxeoClient;
import org.nuxeo.client.Operations;
import org.nuxeo.client.objects.Document;
import org.nuxeo.client.objects.Documents;
import org.nuxeo.client.objects.blob.Blob;
import org.nuxeo.client.objects.blob.FileBlob;
import org.nuxeo.client.objects.user.Group;
import org.nuxeo.client.objects.user.User;
import org.nuxeo.client.objects.user.UserManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.ResponseBody;

import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@Service
//@Transactional
public class NuxeoServiceImpl implements NuxeoService {


    private NuxeoClient nuxeoClient;


    public NuxeoServiceImpl() {

        System.out.println("######### Nuxeo initialisation");

        System.out.println(System.getenv("NUXEO_URL"));
        System.out.println(System.getenv("NUXEO_LOGIN"));
        System.out.println(System.getenv("NUXEO_PASSWORD"));

        String url = System.getenv("NUXEO_URL");
        String login = System.getenv("NUXEO_LOGIN");
        String password = System.getenv("NUXEO_PASSWORD");
        if (url == null) {
            url = "http://nuxeoperf-nuxeo:8302/nuxeo";
            login = "Administrator";
            password = "Administrator";
        }
        System.out.println(url);
        System.out.println(login);
        System.out.println(password);

        this.nuxeoClient = new NuxeoClient.Builder()
            .url(url)
            .authentication(login, password)
            .connect();

        // To fetch all schemas
        //this.nuxeoClient =
        this.nuxeoClient.schemas("*");

    }

    @PreDestroy
    public void destroy() {

        System.out.println("######### Nuxeo disconnection");
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
        this.importSmall(null, true, false);
    }

    @Override
    public void importSmall(Trade trade, Boolean async, Boolean image) {

        final String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());

        int folderId = 0;
        String avs = "na";
        int loop = 1000;
        if (trade != null) {
            avs = trade.getAvs();
            loop = 20;
        }

        for (int x = 0; x < loop; x++) {

            String folderName = "folder-" + timeStamp + "-" + avs + "-" + folderId;
            String fileName = "file-" + timeStamp + "-" + avs + "-" + x;

            if ((x % 20) == 0) {

                // Create the folder
                folderId++;
                folderName = "folder-" + timeStamp + "-" + avs + "-" + folderId;
                Document documentFolder = Document.createWithName(folderName, "Folder");
                documentFolder.setPropertyValue("dc:title", folderName);
                nuxeoClient.repository().createDocumentByPath("/default-domain/workspaces/test-perf-java/", documentFolder);
            }


            try {

                final String _folderName = "" + folderName;
                final String _fileName = "" + fileName;
                Document document = Document.createWithName(_fileName, "File");
                document.setPropertyValue("dc:title", _fileName);

                if (async) {
                    nuxeoClient.repository()
                        .createDocumentByPath("/default-domain/workspaces/test-perf-java/" + _folderName, document, new Callback<Document>() {
                            //private final String _folderName = folderName;

                            @Override
                            public void onResponse(Call<Document> call, Response<Document> response) {

                                if (image) {
                                    Blob fileBlob = NuxeoServiceImpl.createRandom();
                                    nuxeoClient.operation(Operations.BLOB_ATTACH_ON_DOCUMENT)
                                        .voidOperation(true)
                                        .param("document", "/default-domain/workspaces/test-perf-java/" + _folderName + "/" + _fileName)
                                        .input(fileBlob)
                                        .execute(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                // perf: not interested in feedback
                                                System.out.println(" - ");
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                //System.out.println(t.getMessage());
                                                System.out.print("#");
                                            }
                                        });
                                }
                            }

                            @Override
                            public void onFailure(Call<Document> call, Throwable t) {
                                //System.out.println("Creation pb :" + t.getMessage());
                                System.out.print("@");
                            }
                        });
                } else {

                    nuxeoClient.repository()
                        .createDocumentByPath("/default-domain/workspaces/test-perf-java/" + _folderName, document);

                    if (image) {
                        Blob fileBlob = NuxeoServiceImpl.createRandom();
                        nuxeoClient.operation(Operations.BLOB_ATTACH_ON_DOCUMENT)
                            .voidOperation(true)
                            .param("document", "/default-domain/workspaces/test-perf-java/" + _folderName + "/" + _fileName)
                            .input(fileBlob)
                            .execute();
                    }

                    System.out.print(".");


                }

            } catch (Exception e) {
                System.out.println("#NUXEO pb: " + e);
            }
            // int count = docs.getResultsCount();
        }

    }

    @Override
    public void importBulkSmall() {


        try {

            String urlBulk = "http://localhost:8110/nuxeo/site/randomImporter/run?targetPath=/default-domain/workspaces/test-bulk/&nbNodes=500";
            okhttp3.Response response = this.nuxeoClient.get(urlBulk);
            //assertEquals(true, response.isSuccessful());
            String json = response.body().string();

            System.out.println("#NUXEO import json: " + json);

            /*
            URL urlBulkImport = new URL(urlBulk);
            HttpURLConnection connection = (HttpURLConnection) urlBulkImport.openConnection();
            connection.setRequestMethod("GET");
            connection.getInputStream();
            */
        } catch (Exception e) {
            System.out.println("#NUXEO import pb: " + e);
        }
        System.out.println("#NUXEO import done: ");

    }


    private static Blob createRandom() {

        File f = new File("MyFile.gitignored.png");

        try {
            BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < 100; x++) {
                for (int y = 0; y < 100; y++) {
                    int a = (int) (Math.random() * 256); //alpha
                    int r = (int) (Math.random() * 256); //red
                    int g = (int) (Math.random() * 256); //green
                    int b = (int) (Math.random() * 256); //blue
                    int p = (a << 24) | (r << 16) | (g << 8) | b;
                    img.setRGB(x, y, p);
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

    private void createUsers() {
        // Create User/Group

        UserManager userManager = nuxeoClient.userManager();
        User newUser = new User();
        newUser.setUserName("toto");
        newUser.setCompany("Nuxeo");
        newUser.setEmail("toto@nuxeo.com");
        newUser.setFirstName("to");
        newUser.setLastName("to");
        newUser.setPassword("totopwd");
        newUser.setTenantId("mytenantid");
        List<String> groups = new ArrayList<>();
        groups.add("members");
        newUser.setGroups(groups);
        User user = userManager.createUser(newUser);

        if (false) {
            //UserManager userManager = nuxeoClient.userManager();
            Group group = new Group();
            group.setGroupName("totogroup");
            group.setGroupLabel("Toto Group");
            List<String> users = new ArrayList<>();
            users.add("Administrator");
            group.setMemberUsers(users);
            group = userManager.createGroup(group);
        }
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
