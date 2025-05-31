package com.example.delivery_aggregator.service.db;

import com.example.delivery_aggregator.entity.Package;
import com.example.delivery_aggregator.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;

    public void createList(List<Package> packages){
        packageRepository.saveAll(packages);
    }
}
