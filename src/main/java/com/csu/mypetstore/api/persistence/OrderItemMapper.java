package com.csu.mypetstore.api.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csu.mypetstore.api.domain.OrderItem;
import org.springframework.stereotype.Component;

@Component
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
