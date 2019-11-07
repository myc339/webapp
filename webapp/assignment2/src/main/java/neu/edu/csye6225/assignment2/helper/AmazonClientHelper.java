package neu.edu.csye6225.assignment2.helper;

import com.amazonaws.auth.*;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
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
    @Value("${accesskey}")
    private String accesskey;

    @Value("${secretkey}")
    private String secretkey;

    @Value("${region}")
    private String region;

    @Value("${bucketName}")
    private String bucketName;

    @Bean(name="amazonS3")
    public AmazonS3 getAWSCredentials() {
//        System.out.println("credentialsProvider:"+credentialsProvider.getCredentials().getAWSAccessKeyId());

        System.out.println("accessKey:" + accesskey);
        if (!accesskey.isEmpty()) {
            System.out.println("load credentials from properties");
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.accesskey, this.secretkey);
            return AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(getAWSRegion().getName()).build();

        } else {
            System.out.println("load credentials from ec2 instance profile?");
//            AmazonIdentityManagement client = AmazonIdentityManagementClientBuilder.standard().build();
//            GetInstanceProfileRequest request = new GetInstanceProfileRequest().withInstanceProfileName("profile");
//            GetInstanceProfileResult response = client.getInstanceProfile(request);
//            System.out.println(response.toString());
//            AWSCredentialsProviderChain providerChain = new AWSCredentialsProviderChain(
//                    InstanceProfileCredentialsProvider.getInstance()
//            );
//            new ProfileCredentialsProvider()
//            ,new SystemPropertiesCredentialsProvider(),
//                    new EnvironmentVariableCredentialsProvider(),
//           return AmazonS3ClientBuilder.standard()
//                   .withCredentials( InstanceProfileCredentialsProvider.getInstance())
//                   .build();
//            new ProfileCredentialsProvider();
            InstanceProfileCredentialsProvider credentialsProvider= new InstanceProfileCredentialsProvider(false);
            System.out.println("ec2 accessKeyID:"+credentialsProvider.getCredentials().getAWSAccessKeyId());
            System.out.println("ec2 secretKeyID:"+credentialsProvider.getCredentials().getAWSSecretKey());


//            return AmazonS3ClientBuilder.standard()
//                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//                    .withRegion(getAWSRegion().getName()).build();
            return AmazonS3ClientBuilder.standard().withCredentials(credentialsProvider).build();
//            s3.setRegion(getAWSRegion());
//            return s3;

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
