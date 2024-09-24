package com.easytrip.service;

import com.easytrip.entity.dto.StatisticsDataDto;
import com.easytrip.entity.dto.StatisticsDataWeekDto;

import java.util.List;

public interface StatisticsDataService {
    List<StatisticsDataDto> getAllData();

    StatisticsDataWeekDto getAppWeekData();

    StatisticsDataWeekDto getContentWeekData();
}
