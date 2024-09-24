package com.easytrip.controller;

import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.annotation.VerifyParam;
import com.easytrip.entity.dto.UserLoginDto;
import com.easytrip.entity.po.OrderInfo;
import com.easytrip.entity.po.Sales;
import com.easytrip.entity.po.UserInfo;
import com.easytrip.entity.vo.OrderInfoVo;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.service.OrderInfoService;
import com.easytrip.service.SalesService;
import com.easytrip.service.UserInfoService;
import com.easytrip.utils.StringTools;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@RestController
@RequestMapping("/order")
public class OrderController extends ABaseController{

    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private SalesService salesService;

    @RequestMapping("loadOrderInfo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadOrderInfo(@RequestHeader(required = false,value = "token")String token, @VerifyParam(required = true) String saleId){
        UserLoginDto userLoginDto = getUserAdminFromToken(token);
        UserInfo user = userInfoService.getUserInfoByUserId(userLoginDto.getUserId());
        Sales sales = salesService.getSalesBySalesId(Integer.parseInt(saleId));
        OrderInfoVo orderInfoVo = new OrderInfoVo();
        orderInfoVo.setUserId(user.getUserId());
        orderInfoVo.setUserName(user.getNickName());
        orderInfoVo.setPhoneNumber(user.getPhonenumber());
        orderInfoVo.setEmail(user.getEmail());
        orderInfoVo.setSaleName(sales.getSalesName());
        orderInfoVo.setPrice(sales.getPrice());
        return getSuccessResponseVO(orderInfoVo);
    }

    @RequestMapping("postOrder")
    @GlobalInterceptor(checkLogin = true,checkParams = false)
    public ResponseVO postOrder(@VerifyParam(required = true)Integer salesId,@VerifyParam(required = true)Integer userId,@VerifyParam(required = true)String salesName,@VerifyParam(required = true)String userName,@VerifyParam(required = true) BigDecimal price){
        OrderInfo order = new OrderInfo();
        order.setOrderId(StringTools.getRandomNumber(25)+userId);
        order.setSalesId(salesId);
        order.setSalesName(salesName);
        order.setUserId(userId);
        order.setUserName(userName);
        order.setPrice(price);
        order.setCreateTime(new Date());
        orderInfoService.add(order);
        return getSuccessResponseVO(order);
    }
}
