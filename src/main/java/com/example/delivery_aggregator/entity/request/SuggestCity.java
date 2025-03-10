package com.example.delivery_aggregator.entity.request;

import lombok.Data;

@Data
public class SuggestCity {
    //Идентификатор населенного пункта СДЭК
    private String city_uuid;

    //Код населенного пункта СДЭК
    private Integer code;

    //Наименование населенного пункта СДЭК (город, район, регион, страна)
    private String full_name;
}
