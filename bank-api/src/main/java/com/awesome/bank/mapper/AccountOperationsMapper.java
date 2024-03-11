package com.awesome.bank.mapper;

import com.awesome.bank.domain.model.Account;
import com.awesome.bank.domain.model.Operation;
import com.awesome.bank.dto.generated.AccountOperationsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AccountOperationsMapper {
    AccountOperationsMapper MAPPER = Mappers.getMapper(AccountOperationsMapper.class);

    @Mapping(source = "account", target = "account")
    @Mapping(target = "operations", expression = "java(OperationToDtoMapper.MAPPER.map(account.getOperations()))")
    AccountOperationsDto map(Account account);
}
