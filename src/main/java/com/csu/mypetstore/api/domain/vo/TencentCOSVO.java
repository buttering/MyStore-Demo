package com.csu.mypetstore.api.domain.vo;


import lombok.Builder;

public record TencentCOSVO(
    String tmpSecretId,
    String tmpSecretKey,
    String sessionToken,
    String presignedUrl,
    String serverURL
) {}
