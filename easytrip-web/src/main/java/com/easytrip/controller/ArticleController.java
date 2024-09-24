package com.easytrip.controller;

import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.annotation.VerifyParam;
import com.easytrip.entity.dto.UserLoginDto;
import com.easytrip.entity.enums.EditorTypeEnum;
import com.easytrip.entity.enums.RecordOpTypeEnum;
import com.easytrip.entity.enums.ResponseCodeEnum;
import com.easytrip.entity.enums.TripArticleStatusEnum;
import com.easytrip.entity.po.LikeRecord;
import com.easytrip.entity.po.TripArticle;
import com.easytrip.entity.po.WebCarousel;
import com.easytrip.entity.query.RegionsQuery;
import com.easytrip.entity.query.TripArticleQuery;
import com.easytrip.entity.query.WebCarouselQuery;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.entity.vo.TripArticle2CarouselVo;
import com.easytrip.entity.vo.TripArticleDetailVo;
import com.easytrip.entity.vo.TripArticleVo;
import com.easytrip.exception.BusinessException;
import com.easytrip.service.LikeRecordService;
import com.easytrip.service.RegionsService;
import com.easytrip.service.TripArticleService;
import com.easytrip.service.WebCarouselService;
import com.easytrip.utils.CopyTools;
import com.easytrip.utils.StringTools;
import com.mysql.cj.Session;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController("articleController")
@RequestMapping("/article")
public class ArticleController extends ABaseController{


    @Resource
    private WebCarouselService webCarouselService;
    @Resource
    private TripArticleService tripArticleService;

    @Resource
    private LikeRecordService likeRecordService;

    @Resource
    private RegionsService regionsService;

    @RequestMapping("/getArticleDetail")
    @GlobalInterceptor()
    public ResponseVO getArticleDetail(@RequestHeader(value = "token",required = false)String token, @VerifyParam(required = true) String articleId){
        UserLoginDto userLoginDto = getUserAdminFromToken(token);
        TripArticle article = tripArticleService.readArticle(articleId);
        if (TripArticleStatusEnum.DISABLE.getStatus().equals(article.getStatus())&&(userLoginDto==null||!article.getUserId().equals(userLoginDto.getUserId()))||TripArticleStatusEnum.DEL.getStatus().equals(article.getStatus())){
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        TripArticleDetailVo detailVo = new TripArticleDetailVo();
        detailVo.setTripArticleVo(CopyTools.copy(article,TripArticleVo.class));
        //是否已点赞
        if (userLoginDto!=null){
            LikeRecord likeRecord = likeRecordService.getLikeRecordByObjectIdAndUserIdAndOpType(articleId,userLoginDto.getUserId(), RecordOpTypeEnum.ARTICLE_LIKE.getType());
            if(likeRecord!=null){
                detailVo.setHaveLike(true);
            }
        }
        return getSuccessResponseVO(detailVo);
        }

    @RequestMapping("/doLike")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO doLike(@RequestHeader("token")String token, @VerifyParam(required = true) String articleId){
        UserLoginDto userLoginDto = getUserAdminFromToken(token);
        likeRecordService.doLike(articleId,userLoginDto.getUserId(),userLoginDto.getNickName(),RecordOpTypeEnum.ARTICLE_LIKE);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadRegion4Post")
    @GlobalInterceptor(checkLogin = false)
    public ResponseVO loadRegion4Post(){
        RegionsQuery query = new RegionsQuery();
        query.setTree(true);
        return getSuccessResponseVO( regionsService.findListByParam(query));
    }

    @RequestMapping("/postArticle")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO postArticle(HttpServletRequest request,
                                  @RequestHeader(required = false,value = "token")String token,
                                  MultipartFile cover,
                                  @VerifyParam(required = true,max = 150)String title,
                                  @VerifyParam(required = true)Integer pRegionId,
                                  @VerifyParam() Integer regionId,
                                  @VerifyParam(max = 200) String summary,
                                  @VerifyParam(required = true)Integer editorType,
                                  @VerifyParam(required = true)String content,
                                  String markdownContent
                                  ){
        title= StringTools.escapeHtml(title);
        UserLoginDto dto = getUserAdminFromToken(token);
        TripArticle article = new TripArticle();
        article.setpBoardId(pRegionId);
        article.setBoardId(regionId);
        article.setTitle(title);
        article.setSummary(summary);
        article.setContent(content);

        EditorTypeEnum typeEnum = EditorTypeEnum.getByType(editorType);
        if (typeEnum==null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (EditorTypeEnum.MARKDOWN.getType().equals(editorType)&&StringTools.isEmpty(markdownContent)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        article.setMarkdownContent(markdownContent);
        article.setEditorType(editorType);
        article.setUserId(dto.getUserId());
        article.setNickName(dto.getNickName());
        article.setUserIpAddress(getIpAddress(getIpAddr(request)));
        this.tripArticleService.postArticle(article,cover);
        return getSuccessResponseVO( article.getArticleId());
    }

    @RequestMapping("/articleDetail4Update")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO articleDetail4Update(HttpServletRequest request,
                                  @RequestHeader(required = false,value = "token")String token,
                                  @VerifyParam(required = true) String articleId
    ){
        UserLoginDto dto = getUserAdminFromToken(token);
        TripArticle article = tripArticleService.getTripArticleByArticleId(articleId);
        if (article==null||!article.getUserId().equals(dto.getUserId())){
            throw new BusinessException("文章不存在或者你无权编辑该文章");
        }
        TripArticleDetailVo detailVo = new TripArticleDetailVo();
        TripArticleVo tripArticleVo =CopyTools.copy(article,TripArticleVo.class);
        tripArticleVo.setRegionId(tripArticleVo.getBoardId());
        tripArticleVo.setRegionName(tripArticleVo.getBoardName());
        tripArticleVo.setpRegionId(tripArticleVo.getpBoardId());
        tripArticleVo.setpRegionName(tripArticleVo.getpBoardName());
        detailVo.setTripArticleVo(tripArticleVo);
        return getSuccessResponseVO(detailVo);
    }

    @RequestMapping("/updateArticle")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO updateArticle(HttpServletRequest request,
                                  @RequestHeader(required = false,value = "token")String token,
                                  @VerifyParam(required = true)String articleId,
                                  MultipartFile cover,
                                  @VerifyParam(required = true,max = 150)String title,
                                  @VerifyParam(required = true)Integer pRegionId,
                                  @VerifyParam() Integer regionId,
                                  @VerifyParam(max = 200) String summary,
                                  @VerifyParam(required = true)Integer editorType,
                                  @VerifyParam(required = true)String content,
                                  String markdownContent
    ){
        title= StringTools.escapeHtml(title);
        UserLoginDto dto = getUserAdminFromToken(token);
        TripArticle article = new TripArticle();
        article.setArticleId(articleId);
        article.setpBoardId(pRegionId);
        article.setBoardId(regionId);
        article.setTitle(title);
        article.setSummary(summary);
        article.setContent(content);

        EditorTypeEnum typeEnum = EditorTypeEnum.getByType(editorType);
        if (typeEnum==null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (EditorTypeEnum.MARKDOWN.getType().equals(editorType)&&StringTools.isEmpty(markdownContent)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        article.setMarkdownContent(markdownContent);
        article.setEditorType(editorType);
        article.setUserId(dto.getUserId());
        article.setNickName(dto.getNickName());
        article.setUserIpAddress(getIpAddress(getIpAddr(request)));
        this.tripArticleService.updateArticle(article,cover);
        return getSuccessResponseVO( article.getArticleId());
    }


    @RequestMapping("/loadCarousel")
    @GlobalInterceptor
    public ResponseVO loadCarousel() {
        TripArticleQuery query =new TripArticleQuery();
        query.setOrderBy("read_count desc");
        List<TripArticle> carouselList =tripArticleService.findListByParam(query);
        List<TripArticle2CarouselVo> result = new ArrayList<>();
        for (TripArticle item:carouselList){
            TripArticle2CarouselVo vo= new TripArticle2CarouselVo();
            vo.setImgPath(item.getCover());
            vo.setObjectType(0);
            vo.setObjectId(item.getArticleId());
            result.add(vo);
        }
        return getSuccessResponseVO(result);
    }
}
