package com.indorse.blood.bank.rest.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonorStatDto {

    private Long donatedBlood;
    private String name;
    private String memberId;
}
