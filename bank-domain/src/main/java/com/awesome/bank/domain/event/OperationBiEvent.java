package com.awesome.bank.domain.event;

import com.awesome.bank.event.BiEvent;
import com.awesome.bank.domain.model.Operation;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder()
@Data
public class OperationBiEvent extends BiEvent<Operation> {

}
