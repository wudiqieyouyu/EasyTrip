package com.easytrip.controller;

import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.entity.po.Line;
import com.easytrip.entity.po.Line2Sales;
import com.easytrip.entity.po.Sales;
import com.easytrip.entity.query.Line2SalesQuery;
import com.easytrip.entity.query.LineQuery;
import com.easytrip.entity.query.SalesQuery;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.service.Line2SalesService;
import com.easytrip.service.LineService;
import com.easytrip.service.SalesService;
import com.easytrip.utils.StringTools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/line")
public class LineController extends ABaseController{

    @Resource
    private LineService lineService;
    @Resource
    private Line2SalesService line2SalesService;
    @Resource
    private SalesService salesService;

    @RequestMapping("loadLineList")
    @GlobalInterceptor
    public ResponseVO loadLineList(LineQuery query){
        query.setOrderBy("create_time desc");
        PaginationResultVO<Line> resultVO = lineService.findListByPage(query);
        for (Line item:resultVO.getList()){
            Line2SalesQuery line2SalesQuery = new Line2SalesQuery();
            line2SalesQuery.setLineId(item.getLineId());
          List<Line2Sales> line2SalesList=  line2SalesService.findListByParam(line2SalesQuery);
          List<Integer> salesIdList =line2SalesList.stream().map(Line2Sales::getSalesId).collect(Collectors.toList());
          List<Sales> salesList = new ArrayList<>();
          for(Integer id:salesIdList){
              SalesQuery salesQuery = new SalesQuery();
              salesQuery.setSalesId(id);
              salesList.addAll(salesService.findListByParam(salesQuery));
          }
           item.setSalesList(salesList);
        }
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("saveLine")
    @GlobalInterceptor
    public ResponseVO saveLine(Line bean,String salesString){
        String playTime = StringTools.getRandomNumber(1)+"小时";
        lineService.saveLine(bean,salesString,playTime);
        return getSuccessResponseVO(null);
    }
}
