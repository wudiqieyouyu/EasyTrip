package com.easytrip.controller;


import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.entity.dto.UserLoginDto;
import com.easytrip.entity.enums.OrderTypeEnum;
import com.easytrip.entity.po.TripArticle;
import com.easytrip.entity.po.TripBoard;
import com.easytrip.entity.po.WebCarousel;
import com.easytrip.entity.query.TripArticleQuery;
import com.easytrip.entity.query.WebCarouselQuery;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.entity.vo.TripArticleVo;
import com.easytrip.service.TripArticleService;
import com.easytrip.service.TripBoardService;
import com.easytrip.service.WebCarouselService;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController("indexController")
@RequestMapping("index")
public class IndexController extends ABaseController{

    @Resource
    private WebCarouselService webCarouselService;

    @Resource
    private TripBoardService tripBoardService;

    @Resource
    private TripArticleService tripArticleService;

    @RequestMapping("/loadCarousel")
    @GlobalInterceptor
    public ResponseVO loadCarousel() {
        WebCarouselQuery query =new WebCarouselQuery();
        query.setOrderBy("sort asc");
        List<WebCarousel> carouselList =webCarouselService.findListByParam(query);
        return getSuccessResponseVO(carouselList);
    }

    @RequestMapping("/loadBoard")
    @GlobalInterceptor
    public ResponseVO loadBoard( @RequestHeader(value = "token",required= false) String token) {
        Integer postType ;
        UserLoginDto userLoginDto =getUserAdminFromToken(token);
        if (userLoginDto == null){
            postType = 1;
        }else{
            postType= isSuperAdmin(userLoginDto.getUserId())?null:1;
        }
        List<TripBoard> boardList =tripBoardService.getBoardTree(postType);
        return getSuccessResponseVO(boardList);
    }

    @RequestMapping("/loadArticle")
    @GlobalInterceptor
    public ResponseVO loadArticle(TripArticleQuery query,Integer orderType) {
        OrderTypeEnum orderTypeEnum = OrderTypeEnum.getByType(orderType);
        if (orderTypeEnum ==null){
            orderTypeEnum = OrderTypeEnum.Hot;
        }
        query.setOrderBy(orderTypeEnum.getOrderSql());
        PaginationResultVO resultVO = tripArticleService.findListByPage(query);
        return getSuccessResponseVO(convert2PaginationVo(resultVO, TripArticleVo.class));
    }
}
