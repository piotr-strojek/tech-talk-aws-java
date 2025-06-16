package com.awstraining.backend.infrastructure.dynamodb;

import java.util.List;

import com.awstraining.backend.domain.measurement.CouldNotSaveMeasurementException;
import com.awstraining.backend.domain.measurement.UnknownDeviceException;
import com.awstraining.backend.domain.measurement.model.MeasurementDO;
import com.awstraining.backend.domain.measurement.repository.MeasurementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

@RequiredArgsConstructor
@Repository
public class MeasurementDbRepository implements MeasurementRepository {
    @Value("${backend.measurements.ttlInSeconds:2592000}")
    private Long ttlInSeconds;

    private final DynamoDbTable<MeasurementDbEntity> measurementTable;

    @Override
    public void save(final MeasurementDO measurement) {
        try {
            measurementTable.putItem(MeasurementDbEntityMapper.toEntity(measurement, ttlInSeconds));
        } catch(final DynamoDbException e) {
            throw new CouldNotSaveMeasurementException();
        }
    }

    @Override
    public List<MeasurementDO> findById(final String deviceId) {
        final QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                .partitionValue(deviceId)
                .build());

        // Query all results into a List
        try {
            return measurementTable.query(r -> r.queryConditional(queryConditional))
                    .items()
                    .stream()
                    .map(MeasurementDbEntityMapper::toDomain)
                    .toList();
        } catch(final DynamoDbException e) {
            throw new UnknownDeviceException();
        }
    }
}
