package com.bread.traveler.utils;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@Slf4j
public class AliyunOssUtils {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;
    @Value("${aliyun.oss.region}")
    private String region;

    /**
     * 上传文件
     * @param file
     * @return 文件访问url地址
     * @throws ClientException
     */
    public String uploadFile(MultipartFile file) throws ClientException {
        OSS ossClient = null;

        try {
            // 从环境变量中获取访问凭证
            EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

            // 创建OSSClient实例。
            // 当OSSClient实例不再使用时，调用shutdown方法以释放资源。
            ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
            clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
            ossClient = OSSClientBuilder.create()
                    .endpoint(endpoint)
                    .credentialsProvider(credentialsProvider)
                    .clientConfiguration(clientBuilderConfiguration)
                    .region(region)
                    .build();

            // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
            String originalFilename = file.getOriginalFilename();
            String objectPath = "traveler/logImg/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM")) + "/";
            String objectName = objectPath + UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));

            InputStream inputStream = file.getInputStream();
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            // 解析结果
            String[] split = endpoint.split("//");
            String url = split[0] + "//" + bucketName + "." + split[1] + "/" + objectName;
            log.info("文件上传成功, url: {}", url);
            return url;
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, " +
                    "but was rejected with an error response for some reason.");
            log.error("Error Message:{}", oe.getErrorMessage());
            log.error("Error Code:{}", oe.getErrorCode());
            log.error("Request ID:{}", oe.getRequestId());
            log.error("Host ID:{}", oe.getHostId());
            throw oe;
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message:{}", ce.getMessage());
            throw ce;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
