package com.awstraining.backend.infrastructure.dynamodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class MeasurementDbEntity {
    private String deviceId;
    private Long creationTime;
    private Double value;
    private String type;
    private Long expiresAt;

    @DynamoDbPartitionKey
    public String getDeviceId() {
        return deviceId;
    }

    @DynamoDbSortKey
    public Long getCreationTime() {
        return creationTime;
    }
}
