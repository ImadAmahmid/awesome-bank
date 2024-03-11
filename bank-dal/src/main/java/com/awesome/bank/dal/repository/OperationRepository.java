package com.awesome.bank.dal.repository;

import com.awesome.bank.dal.entity.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<OperationEntity, Long> {

    List<OperationEntity> findByAccountIdAndCreatedAtBetweenOrderByCreatedAtDesc(Long accountId, OffsetDateTime startDate, OffsetDateTime endDate);

    List<OperationEntity> findAllByAccountId(Long accountId);
}
