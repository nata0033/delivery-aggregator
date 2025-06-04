package com.example.delivery_aggregator.service.db;

import com.example.delivery_aggregator.entity.DpdCity;
import com.example.delivery_aggregator.mappers.DpdMapper;
import com.example.delivery_aggregator.repository.DpdCityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dpd.ws.geography._2015_05_20.City;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DpdCityService {

    private final DpdCityRepository dpdCityRepository;

    private final DpdMapper dpdMapper;

    public void saveAll(List<City> cities){
        dpdCityRepository.deleteAll();
        dpdCityRepository.saveAll(dpdMapper.cityListToDpdCityList(cities));
    }

    public Long getCityIdByCityName(String cityName) {
        List<DpdCity> cities = dpdCityRepository.findAllByCityName(cityName);
        if(cities.isEmpty())
            return null;
        return cities.getFirst().getCityId();
    }
}
