package com.awesome.bank.dal.mapper;

import com.awesome.bank.dal.entity.AccountEntity;
import com.awesome.bank.dal.entity.OperationEntity;
import com.awesome.bank.domain.model.Account;
import com.awesome.bank.domain.model.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OperationToDomainMapper {
  OperationToDomainMapper MAPPER = Mappers.getMapper(OperationToDomainMapper.class);

  @Mapping(target = "account", expression = "java(mapAccountDetails(operation.getAccount()))")
  Operation map(OperationEntity operation);

  List<Operation> map(List<OperationEntity> operation);


  /**
   * To avoid circular mapping between account -> operations -> accounts and so on
   * @param account
   * @return
   */
  default Account mapAccountDetails(AccountEntity account) {
    return  Account.builder().id(account.getId()).balance(account.getBalance()).build();
  }
}
