package com.csu.mypetstore.api.service.impl;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.common.ResponseCode;
import com.csu.mypetstore.api.domain.ImageToken;
import com.csu.mypetstore.api.domain.vo.TencentCOSVO;
import com.csu.mypetstore.api.service.COSService;
import com.github.benmanes.caffeine.cache.Cache;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.http.HttpMethodName;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
public class TencentCOSServiceImpl implements COSService {
//    @Value("${tencent-cloud.cos.secretId}")
//    private String SECRET_ID;
//    @Value("${tencent-cloud.cos.secretKey}")
//    private String SECRET_KEY;
//    @Value("${tencent-cloud.cos.bucket}")
//    private String BUCKET;
//    @Value("${tencent-cloud.cos.region}")
//    private String REGION;
//    @Value("${tencent-cloud.cos.serverURL}")
//    private String serverURL;
    @Value("${tencentCOS.durationSeconds}")
    private int DURATION_SECONDS;
    @Value("${tencentCOS.bucketName}")
    String BUCKET_NAME;

    private final Cache<String, ImageToken> imageTokenCache;
    private final COSClient cosClient;

    TencentCOSServiceImpl(Cache<String, ImageToken> imageTokenCache, COSClient cosClient) {
        this.imageTokenCache = imageTokenCache;
        this.cosClient = cosClient;
    }

    @Override
    // 生成临时密钥
    public CommonResponse<TencentCOSVO> generatePolicy(String imageId, CONSTANT.IMAGE_PERMISSION permission) {
//
//        if (StringUtils.isBlank(imageId))
//            return CommonResponse.createResponseForError(ResponseCode.ARGUMENT_ILLEGAL.getDescription(), ResponseCode.SUCCESS.getCode());
//
//        String keyString = imageId + permission;
//        TencentCOSVO tencentCOSVO;
//
//        // 检查缓存和可用时间
//        ImageToken imageToken = imageTokenCache.getIfPresent(keyString);
//        if (imageToken != null) {
//            long minutes = Duration.between(imageToken.createTime(), LocalDateTime.now()).toMinutes();
//            if (minutes <= DURATION_SECONDS / 60 - 20) {  // 缓存中存在有效时间多于20分钟的token
//                tencentCOSVO = imageToken.token();
//                log.info("取出token缓存: {}", imageToken);
//                return CommonResponse.createResponseForSuccess(tencentCOSVO);
//            }
//        }
//
//        TreeMap<String, Object> config = new TreeMap<>();
//
//        try {
//            config.put("secretId", SECRET_ID);
//            config.put("secretKey", SECRET_KEY);
//            config.put("durationSeconds", DURATION_SECONDS);
//            config.put("bucket", BUCKET);
//            config.put("region", REGION);
//            config.put("allowPrefixes", new String[]{imageId});  // TODO: 一次请求，根据类别获取一类图片的token，置于redis中，这些图片共用一个token
//            config.put("allowActions", new String[]{permission.getPermission()});// TODO: 最小权限
//
//
//            Credentials credentials = CosStsClient.getCredential(config).credentials;
//            tencentCOSVO = new TencentCOSVO(credentials.tmpSecretId, credentials.tmpSecretKey, null, credentials.sessionToken, serverURL);
//        } catch (Exception e) {
//            log.info("服务端生成AliOSS的policy失败.", e);
//            return CommonResponse.createResponseForError();
//        }
//
//        imageToken = new ImageToken(imageId, tencentCOSVO, permission, LocalDateTime.now());
//        log.info("存入token缓存: {}", imageToken);
//        imageTokenCache.put(keyString, imageToken);
//        return CommonResponse.createResponseForSuccess(tencentCOSVO);
        return null;
    }

    @Override
    // 生成预签名url
    public CommonResponse<String> generateURL(String imageId, CONSTANT.IMAGE_PERMISSION permission) {
        if (StringUtils.isBlank(imageId))
            return CommonResponse.createResponseForError(ResponseCode.ARGUMENT_ILLEGAL.getDescription(), ResponseCode.SUCCESS.getCode());

        String keyString = imageId + permission;
        String preSignedUrl;

        // 检查缓存和可用时间
        ImageToken imageToken = imageTokenCache.getIfPresent(keyString);
        if (imageToken != null) {
            long minutes = Duration.between(imageToken.createTime(), LocalDateTime.now()).toMinutes();
            if (minutes <= DURATION_SECONDS / 60 - 20) {  // 缓存中存在有效时间多于20分钟的token
                preSignedUrl = imageToken.preSignedUrl();
                log.info("取出token缓存: {}", imageToken);
                return CommonResponse.createResponseForSuccess(preSignedUrl);
            }
        }


        Date expirationDate = Date.from(LocalDateTime.now().plusMinutes(DURATION_SECONDS).atZone(ZoneId.systemDefault()).toInstant());
        URL url = cosClient.generatePresignedUrl(BUCKET_NAME, imageId, expirationDate, HttpMethodName.GET);

        imageToken = new ImageToken(imageId, url.toString(), permission, LocalDateTime.now());
        log.info("存入token缓存: {}", imageToken);
        imageTokenCache.put(keyString, imageToken);
        return CommonResponse.createResponseForSuccess(url.toString());
    }

}