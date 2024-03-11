package com.awesome.bank.mapper;

import com.awesome.bank.domain.model.Account;
import com.awesome.bank.dto.generated.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AccountToDomainMapper {
    AccountToDomainMapper MAPPER = Mappers.getMapper(AccountToDomainMapper.class);

    Account map(AccountDto account);

    List<Account> map(List<AccountDto> accounts);

}
