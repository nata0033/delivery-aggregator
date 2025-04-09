package com.example.delivery_aggregator.dto.external_api.dellin.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderDates {
    @JsonProperty("pickup")
    private String pickup;
    @JsonProperty("senderAddressTime")
    private String senderAddressTime;
    @JsonProperty("senderTerminalTime")
    private String senderTerminalTime;
    @JsonProperty("arrivalToOspSender")
    private String arrivalToOspSender;
    @JsonProperty("derrivalFromOspSender")
    private String derrivalFromOspSender;
    @JsonProperty("arrivalToOspReceiver")
    private String arrivalToOspReceiver;
    @JsonProperty("arrivalToPriceport")
    private String arrivalToPriceport;
    @JsonProperty("arrivalToPriceportMax")
    private String arrivalToPriceportMax;
    @JsonProperty("giveoutFromOspReceiver")
    private String giveoutFromOspReceiver;
    @JsonProperty("giveoutFromOspReceiverMax")
    private String giveoutFromOspReceiverMax;
    @JsonProperty("derrivalFromOspReceiver")
    private String derrivalFromOspReceiver;
    @JsonProperty("createTo")
    private String createTo;
    @JsonProperty("derrivalToAddress")
    private String derrivalToAddress;
    @JsonProperty("derivalToAddressMax")
    private String derivalToAddressMax;
}
