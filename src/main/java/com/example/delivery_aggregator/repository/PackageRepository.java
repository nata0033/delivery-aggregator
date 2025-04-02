package com.example.delivery_aggregator.repository;

import com.example.delivery_aggregator.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PackageRepository extends JpaRepository<Package, UUID>{
}
