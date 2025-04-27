package com.example.delivery_aggregator.dto.pages.russian_cities;

import lombok.Data;

@Data
public class RussianCitiesDto {
        private Coords coords;
        private String district;
        private String name;
        private Integer population;
        private String subject;
}
