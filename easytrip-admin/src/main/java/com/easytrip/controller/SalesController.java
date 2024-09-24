package com.easytrip.controller;

import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.annotation.VerifyParam;
import com.easytrip.entity.po.Sales;
import com.easytrip.entity.query.SalesQuery;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.service.SalesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sales")
public class SalesController extends ABaseController{

    @Resource
    private SalesService salesService;

    @RequestMapping("loadSalesList")
    @GlobalInterceptor
    public ResponseVO loadSalesList(SalesQuery query){
        query.setOrderBy("create_time desc");
        PaginationResultVO<Sales> resultVO = salesService.findListByPage(query);
        for (Sales s:resultVO.getList()){
            s.setAddressCode(s.getSalesAddress());
          s.setSalesAddress(getAddressNameByGeocoder(s.getSalesAddress()));  ;
        }

        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/saveSales")
    @GlobalInterceptor()
    public ResponseVO saveOperator(Sales bean) {
        bean.setSalesAddress(bean.getAddressCode());
        salesService.saveSales(bean);
        return getSuccessResponseVO(null);
    }
    @RequestMapping("/delSales")
    @GlobalInterceptor()
    public ResponseVO delSales(@VerifyParam(required = true)String salesId) {
        salesService.deleteSalesBySalesId(Integer.parseInt(salesId));
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/delSalesBatch")
    @GlobalInterceptor()
    public ResponseVO delOperatorBatch(@VerifyParam(required = true) String salesIdS) {
        salesService.deleteBatch(salesIdS);
        return getSuccessResponseVO(null);
    }
}
