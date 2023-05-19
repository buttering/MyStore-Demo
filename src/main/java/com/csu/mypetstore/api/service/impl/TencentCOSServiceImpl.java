package com.csu.mypetstore.api.service.impl;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.common.ResponseCode;
import com.csu.mypetstore.api.domain.ImageToken;
import com.csu.mypetstore.api.domain.vo.TencentCOSVO;
import com.csu.mypetstore.api.service.COSService;
import com.github.benmanes.caffeine.cache.Cache;
import com.google.common.collect.Lists;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Credentials;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;

@Slf4j
@Component
public class TencentCOSServiceImpl implements COSService {
    @Value("${tencent-cloud.cos.secretId}")
    private String SECRET_ID;
    @Value("${tencent-cloud.cos.secretKey}")
    private String SECRET_KEY;
    @Value("${tencent-cloud.cos.durationSeconds}")
    private int DURATION_SECONDS;
    @Value("${tencent-cloud.cos.bucket}")
    private String BUCKET;
    @Value("${tencent-cloud.cos.region}")
    private String REGION;

    Cache<String, ImageToken> imageTokenCache;
    TencentCOSServiceImpl(Cache<String, ImageToken> imageTokenCache) {
        this.imageTokenCache = imageTokenCache;
    }

    @Override
    public CommonResponse<TencentCOSVO> generatePolicy(String imageId, CONSTANT.IMAGE_PERMISSION permission) {
        if (StringUtils.isBlank(imageId))
            return CommonResponse.createResponseForError(ResponseCode.ARGUMENT_ILLEGAL.getDescription(), ResponseCode.SUCCESS.getCode());

        String keyString = imageId + permission;
        TencentCOSVO tencentCOSVO;

        // 检查缓存和可用时间
        ImageToken imageToken = imageTokenCache.getIfPresent(keyString);
        if (imageToken != null) {
            long minutes = Duration.between(imageToken.createTime(), LocalDateTime.now()).toMinutes();
            if (minutes <= DURATION_SECONDS / 60 - 20) {  // 缓存中存在有效时间多于20分钟的token
                tencentCOSVO = imageToken.token();
                log.info("取出token缓存: {}", imageToken);
                return CommonResponse.createResponseForSuccess(tencentCOSVO);
            }
        }

        TreeMap<String, Object> config = new TreeMap<>();

        try {
            config.put("secretId", SECRET_ID);
            config.put("secretKey", SECRET_KEY);
            config.put("durationSeconds", DURATION_SECONDS);
            config.put("bucket", BUCKET);
            config.put("region", REGION);
            config.put("allowPrefixes", new String[] {imageId});  // TODO: 一次请求，根据类别获取一类图片的token，置于redis中，这些图片共用一个token
            config.put("allowActions", new String[] {permission.getPermission()});// TODO: 最小权限

            Credentials credentials = CosStsClient.getCredential(config).credentials;
            tencentCOSVO = new TencentCOSVO(credentials.tmpSecretId, credentials.tmpSecretKey, credentials.sessionToken);
        } catch (Exception e) {
            log.info("服务端生成AliOSS的policy失败.",e);
            return CommonResponse.createResponseForError();
        }

        imageToken = new ImageToken(imageId, tencentCOSVO, permission, LocalDateTime.now());
        log.info("存入token缓存: {}", imageToken);
        imageTokenCache.put(keyString, imageToken);
        return CommonResponse.createResponseForSuccess(tencentCOSVO);
    }

//    @Override
//    public CommonResponse<List<TencentCOSVO>> generatePolicy(List<String> imageIdList, CONSTANT.IMAGE_PERMISSION permission) {
//        List<TencentCOSVO> tokenList = Lists.newArrayList();
//        List<String> outOfCache = Lists.newArrayList();
//
//        imageIdList.forEach(imageId -> {
//            if (StringUtils.isBlank(imageId))
//                return;
//            String keyString = imageId + permission;
//            TencentCOSVO tencentCOSVO;
//
//            // 检查缓存和可用时间
//            ImageToken imageToken = imageTokenCache.getIfPresent(keyString);
//            if (imageToken != null) {
//                long minutes = Duration.between(imageToken.createTime(), LocalDateTime.now()).toMinutes();
//                if (minutes >= 20) {  // 缓存中存在有效时间多于20分钟的token
//                    tencentCOSVO = imageToken.token();
//                    tokenList.add(tencentCOSVO);
//                    return;
//                }
//            }
//            outOfCache.add(imageId);
//        });
//
//        TreeMap<String, Object> config = new TreeMap<>();
//
//        try {
//            config.put("secretId", SECRET_ID);
//            config.put("secretKey", SECRET_KEY);
//            config.put("durationSeconds", DURATION_SECONDS);
//            config.put("bucket", BUCKET);
//            config.put("region", REGION);
//            config.put("allowPrefixes", outOfCache.toArray());
//            config.put("allowActions", new String[] {permission.getPermission()});
//
//            Credentials credentials = CosStsClient.getCredential(config).credentials;
//            tencentCOSVO = new TencentCOSVO(credentials.tmpSecretId, credentials.tmpSecretKey, credentials.sessionToken);
//
//        } catch (Exception e) {
//            log.info("服务端生成AliOSS的policy失败.",e);
//            return CommonResponse.createResponseForError();
//        }
//
//        imageTokenCache.put(keyString, new ImageToken(imageId, tencentCOSVO, permission, LocalDateTime.now()));
//        return CommonResponse.createResponseForSuccess(tencentCOSVO);
//    }

}
