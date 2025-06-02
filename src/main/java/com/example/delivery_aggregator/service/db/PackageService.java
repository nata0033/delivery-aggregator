package com.example.delivery_aggregator.service.db;

import com.example.delivery_aggregator.dto.aggregator.PackageDto;
import com.example.delivery_aggregator.entity.Order;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;

    private final AggregatorMapper aggregatorMapper;

    public void saveList(Order order, List<PackageDto> packages){
        packageRepository.saveAll(aggregatorMapper.packageDtoListToPackageList(packages, order));
    }
}
