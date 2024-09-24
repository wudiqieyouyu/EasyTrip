package com.easytrip.controller;

import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.annotation.VerifyParam;
import com.easytrip.entity.enums.PermissionCodeEnum;
import com.easytrip.entity.po.TripBoard;
import com.easytrip.entity.query.TripBoardQuery;
import com.easytrip.entity.query.WebCarouselQuery;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.service.TripBoardService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController("boardController")
@RequestMapping("/board")
public class BoardController extends ABaseController{
    @Resource
    private TripBoardService tripBoardService;

    /**
     * 根据条件分页查询
     */
    @RequestMapping("/loadDataList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.BOARD_LIST)
    public ResponseVO loadDataList(TripBoardQuery query) {
        query.setOrderBy("sort asc");
        return getSuccessResponseVO(tripBoardService.findListByParam(query));
    }
    @RequestMapping("/saveBoard")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.BOARD_EDIT)
    public ResponseVO saveCategory(TripBoard tripBoard) {
        tripBoardService.saveBoard(tripBoard);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/delBoard")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.BOARD_DEL)
    public ResponseVO delBoard(@VerifyParam(required = true) Integer boardId) {
        tripBoardService.deleteTripBoardByBoardId(boardId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/changeSort")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.BOARD_EDIT)
    public ResponseVO changeSort(@VerifyParam(required = true) String boardIds) {
        tripBoardService.changeSort(boardIds);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadAllCategory4Select")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.BOARD_LIST)
    public ResponseVO loadAllCategoryByType(@VerifyParam(required = true) Integer type) {
        List<TripBoard> list = tripBoardService.loadAllTripBoardByType(type);
        return getSuccessResponseVO(list);
    }
}
