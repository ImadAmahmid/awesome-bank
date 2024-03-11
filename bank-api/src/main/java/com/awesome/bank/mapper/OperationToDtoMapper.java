package com.awesome.bank.mapper;

import com.awesome.bank.domain.model.Operation;
import com.awesome.bank.dto.generated.OperationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Mapper
public interface OperationToDtoMapper {
  OperationToDtoMapper MAPPER = Mappers.getMapper(OperationToDtoMapper.class);

  @Mapping(source = "operation.account.id", target = "accountId")
  @Mapping(source = "operation.account.balance", target = "newAccountBalance")
  @Mapping(target = "createdAt", expression = "java(com.awesome.bank.utility.DateUtils.toDateString(operation.getCreatedAt()))")
  OperationDto map(Operation operation);

  List<OperationDto> map(List<Operation> operation);

}
