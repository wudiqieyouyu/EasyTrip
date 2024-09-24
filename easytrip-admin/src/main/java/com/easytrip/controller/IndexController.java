package com.easytrip.controller;

import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.entity.dto.StatisticsDataDto;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.service.StatisticsDataService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/index")
public class IndexController extends ABaseController{
    @Resource
    private StatisticsDataService statisticsDataService;


    @RequestMapping("/getAllData")
    @GlobalInterceptor()
    public ResponseVO getAllData() {
        List<StatisticsDataDto> statisticsDataDtoList = statisticsDataService.getAllData();
        return getSuccessResponseVO(statisticsDataDtoList);
    }

    @RequestMapping("/getAppWeekData")
    @GlobalInterceptor()
    public ResponseVO getAppWeekData() {
        return getSuccessResponseVO(statisticsDataService.getAppWeekData());
    }

    @RequestMapping("/getContentWeekData")
    @GlobalInterceptor()
    public ResponseVO getContentWeekData() {
        return getSuccessResponseVO(statisticsDataService.getContentWeekData());
    }

}
