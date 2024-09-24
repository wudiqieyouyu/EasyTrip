package com.easytrip.controller;

import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.annotation.VerifyParam;
import com.easytrip.entity.Constants.Constants;
import com.easytrip.entity.dto.UserLoginDto;
import com.easytrip.entity.enums.CommentTopTypeEnum;
import com.easytrip.entity.enums.PageSize;
import com.easytrip.entity.enums.RecordOpTypeEnum;
import com.easytrip.entity.enums.ResponseCodeEnum;
import com.easytrip.entity.po.LikeRecord;
import com.easytrip.entity.po.TripComment;
import com.easytrip.entity.query.TripCommentQuery;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.exception.BusinessException;
import com.easytrip.service.LikeRecordService;
import com.easytrip.service.TripCommentService;
import com.easytrip.utils.StringTools;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class ArticleCommentController extends ABaseController{
        @Resource
        private TripCommentService tripCommentService;

        @Resource
        private LikeRecordService likeRecordService;

        final String ORDER_TYPE0 = "good_count desc,comment_id asc";
        final String ORDER_TYPE1 = "comment_id desc";
        @RequestMapping("/loadComment")
        @GlobalInterceptor()
        public ResponseVO loadComment(String articleId,
                                       Integer pageNo,
                                      Integer orderType,
                                      @RequestHeader(required = false,value = "token")String token){
                TripCommentQuery query = new TripCommentQuery();
                query.setArticleId(articleId);
                String orderBy = orderType==null||orderType== Constants.ZERO?ORDER_TYPE0: ORDER_TYPE1;
                query.setOrderBy("top_type desc,"+orderBy);
                query.setPageNo(pageNo);
                query.setPageSize(PageSize.SIZE50.getSize());
                UserLoginDto dto = getUserAdminFromToken(token);
                if (dto!=null){
                        query.setQueryLikeType(true);
                        query.setCurrentUserId(dto.getUserId());
                }
                query.setpCommentId(0);
                query.setLoadChildren(true);
                return getSuccessResponseVO(tripCommentService .findListByPage(query));
        }

        @RequestMapping("/doLike")
        @GlobalInterceptor(checkLogin = true)
        public ResponseVO doLike(@RequestHeader(required = false,value = "token")String token,
                                 @VerifyParam(required = true) Integer commentId){
                UserLoginDto dto = getUserAdminFromToken(token);
                String objectId = String.valueOf(commentId);
                likeRecordService.doLike(objectId, dto.getUserId(), dto.getNickName(), RecordOpTypeEnum.COMMENT_LIKE);
                LikeRecord likeRecord = likeRecordService.getLikeRecordByObjectIdAndUserIdAndOpType(objectId, dto.getUserId(), RecordOpTypeEnum.COMMENT_LIKE.getType());
                TripComment comment = tripCommentService.getTripCommentByCommentId(commentId);
                comment.setLikeType(likeRecord==null?0:1);
                return getSuccessResponseVO(comment);
        }

        @RequestMapping("/changeTopType")
        @GlobalInterceptor(checkLogin = true)
        public ResponseVO changeTopType(@RequestHeader(required = false,value = "token")String token,
                                 @VerifyParam(required = true) Integer commentId,
                                 @VerifyParam(required = true) Integer topType){
                UserLoginDto dto = getUserAdminFromToken(token);
                this.tripCommentService.changeTopType(dto.getUserId(),commentId,topType);
                return getSuccessResponseVO(null);
        }


        @RequestMapping("/postComment")
        @GlobalInterceptor(checkLogin = true)
        public ResponseVO postComment(HttpServletRequest request,
                                      @RequestHeader(required = false,value = "token")String token,
                                      @VerifyParam(required = true) String articleId,
                                      @VerifyParam(required = true) Integer pCommentId,
                                      @VerifyParam(min = 5,max = 800)String content,
                                      MultipartFile image,
                                      String replyUserId){
                if (image == null&& StringTools.isEmpty(content)){
                        throw new BusinessException(ResponseCodeEnum.CODE_600);
                }
                UserLoginDto dto = getUserAdminFromToken(token);
                content =StringTools.escapeHtml(content);
                TripComment comment = new TripComment();
                comment.setUserId(dto.getUserId());
                comment.setNickName(dto.getNickName());
                comment.setUserIpAddress(getIpAddress(getIpAddr(request)));
                comment.setpCommentId(pCommentId);
                comment.setArticleId(articleId);
                comment.setContent(content);
                comment.setReplyUserId(replyUserId);
                comment.setTopType(CommentTopTypeEnum.NO_TOP.getType());

                tripCommentService.postComment(comment,image);
                if (pCommentId!=0){
                        TripCommentQuery tripCommentQuery = new TripCommentQuery();
                        tripCommentQuery.setArticleId(articleId);
                        tripCommentQuery.setpCommentId(pCommentId);
                        tripCommentQuery.setOrderBy("comment_id asc");
                        List<TripComment> children =  tripCommentService.findListByParam(tripCommentQuery);
                        return getSuccessResponseVO(children);
                }
                return getSuccessResponseVO(comment);
        }

}
