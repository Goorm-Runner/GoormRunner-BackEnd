package goorm_runner.backend;

import org.junit.jupiter.api.Test;

public class EnvironmentVariableTest {
    @Test
    void printEnvironmentVariables() {
        System.out.println("DB Username: " + System.getenv("DB_USERNAME"));
        System.out.println("S3 Bucket: " + System.getenv("AWS_S3_BUCKET_NAME"));
    }
}
