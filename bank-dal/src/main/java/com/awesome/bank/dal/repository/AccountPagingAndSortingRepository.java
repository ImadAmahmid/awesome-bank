package com.awesome.bank.dal.repository;

import com.awesome.bank.dal.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Include the CRUD operations as well as the lock account by ID defined using @Query annotation
 */
@Repository
public interface AccountPagingAndSortingRepository extends JpaRepository<AccountEntity, Long> {

}
