package com.awstraining.backend.infrastructure.dynamodb;

import static java.lang.System.currentTimeMillis;

import com.awstraining.backend.domain.measurement.model.MeasurementDO;

public class MeasurementDbEntityMapper {

    public static MeasurementDbEntity toEntity(final MeasurementDO measurement, final Long ttl) {
        return new MeasurementDbEntity(
                measurement.getDeviceId(),
                measurement.getCreationTime(),
                measurement.getValue(),
                measurement.getType(),
                currentTimeMillis() + ttl
        );
    }

    public static MeasurementDO toDomain(final MeasurementDbEntity entity) {
        return new MeasurementDO(
                entity.getDeviceId(),
                entity.getValue(),
                entity.getType(),
                entity.getCreationTime()
        );
    }
}