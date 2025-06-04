package com.example.delivery_aggregator.dto.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CdekInfoEntityDto {
    private String uuid;
    private Integer type;
    @JsonProperty("additional_order_types")
    private List<Integer> additionalOrderTypes;
    @JsonProperty("is_return")
    private Boolean isReturn;
    @JsonProperty("is_reverse")
    private Boolean isReverse;
    @JsonProperty("cdek_number")
    private String cdekNumber;
    private String number;
    @JsonProperty("accompanying_number")
    private String accompanyingNumber;
    @JsonProperty("accompanying_waybill")
    private CdekInfoAccompanyingWaybillDto accompanyingWaybill;
    @JsonProperty("tariff_code")
    private Integer tariffCode;
    private String comment;
    @JsonProperty("shipment_point")
    private String shipmentPoint;
    @JsonProperty("delivery_point")
    private String deliveryPoint;
    @JsonProperty("date_invoice")
    private LocalDate dateInvoice;
    @JsonProperty("keep_free_until")
    private LocalDateTime keepFreeUntil;
    @JsonProperty("shipper_name")
    private String shipperName;
    @JsonProperty("shipper_address")
    private String shipperAddress;
    @JsonProperty("delivery_recipient_cost")
    private CdekInfoDeliveryRecipientCostDto deliveryRecipientCost;
    @JsonProperty("delivery_recipient_cost_adv")
    private List<CdekInfoDeliveryRecipientCostAdvDto> deliveryRecipientCostAdv;
    private CdekInfoSenderDto sender;
    private CdekInfoSellerDto seller;
    private CdekInfoRecipientDto recipient;
    @JsonProperty("from_location")
    private CdekInfoLocationDto fromLocation;
    @JsonProperty("to_location")
    private CdekInfoLocationDto toLocation;
    private List<CdekInfoServiceDto> services;
    private List<Package> packages;
    private List<CdekInfoStatusDto> statuses;
    @JsonProperty("is_client_return")
    private Boolean isClientReturn;
    @JsonProperty("delivery_mode")
    private String deliveryMode;
    @JsonProperty("planned_delivery_date")
    private LocalDate plannedDeliveryDate;
    @JsonProperty("delivery_detail")
    private CdekInfoDeliveryDetailDto deliveryDetail;
    @JsonProperty("transacted_payment")
    private Boolean transactedPayment;
    @JsonProperty("delivery_problem")
    private List<CdekInfoDeliveryProblemDto> deliveryProblem;
    @JsonProperty("developer_key")
    private String developerKey;
    private CdekInfoCallsDto calls;
}
