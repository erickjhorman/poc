package com.eqfx.latam.poc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SalesByQuarterWrapper {

    private String sourceFile;
    private String targetFile;
    private List<Integer> year;
}
