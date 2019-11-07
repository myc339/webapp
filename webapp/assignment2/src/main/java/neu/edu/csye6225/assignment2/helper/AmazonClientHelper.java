package neu.edu.csye6225.assignment2.helper;

import com.amazonaws.auth.*;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.amazonaws.services.identitymanagement.model.GetInstanceProfileRequest;
import com.amazonaws.services.identitymanagement.model.GetInstanceProfileResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class AmazonClientHelper {
    @Value("${accessKey}")
    private String accessKey;

    @Value("${secretKey}")
    private String secretKey;

    @Value("${region}")
    private String region;

    @Value("${bucketName}")
    private String bucketName;

    @Bean(name="amazonS3")
    public AmazonS3 getAWSCredentials() {
        InstanceProfileCredentialsProvider credentialsProvider = new InstanceProfileCredentialsProvider(true);

//        System.out.println("credentialsProvider:"+credentialsProvider.getCredentials().getAWSAccessKeyId());

        System.out.println("accessKey:" + accessKey);
        if (!accessKey.isEmpty()) {
            System.out.println("load credentials from properties");
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
            return AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(getAWSRegion().getName()).build();

        } else {
            System.out.println("load credentials from ec2 instance profile?");
            AmazonIdentityManagement client = AmazonIdentityManagementClientBuilder.standard().build();
            GetInstanceProfileRequest request = new GetInstanceProfileRequest().withInstanceProfileName("profile");
            GetInstanceProfileResult response = client.getInstanceProfile(request);
            System.out.println(response.toString());
            return AmazonS3ClientBuilder.defaultClient();
//            return AmazonS3ClientBuilder.standard().build();
//        }

        }
    }
    @Bean(name = "awsS3Bucket")
    public String getAWSS3Bucket() {
        return bucketName;
    }
    @Bean(name="awsRegion")
    public Region getAWSRegion()
    {
        return Region.getRegion(Regions.fromName(region));
    }
}
