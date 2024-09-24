package com.easytrip.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.plaf.synth.Region;

import com.easytrip.entity.Constants.Constants;
import com.easytrip.entity.dto.FileUploadDto;
import com.easytrip.entity.enums.*;
import com.easytrip.entity.po.Regions;
import com.easytrip.exception.BusinessException;
import com.easytrip.service.RegionsService;
import com.easytrip.utils.FileUtils;
import com.easytrip.utils.ImageUtils;
import org.springframework.stereotype.Service;

import com.easytrip.entity.query.TripArticleQuery;
import com.easytrip.entity.po.TripArticle;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.query.SimplePage;
import com.easytrip.mappers.TripArticleMapper;
import com.easytrip.service.TripArticleService;
import com.easytrip.utils.StringTools;
import org.springframework.web.multipart.MultipartFile;


/**
 * 文章信息 业务接口实现
 */
@Service("tripArticleService")
public class TripArticleServiceImpl implements TripArticleService {

	@Resource
	private TripArticleMapper<TripArticle, TripArticleQuery> tripArticleMapper;

	@Resource
	private ImageUtils imageUtils;

	@Resource
	private RegionsService regionsService;

	@Resource
	private FileUtils fileUtils;
	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<TripArticle> findListByParam(TripArticleQuery param) {
		return this.tripArticleMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(TripArticleQuery param) {
		return this.tripArticleMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<TripArticle> findListByPage(TripArticleQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<TripArticle> list = this.findListByParam(param);
		PaginationResultVO<TripArticle> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(TripArticle bean) {
		return this.tripArticleMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<TripArticle> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.tripArticleMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<TripArticle> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.tripArticleMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(TripArticle bean, TripArticleQuery param) {
		StringTools.checkParam(param);
		return this.tripArticleMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(TripArticleQuery param) {
		StringTools.checkParam(param);
		return this.tripArticleMapper.deleteByParam(param);
	}

	/**
	 * 根据ArticleId获取对象
	 */
	@Override
	public TripArticle getTripArticleByArticleId(String articleId) {
		return this.tripArticleMapper.selectByArticleId(articleId);
	}

	/**
	 * 根据ArticleId修改
	 */
	@Override
	public Integer updateTripArticleByArticleId(TripArticle bean, String articleId) {
		return this.tripArticleMapper.updateByArticleId(bean, articleId);
	}

	/**
	 * 根据ArticleId删除
	 */
	@Override
	public Integer deleteTripArticleByArticleId(String articleId) {
		return this.tripArticleMapper.deleteByArticleId(articleId);
	}

	@Override
	public void saveArticle(TripArticle article, Boolean admin) {
		if (null == article.getArticleId()) {
			article.setPostTime(new Date());
			this.tripArticleMapper.insert(article);
		} else {
			TripArticle dbInfo = this.tripArticleMapper.selectByArticleId(article.getArticleId());
			if (!dbInfo.getUserId().equals(article.getUserId()) && !admin) {
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
			article.setUserId(null);
			article.setNickName(null);
			article.setPostTime(null);
			this.tripArticleMapper.updateByArticleId(article, article.getArticleId());
		}
	}

	@Override
	public TripArticle readArticle(String articleId) {
		TripArticle article = this.tripArticleMapper.selectByArticleId(articleId);
		if (article==null){
			throw new BusinessException(ResponseCodeEnum.CODE_404);
		}
		if (TripArticleStatusEnum.ENABLE.getStatus().equals(article.getStatus())){
			this.tripArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.READ_COUNT.getType(), Constants.ONE,articleId);
		}
		return article;
	}

	@Override
	public void postArticle(TripArticle article, MultipartFile cover) {
			checkArticle(article);
			String articleId = StringTools.getRandomString(Constants.LENGTH_15);
			Date curDate = new Date();
			article.setArticleId(articleId);
			article.setPostTime(curDate);
			article.setLastUpdateTime(curDate);
			article.setStatus(Constants.ONE);
			if (cover!=null){
				FileUploadDto fileUploadDto  = fileUtils.uploadFile2Local(cover,Constants.FILE_FOLDER_IMAGE,ImageUploadTypeEnum.ARTICLE_COVER);
				article.setCover(fileUploadDto.getLocalPath());
			}

			//替换图片
			String content = article.getContent();
			if (!StringTools.isEmpty(content)){
				String month =imageUtils.resetImageHtml(content);
				String replaceMonth = "/"+month+"/";
				content = content.replace(Constants.FILE_FOLDER_TEMP,replaceMonth);
				article.setContent(content);
				String markdownContent =article.getMarkdownContent();
				if (!StringTools.isEmpty(markdownContent)){
					markdownContent = markdownContent.replace(Constants.FILE_FOLDER_TEMP,replaceMonth);
					article.setMarkdownContent(markdownContent);
				}
			}
			this.tripArticleMapper.insert(article);
	}


	@Override
	public void updateArticle(TripArticle article, MultipartFile cover) {
			TripArticle dbInfo = tripArticleMapper.selectByArticleId(article.getArticleId());
			if (!dbInfo.getUserId().equals(article.getUserId())){
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
			article.setLastUpdateTime(new Date());
			checkArticle(article);
			if (cover!=null){
				FileUploadDto fileUploadDto  = fileUtils.uploadFile2Local(cover,Constants.FILE_FOLDER_IMAGE,ImageUploadTypeEnum.ARTICLE_COVER);
				article.setCover(fileUploadDto.getLocalPath());
			}
		//替换图片
		String content = article.getContent();
		if (!StringTools.isEmpty(content)){
			String month =imageUtils.resetImageHtml(content);
			String replaceMonth = "/"+month+"/";
			content = content.replace(Constants.FILE_FOLDER_TEMP,replaceMonth);
			article.setContent(content);
			String markdownContent =article.getMarkdownContent();
			if (!StringTools.isEmpty(markdownContent)){
				markdownContent = markdownContent.replace(Constants.FILE_FOLDER_TEMP,replaceMonth);
				article.setMarkdownContent(markdownContent);
			}
		}
		this.tripArticleMapper.updateByArticleId(article,article.getArticleId());
	}

	private void checkArticle(TripArticle article){
		Regions pRegion = regionsService.getRegionsByRegionId(article.getpBoardId());
		if (pRegion==null){
			throw new BusinessException("一级板块不存在");
		}
		article.setpBoardName(pRegion.getRegionName());
		if (article.getBoardId()!=null&&article.getBoardId()!=0){
			Regions region = regionsService.getRegionsByRegionId(article.getBoardId());
			if (region==null){
				throw new BusinessException("二级板块不存在");
			}
			article.setBoardName(region.getRegionName());
		}else {
			article.setBoardId(0);
			article.setBoardName("");
		}
	}
}