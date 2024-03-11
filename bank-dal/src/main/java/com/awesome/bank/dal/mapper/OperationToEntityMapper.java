package com.awesome.bank.dal.mapper;

import com.awesome.bank.dal.entity.OperationEntity;
import com.awesome.bank.domain.model.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OperationToEntityMapper {
  OperationToEntityMapper MAPPER = Mappers.getMapper(OperationToEntityMapper.class);

  OperationEntity map(Operation operation);
}
