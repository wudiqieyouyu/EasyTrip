package com.easytrip.controller;
import com.alibaba.fastjson.JSONObject;
import com.easytrip.entity.Constants.Constants;
import com.easytrip.entity.config.AppConfig;
import com.easytrip.entity.dto.UserLoginDto;
import com.easytrip.entity.enums.ResponseCodeEnum;
import com.easytrip.entity.po.UserInfo;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.exception.BusinessException;
import com.easytrip.service.UserInfoService;
import com.easytrip.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class ABaseController {

    protected static final String STATUC_SUCCESS = "success";

    protected static final String STATUC_ERROR = "error";
    private static final Logger logger = LoggerFactory.getLogger(ABaseController.class);



    @Resource
    private JWTUtil<UserLoginDto> jwtUtil;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private AppConfig appConfig;

    protected <T> ResponseVO getSuccessResponseVO(T t) {
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.setStatus(STATUC_SUCCESS);
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVO.setData(t);
        return responseVO;
    }

    protected <T> ResponseVO getBusinessErrorResponseVO(BusinessException e, T t) {
        ResponseVO vo = new ResponseVO();
        vo.setStatus(STATUC_ERROR);
        if (e.getCode() == null) {
            vo.setCode(ResponseCodeEnum.CODE_600.getCode());
        } else {
            vo.setCode(e.getCode());
        }
        vo.setInfo(e.getMessage());
        vo.setData(t);
        return vo;
    }

    protected <T> ResponseVO getServerErrorResponseVO(T t) {
        ResponseVO vo = new ResponseVO();
        vo.setStatus(STATUC_ERROR);
        vo.setCode(ResponseCodeEnum.CODE_500.getCode());
        vo.setInfo(ResponseCodeEnum.CODE_500.getMsg());
        vo.setData(t);
        return vo;
    }
    protected <S,T> PaginationResultVO<T> convert2PaginationVo(PaginationResultVO<S> result, Class<T> classz){
        PaginationResultVO<T> resultVO = new PaginationResultVO<>();
        resultVO.setList(CopyTools.copyList(result.getList(),classz));
        resultVO.setPageNo(result.getPageNo());
        resultVO.setPageSize(result.getPageSize());
        resultVO.setPageTotal(result.getPageTotal());
        resultVO.setTotalCount(result.getTotalCount());
        return resultVO;
    }
    protected String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public String getIpAddress(String ip){
        try{
            String url = "http://whois.pconline.com.cn/ipJson.jsp?json=true&ip="+ip;
            String reponseJson = OKHttpUtils.getRequest(url);
            if (null == reponseJson){
                return Constants.NO_ADDRESS;
            }
            Map<String,String> addressInfo = JsonUtils.convertJson2Obj(reponseJson,Map.class);
            return addressInfo.get("pro");
        }catch (Exception e){
            logger.error("获取ip地址失败",e);
        }
        return  Constants.NO_ADDRESS;
    }

    protected UserLoginDto getUserAdminFromToken(String token) {
        return jwtUtil.getTokenData(Constants.JWT_KEY_LOGIN_TOKEN, token, UserLoginDto.class);
    }
    protected Boolean isSuperAdmin(String userId){
        UserInfo userInfo= userInfoService.getUserInfoByUserId(userId);
        String superPhones = appConfig.getSuperAdminPhones();
        List<String> stringList = Arrays.asList(superPhones.split(","));
        return stringList.contains(userInfo.getPhonenumber());
    }

    protected String getAddressNameByGeocoder(String location){
        String jsonString = sendGet(getRegeoUrl(location));
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        String addressName = jsonObject.getJSONObject("regeocode").getString("formatted_address");
        return addressName;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * param url
     *            发送请求的URL
     * return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            return null;
        } finally { // 使用finally块来关闭输入流
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 逆地理编码url
     *
     * param location
     *            经纬度坐标,经度在前,纬度在后，经纬度间以“,”分割
     * return
     */
    public static String getRegeoUrl(String location) {
        return "http://restapi.amap.com/v3/geocode/regeo?location=" + location
                + "&key=17479d86c0c6a0305024e1142351a0a4";
    }
    protected void readFile(HttpServletResponse response, String filePath){
        if(!StringTools.pathIsOk(filePath)){
            return;
        }
        OutputStream out = null;
        FileInputStream in = null;
        try{
            File file = new File(filePath);
            if(!file.exists()){
                return;
            }
            in = new FileInputStream(file);
            byte[] byteData = new byte[1024];
            out = response.getOutputStream();
            int len = 0;
            while((len = in.read(byteData)) != -1){
                out.write(byteData,0,len);
            }
            out.flush();
        }catch (Exception e){
            logger.error("读取文件异常",e);
        }finally {
            if (out != null){
                try {
                    out.close();
                }catch (IOException e){
                    logger.error("IO异常",e);
                }
            }
            if (in != null){
                try{
                    in.close();
                }catch (IOException e){
                    logger.error("IO异常",e);
                }
            }
        }
    }
}
