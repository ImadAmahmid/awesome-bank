package com.awesome.bank.dal.repository;

import com.awesome.bank.dal.entity.AccountEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AccountRepositoryImpl implements AccountRepository {


    private static final String LOCK_TIMEOUT = "3000";
    private static final String LOCK_HINT = "jakarta.persistence.lock.timeout";
    private final AccountPagingAndSortingRepository pagingAndSortingRepository;
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<AccountEntity> findAll() {
        return pagingAndSortingRepository.findAll();
    }

    @Override
    public AccountEntity save(AccountEntity account) {
      return  pagingAndSortingRepository.save(account);
    }

    @Override
    public Optional<AccountEntity> findById(Long accountId) {
        return pagingAndSortingRepository.findById(accountId);
    }

    /**
     * It's rather debatable which type of locking to be used in these scenarios, the optimistic vs the pessimistic locking.
     * My choice here was leaning more towards the optimistic locking to not impede the performance of the application
     * especially that we could just rollback everything if an update happens to the account during the span of the
     * transaction. We also do not call any external system or do something that we will not recover from, in which
     * case I would have opted for a pessimistic lock to stop the concurrent process until the first process commits his changes.
     * It's also a tradeoff between blocking every access to the accessed rows VS how many transaction would fail..
     *
     *
     * @param accountId
     * @return Account entity if any and locks the record
     */
    @Override
    public Optional<AccountEntity> findByAccountIdAndLock(Long accountId) {
      AccountEntity accountEntity = entityManager.find(AccountEntity.class, accountId, LockModeType.PESSIMISTIC_READ, Map.of(LOCK_HINT, LOCK_TIMEOUT));

      return Optional.of(accountEntity);
    }

    @Override
    public void deleteAccountById(Long accountId) {
        pagingAndSortingRepository.deleteById(accountId);
    }
}
