package com.csu.mypetstore.api.controller.front;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.vo.TencentCOSVO;
import com.csu.mypetstore.api.service.COSService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    COSService cosService;

    public ImageController(COSService cosService) {
        this.cosService = cosService;
    }

    @GetMapping("api/image/{id}")
    CommonResponse<?> getImageToken(@PathVariable String id) {
        // TODO: 流量管理
        return cosService.generatePolicy(id);
    }
}
