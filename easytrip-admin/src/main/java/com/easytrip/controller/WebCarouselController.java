package com.easytrip.controller;


import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.annotation.VerifyParam;
import com.easytrip.entity.enums.PermissionCodeEnum;
import com.easytrip.entity.po.WebCarousel;
import com.easytrip.entity.query.WebCarouselQuery;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.service.WebCarouselService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * app轮播 Controller
 */
@RestController("webCarouselController")
@RequestMapping("/webCarousel")
public class WebCarouselController extends ABaseController {

    @Resource
    private WebCarouselService webCarouselService;

    /**
     * 根据条件分页查询
     */
    @RequestMapping("/loadDataList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.WEB_CAROUSEL_LIST)
    public ResponseVO loadDataList(WebCarouselQuery query) {
        query.setOrderBy("sort asc");
        return getSuccessResponseVO(webCarouselService.findListByParam(query));
    }

    @RequestMapping("/saveCarousel")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.WEB_CAROUSEL_EDIT)
    public ResponseVO saveCarousel(WebCarousel webCarousel) {
        webCarouselService.saveCarousel(webCarousel);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/delCarousel")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.WEB_CAROUSEL_EDIT)
    public ResponseVO delCarousel(@VerifyParam(required = true) Integer carouselId) {
        webCarouselService.deleteWebCarouselByCarouselId(carouselId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/changeSort")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.WEB_CAROUSEL_EDIT)
    public ResponseVO changeSort(@VerifyParam(required = true) String carouselIds) {
        webCarouselService.changeSort(carouselIds);
        return getSuccessResponseVO(null);
    }

}