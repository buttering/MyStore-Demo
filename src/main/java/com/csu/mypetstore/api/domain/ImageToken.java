package com.csu.mypetstore.api.domain;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.domain.vo.TencentCOSVO;

import java.time.LocalDateTime;

public record ImageToken (
        // TODO,存于缓存中
        String pictureId,
        TencentCOSVO token,
        CONSTANT.IMAGE_PERMISSION permission,  // 权限
        LocalDateTime createTime // 创建时间
) {}
