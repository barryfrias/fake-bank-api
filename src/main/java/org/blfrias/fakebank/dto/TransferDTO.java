package org.blfrias.fakebank.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferDTO {
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private Double amount;
}