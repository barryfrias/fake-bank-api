package org.blfrias.fakebank.mapper;

import org.blfrias.fakebank.dto.AccountDTO;
import org.blfrias.fakebank.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toEntity(AccountDTO dto);
    AccountDTO toDto(Account entity);
}