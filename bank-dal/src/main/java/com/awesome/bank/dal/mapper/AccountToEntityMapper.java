package com.awesome.bank.dal.mapper;

import com.awesome.bank.dal.entity.AccountEntity;
import com.awesome.bank.domain.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountToEntityMapper {
  AccountToEntityMapper MAPPER = Mappers.getMapper(AccountToEntityMapper.class);

  AccountEntity map(Account account);
}
