package com.example.delivery_aggregator.repository;

import com.example.delivery_aggregator.entity.DpdCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DpdCityRepository extends JpaRepository<DpdCity, Long> {
    List<DpdCity> findAllByCityName(String cityName);
}
