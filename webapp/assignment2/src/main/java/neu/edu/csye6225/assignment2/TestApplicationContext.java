package neu.edu.csye6225.assignment2;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.StandardEnvironment;

@SpringBootApplication
public class TestApplicationContext {
    public static void main(String[] args){
        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        builder.environment(new StandardEnvironment());
        builder.sources(TestApplicationContext.class);
        builder.main(TestApplicationContext.class);
        builder.run(args);
    }
}
