package com.example.delivery_aggregator;

import com.example.delivery_aggregator.entity.DeliveryService;
import com.example.delivery_aggregator.service.db.DeliveryServiceService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor
class DeliveryAggregatorApplicationTests {

	private final DeliveryServiceService deliveryServiceService;

	@Test
    public void test() {
		DeliveryService deliveryService = deliveryServiceService.createWithName("serviceName");
	}
}
