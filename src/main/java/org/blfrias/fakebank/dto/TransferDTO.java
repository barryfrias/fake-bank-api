package org.blfrias.fakebank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private double amount;
}