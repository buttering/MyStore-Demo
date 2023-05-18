package com.csu.mypetstore.api.service.impl;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.vo.TencentCOSVO;
import com.csu.mypetstore.api.service.COSService;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Credentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.TreeMap;

@Slf4j
@Component
public class TencentCOSServiceImpl implements COSService {
    @Value("${tencent-cloud.cos.secretId}")
    private String SECRET_ID;
    @Value("${tencent-cloud.cos.secretKey}")
    private String SECRET_KEY;
    @Value("${tencent-cloud.cos.durationSeconds}")
    private int DURATION_SECONDES;
    @Value("${tencent-cloud.cos.bucket}")
    private String BUCKET;
    @Value("${tencent-cloud.cos.region}")
    private String REGION;

    @Override
    public CommonResponse<TencentCOSVO> generatePolicy(String id) {
        TreeMap<String, Object> config = new TreeMap<>();
        TencentCOSVO tencentCOSVO;
        try {
            config.put("secretId", SECRET_ID);
            config.put("secretKey", SECRET_KEY);
            config.put("durationSeconds", DURATION_SECONDES);
            config.put("bucket", BUCKET);
            config.put("region", REGION);
            String [] allowPrefixes = new String[] {  // TODO: 一次请求，根据类别获取一类图片的token，置于redis中，这些图片共用一个token
                    "*"
            };
            config.put("allowPrefixes", allowPrefixes);  // TODO: 最小权限
            String[] allowActions = new String[] {
                    "name/cos:HeadObject",
                    "name/cos:GetObject",
                    "name/cos:GetBucket",
                    "name/cos:OptionsObject"
            };
            config.put("allowActions", allowActions);

            Credentials credentials = CosStsClient.getCredential(config).credentials;
            tencentCOSVO = new TencentCOSVO(credentials.tmpSecretId, credentials.tmpSecretKey, credentials.sessionToken);

        } catch (Exception e) {
            log.info("服务端生成AliOSS的policy失败.",e);
            return CommonResponse.createResponseForError();
        }

        return CommonResponse.createResponseForSuccess(tencentCOSVO);
    }
}
