package neu.edu.csye6225.assignment2.helper;

import com.amazonaws.auth.*;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class AmazonClientHelper {
    private AmazonS3 s3client;
    @Value("${accessKey}")
    private String accessKey;

    @Value("${secretKey}")
    private String secretKey;

    @Value("${region}")
    private String region;

    @Value("${bucketName}")
    private String bucketName;

    @Bean(name="awsCredentialsProvider")
    public AWSCredentialsProvider getAWSCredentials() {
        InstanceProfileCredentialsProvider credentialsProvider=new InstanceProfileCredentialsProvider(true);
        System.out.println("credentialsProvider:"+credentialsProvider.getCredentials().getAWSAccessKeyId());
        System.out.println("credentialsProvider:"+credentialsProvider.getCredentials().getAWSAccessKeyId());
        System.out.println("credentialsProvider:"+credentialsProvider.getCredentials().getAWSAccessKeyId());
        System.out.println("credentialsProvider:"+credentialsProvider.getCredentials().getAWSAccessKeyId());


        if(credentialsProvider.getCredentials().getAWSAccessKeyId()==null)
        {
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
            return new AWSStaticCredentialsProvider(awsCredentials);
        }
        else{
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(credentialsProvider.getCredentials().getAWSAccessKeyId(),
                    credentialsProvider.getCredentials().getAWSSecretKey());
            return new AWSStaticCredentialsProvider(awsCredentials);
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
