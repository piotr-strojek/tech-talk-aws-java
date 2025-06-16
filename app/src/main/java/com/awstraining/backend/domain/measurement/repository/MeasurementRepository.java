package com.awstraining.backend.domain.measurement.repository;

import java.util.List;

import com.awstraining.backend.domain.measurement.model.MeasurementDO;

public interface MeasurementRepository {
    void save(MeasurementDO measurement);

    List<MeasurementDO> findById(String deviceId);
}
