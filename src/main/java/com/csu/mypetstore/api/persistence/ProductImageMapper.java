package com.csu.mypetstore.api.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csu.mypetstore.api.domain.ProductImage;
import org.springframework.stereotype.Component;

@Component
public interface ProductImageMapper extends BaseMapper<ProductImage> {
}
