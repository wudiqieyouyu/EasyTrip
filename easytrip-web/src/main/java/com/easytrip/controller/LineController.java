package com.easytrip.controller;

import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.entity.po.Line;
import com.easytrip.entity.po.Line2Sales;
import com.easytrip.entity.query.LineQuery;
import com.easytrip.entity.query.RegionsQuery;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.exception.BusinessException;
import com.easytrip.service.Line2SalesService;
import com.easytrip.service.LineService;
import com.easytrip.service.RegionsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/line")
public class LineController extends ABaseController{

    @Resource
    private RegionsService regionsService;
    @Resource
    private LineService lineService;

    @Resource
    private Line2SalesService line2SalesService;

    @RequestMapping("loadLineList")
    @GlobalInterceptor
    public ResponseVO loadLineList(LineQuery query){
        query.setOrderBy("create_time desc");
        List<Line> lineList= lineService.findListByParam(query);
        if (lineList.size()==0){
            throw new BusinessException("暂未更新路线图,敬请期待");
        }
        lineList=line2SalesService.updateLines(lineList);
        return getSuccessResponseVO(lineList);
    }


}
