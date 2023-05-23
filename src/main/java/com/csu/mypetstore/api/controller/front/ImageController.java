package com.csu.mypetstore.api.controller.front;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.vo.TencentCOSVO;
import com.csu.mypetstore.api.service.COSService;
import jakarta.validation.constraints.NotNull;
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
    CommonResponse<?> getImageToken(@PathVariable @NotNull String id) {
        return cosService.generatePolicy(id, CONSTANT.IMAGE_PERMISSION.GET_OBJECT);
    }
}
