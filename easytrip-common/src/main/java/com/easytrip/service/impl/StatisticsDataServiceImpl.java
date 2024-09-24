package com.easytrip.service.impl;

import com.easytrip.entity.dto.StatisticsDataDto;
import com.easytrip.entity.dto.StatisticsDataWeekDto;
import com.easytrip.entity.enums.DateTimePatternEnum;
import com.easytrip.entity.enums.StatisticsDateTypeEnum;
import com.easytrip.entity.po.PhoneCode;
import com.easytrip.entity.po.UserInfo;
import com.easytrip.entity.query.PhoneCodeQuery;
import com.easytrip.entity.query.UserInfoQuery;
import com.easytrip.mappers.PhoneCodeMapper;
import com.easytrip.mappers.UserInfoMapper;
import com.easytrip.service.StatisticsDataService;
import com.easytrip.utils.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("statisticsDataService")
public class StatisticsDataServiceImpl implements StatisticsDataService {

    @Resource
    private UserInfoMapper<UserInfo,UserInfoQuery> userInfoMapper;
    @Resource
    private PhoneCodeMapper<PhoneCode,PhoneCodeQuery> phoneCodeMapper;

    @Override
    public List<StatisticsDataDto> getAllData() {
        String preDate = DateUtil.format(DateUtil.getDayAgo(1), DateTimePatternEnum.YYYY_MM_DD.getPattern());
        List<StatisticsDataDto> dataDtoList = new ArrayList<>();
        for (StatisticsDateTypeEnum item : StatisticsDateTypeEnum.values()) {
            StatisticsDataDto dataDto = new StatisticsDataDto();
            dataDto.setStatisticsName(item.getDescription());
            switch (item) {
                case REGISTER_USER:
                    UserInfoQuery userInfoQuery = new UserInfoQuery();
                    dataDto.setCount(userInfoMapper.selectCount(userInfoQuery));
                    userInfoQuery.setJoinTimeStart(preDate);
                    userInfoQuery.setJoinTimeEnd(preDate);
                    dataDto.setPreCount(userInfoMapper.selectCount(userInfoQuery));
                    break;
                case LOGIN_USER:
                    UserInfoQuery userQuery = new UserInfoQuery();
                    dataDto.setCount(userInfoMapper.selectCount(userQuery));
                    userQuery.setLastLoginTimeStart(preDate);
                    userQuery.setLastLoginTimeEnd(preDate);
                    dataDto.setPreCount(userInfoMapper.selectCount(userQuery));
                    break;
                case PHONE_CODE_SEND:
                    PhoneCodeQuery phoneCodeQuery = new PhoneCodeQuery();
                    dataDto.setCount(phoneCodeMapper.selectCount(phoneCodeQuery));
                    phoneCodeQuery.setCreateTimeStart(preDate);
                    phoneCodeQuery.setCreateTimeEnd(preDate);
                    dataDto.setPreCount(phoneCodeMapper.selectCount(phoneCodeQuery));
                    break;
            }
            dataDtoList.add(dataDto);
        }
        return dataDtoList;
    }
    private List<String> getDays() {
        Date startDate = DateUtil.getDayAgo(7);
        Date preDate = DateUtil.getDayAgo(1);
        List<String> days = DateUtil.getBetweenDate(startDate, preDate);
        return days;
    }
    @Override
    public StatisticsDataWeekDto getAppWeekData() {
        List<String> days = getDays();

        StatisticsDataWeekDto weekDto = new StatisticsDataWeekDto();
        weekDto.setDateList(days);
        weekDto.setItemDataList(new ArrayList<>());

        StatisticsDataDto register = new StatisticsDataDto();
        register.setListData(new ArrayList<>());
        register.setStatisticsName(StatisticsDateTypeEnum.REGISTER_USER.getDescription());
        weekDto.getItemDataList().add(register);

        StatisticsDataDto login = new StatisticsDataDto();
        login.setStatisticsName(StatisticsDateTypeEnum.LOGIN_USER.getDescription());
        login.setListData(new ArrayList<>());
        weekDto.getItemDataList().add(login);

        for (String date : days) {
            UserInfoQuery userInfoQuery = new UserInfoQuery();
            userInfoQuery.setJoinTimeStart(date);
            userInfoQuery.setJoinTimeEnd(date);
            Integer registerCount = userInfoMapper.selectCount(userInfoQuery);
            register.getListData().add(registerCount);

            UserInfoQuery userQuery = new UserInfoQuery();
            userQuery.setLastLoginTimeStart(date);
            userQuery.setLastLoginTimeEnd(date);
            Integer loginCount = userInfoMapper.selectCount(userQuery);
            login.getListData().add(loginCount);

        }
        return weekDto;
    }

    @Override
    public StatisticsDataWeekDto getContentWeekData() {
        List<String> days = getDays();

        StatisticsDataWeekDto weekDto = new StatisticsDataWeekDto();
        weekDto.setDateList(days);
        weekDto.setItemDataList(new ArrayList<>());


        StatisticsDataDto phone = new StatisticsDataDto();
        phone.setStatisticsName(StatisticsDateTypeEnum.PHONE_CODE_SEND.getDescription());
        phone.setListData(new ArrayList<>());
        weekDto.getItemDataList().add(phone);

        for (String date : days) {
            PhoneCodeQuery phoneCodeQuery = new PhoneCodeQuery();
            phoneCodeQuery.setCreateTimeStart(date);
            phoneCodeQuery.setCreateTimeEnd(date);
            phone.getListData().add(phoneCodeMapper.selectCount(phoneCodeQuery));
        }
        return weekDto;
    }
}
