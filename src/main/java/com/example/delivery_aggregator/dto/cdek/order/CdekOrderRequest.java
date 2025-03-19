package com.example.delivery_aggregator.dto.cdek.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CdekOrderRequest {
    private String uuid;
    private Integer type;
    @JsonProperty("additional_order_types")
    private List<Integer> additionalOrderTypes;
    private String number;
    @JsonProperty("accompanying_number")
    private String accompanyingNumber;
    @JsonProperty("tariff_code")
    private Integer tariffCode;
    private String comment;
    @JsonProperty("shipment_point")
    private String shipmentPoint;
    @JsonProperty("delivery_point")
    private String deliveryPoint;
    @JsonProperty("date_invoice")
    private String dateInvoice;
    @JsonProperty("shipper_name")
    private String shipperName;
    @JsonProperty("shipper_address")
    private String shipperAddress;
    @JsonProperty("delivery_recipient_cost")
    private DeliveryRecipientCost deliveryRecipientCost;
    @JsonProperty("delivery_recipient_cost_adv")
    private List<Object> deliveryRecipientCostAdv;
    private Sender sender;
    private Seller seller;
    private Recipient recipient;
    @JsonProperty("from_location")
    private Location fromLocation;
    @JsonProperty("to_location")
    private Location toLocation;
    private List<Service> services;
    private List<Package> packages;
    @JsonProperty("is_client_return")
    private Boolean isClientReturn;
    @JsonProperty("developer_key")
    private String developerKey;
    private String print;
    @JsonProperty("widget_token")
    private String widgetToken;
}
