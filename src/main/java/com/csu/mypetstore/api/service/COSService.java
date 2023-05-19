package com.csu.mypetstore.api.service;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.vo.TencentCOSVO;

import java.util.List;

public interface COSService {

    CommonResponse<TencentCOSVO> generatePolicy(String imageId, CONSTANT.IMAGE_PERMISSION permission);

//    CommonResponse<List<TencentCOSVO>> generatePolicy(List<String> imageIdList, CONSTANT.IMAGE_PERMISSION permission);
}
