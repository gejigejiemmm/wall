package cn.edu.zzuli.wall.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

@Component
public class OSSClientUtil {

	public static final Logger logger = LoggerFactory.getLogger(OSSClientUtil.class);

	// Endpoint以杭州为例，其它Region请按实际情况填写。
	private String endpoint = "http://oss-cn-beijing.aliyuncs.com";
	// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录
	// https://ram.console.aliyun.com
	private String bucketName = "zzuliwall"; 

	// 文件存储目录
	private String filedir = "";
	private OSSClient ossClient;

	public OSSClientUtil() {
		ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
	}

	/**
	 * 初始化
	 */
	public void init() {
		ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
	}

	/**
	 * 销毁
	 */
	public void destory() {
		ossClient.shutdown();
	}

	/**
	 *
	 * 上传图片
	 * 
	 * @param file
	 * @return
	 */
	public String uploadImg2Oss(MultipartFile file) {
		if (file.getSize() > 1024 * 1024 * 20) {
			return "图片太大";// RestResultGenerator.createErrorResult(ResponseEnum.PHOTO_TOO_MAX);
		}
		String originalFilename = file.getOriginalFilename();
		String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
		Random random = new Random();
		String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
		try {
			InputStream inputStream = file.getInputStream();
			this.uploadFile2OSS(inputStream, name);
			return name;// RestResultGenerator.createSuccessResult(name);
		} catch (Exception e) {
			return "上传失败";// RestResultGenerator.createErrorResult(ResponseEnum.PHOTO_UPLOAD);
		}
	}

	/**
	 * 上传图片获取fileUrl
	 * 
	 * @param instream
	 * @param fileName
	 * @return
	 */
	private String uploadFile2OSS(InputStream instream, String fileName) {
		String ret = "";
		try {
			// 创建上传Object的Metadata
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(instream.available());
			objectMetadata.setCacheControl("no-cache");
			objectMetadata.setHeader("Pragma", "no-cache");
			objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
			objectMetadata.setContentDisposition("inline;filename=" + fileName);
			// 上传文件

			OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			PutObjectResult putResult = ossClient.putObject(bucketName, filedir + fileName, instream, objectMetadata);
			ret = putResult.getETag();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (instream != null) {
					instream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public static String getcontentType(String FilenameExtension) {
		if (FilenameExtension.equalsIgnoreCase(".bmp")) {
			return "image/bmp";
		}
		if (FilenameExtension.equalsIgnoreCase(".gif")) {
			return "image/gif";
		}
		if (FilenameExtension.equalsIgnoreCase(".jpeg") || FilenameExtension.equalsIgnoreCase(".jpg")
				|| FilenameExtension.equalsIgnoreCase(".png")) {
			return "image/jpeg";
		}
		if (FilenameExtension.equalsIgnoreCase(".html")) {
			return "text/html";
		}
		if (FilenameExtension.equalsIgnoreCase(".txt")) {
			return "text/plain";
		}
		if (FilenameExtension.equalsIgnoreCase(".vsd")) {
			return "application/vnd.visio";
		}
		if (FilenameExtension.equalsIgnoreCase(".pptx") || FilenameExtension.equalsIgnoreCase(".ppt")) {
			return "application/vnd.ms-powerpoint";
		}
		if (FilenameExtension.equalsIgnoreCase(".docx") || FilenameExtension.equalsIgnoreCase(".doc")) {
			return "application/msword";
		}
		if (FilenameExtension.equalsIgnoreCase(".xml")) {
			return "text/xml";
		}
		return "image/jpeg";
	}

	/**
	 * 获取图片路径
	 * 
	 * @param fileUrl
	 * @return
	 */
	public String getImgUrl(String fileUrl) {
		if (!StringUtils.isEmpty(fileUrl)) {
			String[] split = fileUrl.split("/");
			String url = this.getUrl(this.filedir + split[split.length - 1]);
//	                log.info(url);
//	                String[] spilt1 = url.split("\\?");
//	                return spilt1[0];
			return url;
		}
		return null;
	}

	/**
	 * 获得url链接
	 *
	 * @param key
	 * @return
	 */
	public String getUrl(String key) {
		// 设置URL过期时间为10年 3600l* 1000*24*365*10
		Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
		// 生成URL
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
		if (url != null) {
			return url.toString();
		}
		return null;
	}

	/**
	 * 多图片上传
	 * 
	 * @param fileList
	 * @return
	 */
	public String checkList(List<MultipartFile> fileList) {
		String fileUrl = "";
		String str = "";
		String photoUrl = "";
		for (int i = 0; i < fileList.size(); i++) {
			fileUrl = uploadImg2Oss(fileList.get(i));
			str = getImgUrl(fileUrl);
			if (i == 0) {
				photoUrl = str;
			} else {
				photoUrl += "," + str;
			}
		}
		return photoUrl.trim();
	}

	/**
	 * 单个图片上传
	 * 
	 * @param file
	 * @return
	 */
	public String checkImage(MultipartFile file) {
		String fileUrl = uploadImg2Oss(file);
		String str = getImgUrl(fileUrl);
		return str.trim();
	}
}
