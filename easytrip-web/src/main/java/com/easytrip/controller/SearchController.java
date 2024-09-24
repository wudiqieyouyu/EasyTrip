package com.easytrip.controller;

import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.annotation.VerifyParam;
import com.easytrip.entity.enums.ResponseCodeEnum;
import com.easytrip.entity.po.Line;
import com.easytrip.entity.po.Regions;
import com.easytrip.entity.po.Sales;
import com.easytrip.entity.query.LineQuery;
import com.easytrip.entity.query.RegionsQuery;
import com.easytrip.entity.query.SalesQuery;
import com.easytrip.entity.query.TripArticleQuery;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.entity.vo.TripArticleVo;
import com.easytrip.exception.BusinessException;
import com.easytrip.service.LineService;
import com.easytrip.service.RegionsService;
import com.easytrip.service.SalesService;
import com.easytrip.service.TripArticleService;
import com.easytrip.utils.StringTools;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("search")
@RestController
public class SearchController extends ABaseController{

        @Resource
        private TripArticleService tripArticleService;
        @Resource
        private SalesService salesService;
        @Resource
        private LineService lineService;

        @Resource
        private RegionsService regionsService;

        @RequestMapping("searchArticle")
        @GlobalInterceptor()
        public ResponseVO searchArticle(@VerifyParam(required = true,min = 3)String keyword){
                TripArticleQuery query = new TripArticleQuery();
                query.setTitleFuzzy(keyword);
                return getSuccessResponseVO(tripArticleService.findListByPage(query));
        }

        @RequestMapping("searchSales")
        @GlobalInterceptor()
        public ResponseVO searchSales(@VerifyParam(required = true) String title,Integer level,String maxPrice,String minPrice,String orderBy){
                if (!StringTools.isEmpty(maxPrice)&&StringTools.isEmpty(minPrice)){
                        throw new BusinessException(ResponseCodeEnum.CODE_600);
                }
                SalesQuery query = new SalesQuery();
                query.setSalesNameFuzzy(title);
                query.setSalesLevel(level);
                if (!StringTools.isEmpty(orderBy)){
                    query.setOrderBy(orderBy);
                }else {
                        query.setOrderBy("sales_number desc");
                }
                if (!StringTools.isEmpty(maxPrice)){
                        query.setMaxPrice(maxPrice);
                }
                query.setMinPrice(minPrice);
                PaginationResultVO<Sales> saleList = salesService.findListByPage(query);
                for (Sales s : saleList.getList()){
                      s.setSalesAddress(getAddressNameByGeocoder(s.getSalesAddress()));
                }
                return getSuccessResponseVO(saleList);
        }

        @RequestMapping("searchLine")
        @GlobalInterceptor()
        public ResponseVO searchLine(@VerifyParam(required = true)String keyword){
                RegionsQuery regionsQuery = new RegionsQuery();
                regionsQuery.setRegionName(keyword);
                List<Regions> regionsList=regionsService.findListByParam(regionsQuery);

                return getSuccessResponseVO(regionsList.get(0).getRegionId());
        }

}
