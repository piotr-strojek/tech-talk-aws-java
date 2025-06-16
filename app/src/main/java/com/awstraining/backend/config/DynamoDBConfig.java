package com.awstraining.backend.config;

import java.net.URI;

import com.awstraining.backend.infrastructure.dynamodb.MeasurementDbEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
@Component
public class DynamoDBConfig {

    @Value("${aws.region:#{null}}")
    private String awsRegion;

    @Value("${aws.dynamodb.poolSize:100}")
    private int poolSize;

    // following properties are optional and only needed on local machine when working with localhost
    @Value("${aws.dynamodb.endpoint:#{null}}")
    private String dynamodbEndpoint;

    @Value("${aws.dynamodb.accessKey:#{null}}")
    private String dynamodbAccessKey;

    @Value("${aws.dynamodb.secretKey:#{null}}")
    private String dynamodbSecretKey;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        // this is the case when using a locally started dynamodb container
        if (dynamodbEndpoint != null && dynamodbAccessKey != null && dynamodbSecretKey != null) {
            final AwsBasicCredentials credentials = AwsBasicCredentials.create(
                    dynamodbAccessKey,
                    dynamodbSecretKey
            );

            return DynamoDbClient.builder()
                    .region(Region.of(awsRegion)) // or your specific region
                    .endpointOverride(URI.create(dynamodbEndpoint)) // e.g., http://localhost:8000
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();
        } else {
            return DynamoDbClient.builder()
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .region(new DefaultAwsRegionProviderChain().getRegion())
                    .build();
        }
    }

    @Bean
    public DynamoDbTable<MeasurementDbEntity> measurementTable(final DynamoDbClient dynamoDbClient,
            @Value("${aws.measurements.dynamodb.tableName:Measurements}") final String measurementTableName) {
        final DynamoDbEnhancedClient dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        final TableSchema<MeasurementDbEntity> measurementDbSchema = TableSchema.fromBean(MeasurementDbEntity.class);
        return dynamoDbEnhancedClient.table(measurementTableName, measurementDbSchema);
    }
}
