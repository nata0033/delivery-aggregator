package com.example.delivery_aggregator.dto.cdek.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CdekInfoAccompanyingWaybillDto {
    @JsonProperty("client_name")
    private String clientName;
    @JsonProperty("flight_number")
    private String flightNumber;
    @JsonProperty("air_waybill_numbers")
    private List<String> airWaybillNumbers;
    @JsonProperty("vehicle_numbers")
    private List<String> vehicleNumbers;
    @JsonProperty("vehicle_driver")
    private String vehicleDriver;
    @JsonProperty("planned_departure_date_time")
    private LocalDateTime plannedDepartureDateTime;
}
