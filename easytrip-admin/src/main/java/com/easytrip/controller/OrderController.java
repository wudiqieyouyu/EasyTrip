package com.easytrip.controller;

import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.entity.po.OrderInfo;
import com.easytrip.entity.query.OrderInfoQuery;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.service.OrderInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderController extends ABaseController {

    @Resource
    private OrderInfoService orderInfoService;

    @RequestMapping("loadOrderList")
    @GlobalInterceptor
    public ResponseVO loadOrderList(OrderInfoQuery query){
        query.setOrderBy("create_time desc");
        return getSuccessResponseVO(orderInfoService.findListByPage(query));
    }
}
