package goorm_runner.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

public class EnvironmentVariableTest {

    @Value("${DB_USERNAME}")
    private String dbUsername;

    @Value("${AWS_ACCESS_KEY}")
    private String awsAccessKeyId;

    @Test
    void printEnvironmentVariables() {
        System.out.println("DB Username: " + dbUsername);
        System.out.println("AWS Access Key: " + awsAccessKeyId);
    }
}
