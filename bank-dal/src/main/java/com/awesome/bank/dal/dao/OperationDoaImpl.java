package com.awesome.bank.dal.dao;

import com.awesome.bank.dal.entity.OperationEntity;
import com.awesome.bank.dal.mapper.AccountToEntityMapper;
import com.awesome.bank.dal.mapper.OperationToDomainMapper;
import com.awesome.bank.dal.repository.OperationRepository;
import com.awesome.bank.domain.dao.OperationDao;
import com.awesome.bank.domain.model.Account;
import com.awesome.bank.domain.model.Operation;
import com.awesome.bank.domain.model.OperationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OperationDoaImpl implements OperationDao {

    private final OperationRepository operationRepository;

    @Override
    public Operation saveOperation(Account account, BigDecimal amount, OperationType type) {
        LOG.info("Saving operation for account | accountId=[{}] accountVersion=[{}] amount=[{}]", account.getId(), account.getVersion(), amount);
        OperationEntity operationEntity = OperationEntity.builder()
                .account(AccountToEntityMapper.MAPPER.map(account))
                .amount(amount).createdAt(OffsetDateTime.now()).type(type).build();
        OperationEntity saved = operationRepository.save(operationEntity);

        return OperationToDomainMapper.MAPPER.map(saved);
    }

    @Override
    public List<Operation> findAllOperationsForAccountSinceDate(Long accountId, OffsetDateTime startDate) {
        List<OperationEntity> latestOperations = operationRepository.findByAccountIdAndCreatedAtBetweenOrderByCreatedAtDesc(accountId, startDate, OffsetDateTime.now());

        return OperationToDomainMapper.MAPPER.map(latestOperations);
    }
}
