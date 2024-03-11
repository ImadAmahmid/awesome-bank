package com.awesome.bank.mapper;

import com.awesome.bank.domain.model.Account;
import com.awesome.bank.dto.generated.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AccountToDtoMapper {
    AccountToDtoMapper MAPPER = Mappers.getMapper(AccountToDtoMapper.class);

    AccountDto map(Account account);

    List<AccountDto> map(List<Account> account);
}
