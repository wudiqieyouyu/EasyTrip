package com.easytrip.controller;

import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.entity.po.Regions;
import com.easytrip.entity.query.RegionsQuery;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.service.RegionsService;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/region")
public class RegionController extends ABaseController{

    @Resource
    private RegionsService regionsService;

    @RequestMapping("/loadRegion")
    @GlobalInterceptor()
    public ResponseVO loadRegion(RegionsQuery regionsQuery){
        regionsQuery.setTree(true);
        List<Regions> regionsList = regionsService.findListByParam(regionsQuery);
        return getSuccessResponseVO(regionsList);
    }



    @RequestMapping("/loadRegion4Leaderboard")
    @GlobalInterceptor()
    public ResponseVO loadRegion4Leaderboard(RegionsQuery regionsQuery){
        regionsQuery.setTree(false);
        regionsQuery.setOrderBy("region_id desc");
        return getSuccessResponseVO(regionsService.findListByPage(regionsQuery));
    }
}
