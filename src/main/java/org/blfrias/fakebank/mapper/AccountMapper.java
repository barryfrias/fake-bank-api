package org.blfrias.fakebank.mapper;

import org.blfrias.fakebank.dto.AccountDTO;
import org.blfrias.fakebank.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    static AccountDTO mapToAccountDTO(Account savedAccount) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(savedAccount.getId());
        accountDTO.setBalance(savedAccount.getBalance());
        return accountDTO;
    }

    Account toEntity(AccountDTO dto);
    AccountDTO toDto(Account entity);
}