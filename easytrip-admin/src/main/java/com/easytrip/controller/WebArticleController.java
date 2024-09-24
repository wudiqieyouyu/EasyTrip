package com.easytrip.controller;


import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.annotation.VerifyParam;
import com.easytrip.entity.dto.SessionUserAdminDto;
import com.easytrip.entity.enums.PermissionCodeEnum;
import com.easytrip.entity.po.TripArticle;
import com.easytrip.entity.query.TripArticleQuery;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.service.TripArticleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * app轮播 Controller
 */
@RestController("webArticleController")
@RequestMapping("/tripArticle")
public class WebArticleController extends ABaseController {

    @Resource
    private TripArticleService tripArticleService;

    /**
     * 根据条件分页查询
     */
    @RequestMapping("/loadDataList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.ARTICLE_LIST)
    public ResponseVO loadDataList(TripArticleQuery query) {
        query.setOrderBy("post_time desc");
        return getSuccessResponseVO(tripArticleService.findListByPage(query));
    }

//    /*
//     * 保存
//     * */
//    @RequestMapping("/saveArticleInfo")
//    @GlobalInterceptor(permissionCode = PermissionCodeEnum.ARTICLE_EDIT)
//    public ResponseVO saveShareInfo(HttpSession session, TripArticle bean) {
//        SessionUserAdminDto adminDto = getUserAdminFromSession(session);
//        tripArticleService.saveArticle(bean, adminDto.getSuperAdmin());
//        return getSuccessResponseVO(null);
//    }


}