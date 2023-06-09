package com.csu.mypetstore.api.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csu.mypetstore.api.domain.Product;
import org.springframework.stereotype.Component;

@Component
public interface ProductMapper extends BaseMapper<Product> {
}
