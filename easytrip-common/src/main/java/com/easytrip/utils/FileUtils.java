package com.easytrip.utils;

import com.easytrip.entity.Constants.Constants;
import com.easytrip.entity.config.AppConfig;
import com.easytrip.entity.dto.FileUploadDto;
import com.easytrip.entity.enums.DateTimePatternEnum;
import com.easytrip.entity.enums.ImageUploadTypeEnum;
import com.easytrip.exception.BusinessException;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;

@Component
public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    @Resource
    private AppConfig appConfig;
    @Resource
    private ImageUtils imageUtils;

    public FileUploadDto uploadFile2Local(MultipartFile file, String folder, ImageUploadTypeEnum typeEnum){
        try{
            FileUploadDto uploadDto = new FileUploadDto();
            String originalFileName =file.getOriginalFilename();
            String fileSuffix=StringTools.getFileSuffix(originalFileName);
            if(originalFileName.length()> Constants.LENGTH_200){
                originalFileName = StringTools.getFileName(originalFileName).substring(0,Constants.LENGTH_190)+fileSuffix;
            }
            if (!ArrayUtils.contains(typeEnum.getSuffixArray(),fileSuffix)){
                throw new BusinessException("文件类型不正确");
            }
            String month = DateUtil.format(new Date(), DateTimePatternEnum.YYYYMM.getPattern());
            String baseFolder = appConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE;
            File targetFileFolder = new File(baseFolder+folder+month+"/");
            String fileName = StringTools.getRandomString(Constants.LENGTH_15)+fileSuffix;

            File targetFile = new File(targetFileFolder.getPath()+"/"+fileName);
            String localPath = month+"/"+fileName;
            if (typeEnum == ImageUploadTypeEnum.AVATAR){
                targetFileFolder = new File(baseFolder+Constants.FILE_FOLDER_AVATAR_NAME);
                targetFile = new File(targetFileFolder.getPath()+"/"+folder+Constants.AVATAR_SUFFIX);
                localPath = folder+Constants.AVATAR_SUFFIX;

            }
            if (!targetFileFolder.exists()){
                targetFileFolder.mkdirs();
            }
            file.transferTo(targetFile);
            //压缩图片
            if (typeEnum==ImageUploadTypeEnum.COMMENT_IMAGE){
                String thumbnailName = targetFile.getName().replace(".","_.");
                File thumbnail = new File(targetFile.getParent()+"/"+thumbnailName);
                Boolean thumbnailCreated = imageUtils.createThumbnail(targetFile,Constants.LENGTH_200,Constants.LENGTH_200,thumbnail);
                if (!thumbnailCreated){
                    org.apache.commons.io.FileUtils.copyFile(targetFile,thumbnail);
                }
            }else if (typeEnum==ImageUploadTypeEnum.AVATAR||typeEnum==ImageUploadTypeEnum.ARTICLE_COVER){
                imageUtils.createThumbnail(targetFile,Constants.LENGTH_200,Constants.LENGTH_200,targetFile);
            }
            uploadDto.setLocalPath(localPath);
            uploadDto.setOriginalFileName(originalFileName);
            return uploadDto;
        }catch(BusinessException e){
            logger.error("文件上传失败",e);
            throw e;
        }catch (Exception e){
            logger.error("文件上传失败",e);
            throw new BusinessException("上传文件失败");
        }
    }
}
