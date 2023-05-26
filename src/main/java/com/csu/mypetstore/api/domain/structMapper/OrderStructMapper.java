package com.csu.mypetstore.api.domain.structMapper;

import com.csu.mypetstore.api.domain.Order;
import com.csu.mypetstore.api.domain.OrderItem;
import com.csu.mypetstore.api.domain.vo.OrderItemVO;
import com.csu.mypetstore.api.domain.vo.OrderVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderStructMapper {
    OrderStructMapper INSTANCE = Mappers.getMapper(OrderStructMapper.class);

    OrderVO order2VO(Order order);

    OrderItemVO orderItem2VO(OrderItem orderItem);
}
