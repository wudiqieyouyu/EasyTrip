package com.easytrip.controller;

import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.entity.po.Sales;
import com.easytrip.entity.query.SalesQuery;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.service.SalesCommentService;
import com.easytrip.service.SalesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sales")
public class SalesController extends ABaseController{
    @Resource
    private SalesService salesService;

    @Resource
    private SalesCommentService salesCommentService;

    @RequestMapping("/loadSales")
    @GlobalInterceptor()
    public ResponseVO loadSales(SalesQuery query){
        query.setOrderBy("sales_number desc");
        query.setPageNo(1);
        query.setPageSize(8);
        query.setShowDetail(false);
        return getSuccessResponseVO(this.salesService.findListByPage(query));
    }

    @RequestMapping("/loadSalesDetail")
    @GlobalInterceptor()
    public ResponseVO loadSalesDetail(Integer salesId){
        Sales sales = this.salesService.getSalesBySalesId(salesId);
        sales.setSalesAddress(getAddressNameByGeocoder(sales.getSalesAddress()));
        return getSuccessResponseVO(sales);
    }

    @RequestMapping("/loadSalesComment")
    @GlobalInterceptor()
    public ResponseVO loadSalesComment(Integer salesId){

      return getSuccessResponseVO(null);
    }
}
