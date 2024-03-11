package com.awesome.bank.dal.mapper;

import com.awesome.bank.dal.entity.AccountEntity;
import com.awesome.bank.domain.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AccountToDomainMapper {
  AccountToDomainMapper MAPPER = Mappers.getMapper(AccountToDomainMapper.class);

  @Mapping(target = "operations", expression = "java(OperationToDomainMapper.MAPPER.map(account.operations))")
  Account map(AccountEntity account);

  List<Account> map(List<AccountEntity> accounts);

}
