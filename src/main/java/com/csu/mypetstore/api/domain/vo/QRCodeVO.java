package com.csu.mypetstore.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record QRCodeVO(
        @JsonProperty("order_no")
        Long orderNo,
        @JsonProperty("qr_code")
        String qrCode
) { }
