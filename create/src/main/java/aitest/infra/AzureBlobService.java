package aitest.infra;

import com.azure.storage.blob.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Service
public class AzureBlobService {

    @Value("${azure.storage.connection-string}")
    private String connectionString;
    private final String containerName = "aiimagepulic"; // 컨테이너명
    private final String containerName2 = "aireplicate"; // 컨테이너명
    
    public String uploadImageFromUrl(String imageUrl) throws Exception {
        // 이미지 URL에서 InputStream 다운로드 (BufferedInputStream으로 감싸기)
        URL url = new URL(imageUrl);
        InputStream inputStream = new BufferedInputStream(url.openStream());

        // Blob Service Client 생성
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        // 컨테이너 클라이언트 생성
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        // Blob 이름 설정
        String blobName = UUID.randomUUID().toString() + ".png";
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        // 업로드 (overwrite = true)
        blobClient.upload(inputStream, url.openConnection().getContentLength(), true);

        // 업로드된 Blob의 URL 반환
        return blobClient.getBlobUrl();
    }
    
    public String uploadImageReplicateUrl(String imageUrl) throws Exception {
        // 이미지 URL에서 InputStream 다운로드 (BufferedInputStream으로 감싸기)
        URL url = new URL(imageUrl);
        InputStream inputStream = new BufferedInputStream(url.openStream());

        // Blob Service Client 생성
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        // 컨테이너 클라이언트 생성
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName2);

        // Blob 이름 설정
        String blobName = UUID.randomUUID().toString() + ".png";
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        // 업로드 (overwrite = true)
        blobClient.upload(inputStream, url.openConnection().getContentLength(), true);

        // 업로드된 Blob의 URL 반환
        return blobClient.getBlobUrl();
    }
}
