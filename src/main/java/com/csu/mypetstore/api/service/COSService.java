package com.csu.mypetstore.api.service;

import com.csu.mypetstore.api.common.CommonResponse;

public interface COSService {
    CommonResponse<?> generatePolicy(String id);
}
