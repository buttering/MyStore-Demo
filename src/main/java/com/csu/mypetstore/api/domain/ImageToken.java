package com.csu.mypetstore.api.domain;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.domain.vo.TencentCOSVO;

import java.time.LocalDateTime;

public record ImageToken (
        String pictureId,
        String preSignedUrl,  // 预签名url
        CONSTANT.IMAGE_PERMISSION permission,  // 权限
        LocalDateTime createTime // 创建时间
) {}
