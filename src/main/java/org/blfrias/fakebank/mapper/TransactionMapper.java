package org.blfrias.fakebank.mapper;

import org.blfrias.fakebank.dto.TransactionDTO;
import org.blfrias.fakebank.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction toEntity(TransactionDTO dto);
    TransactionDTO toDto(Transaction entity);
}