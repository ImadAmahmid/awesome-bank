package com.awesome.bank.kafka.mapper;


import com.awesome.bank.kafka.generated.dto.OperationV2;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OperationToDtoMapper {
    OperationToDtoMapper MAPPER = Mappers.getMapper(OperationToDtoMapper.class);

    @Mapping(target = "accountId", source = "operation.account.id")
    @Mapping(target = "type", expression = "java(operation.getType().name())")
    OperationV2 map(com.awesome.bank.domain.model.Operation operation);


}
