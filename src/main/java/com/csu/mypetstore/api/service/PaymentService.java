package com.csu.mypetstore.api.service;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.PayInfo;
import com.csu.mypetstore.api.domain.vo.QRCodeVO;

public interface PaymentService {
    CommonResponse<QRCodeVO> tradePrecreate(Integer userId, Long orderNo);

    CommonResponse<PayInfo> checkPaymentAndUpdateOrder(Integer userId, Long orderNo);
}
