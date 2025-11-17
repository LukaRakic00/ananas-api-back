package com.ananas.exceltxml.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    private String search;
    private String naziv;
    private String vrednost;
    private String napomena;
    private int page = 0;
    private int size = 20;
}

