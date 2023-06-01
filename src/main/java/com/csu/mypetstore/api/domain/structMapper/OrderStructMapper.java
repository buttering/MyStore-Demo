package com.csu.mypetstore.api.domain.structMapper;

import com.csu.mypetstore.api.domain.CartItem;
import com.csu.mypetstore.api.domain.Order;
import com.csu.mypetstore.api.domain.OrderItem;
import com.csu.mypetstore.api.domain.Product;
import com.csu.mypetstore.api.domain.vo.OrderItemVO;
import com.csu.mypetstore.api.domain.vo.OrderListVO;
import com.csu.mypetstore.api.domain.vo.OrderVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderStructMapper {
    OrderStructMapper INSTANCE = Mappers.getMapper(OrderStructMapper.class);

    OrderVO order2VO(Order order);

    OrderItemVO orderItem2VO(OrderItem orderItem);

    @Mapping(source = "id", target = "productId")
    @Mapping(source = "name", target = "productName")
    @Mapping(source = "subtitle", target = "productSubtitle")
    @Mapping(source = "price", target = "currentPrice")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    OrderItem product2OrderItem(Product product);

    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.subtitle", target = "productSubtitle")
    @Mapping(source = "product.price", target = "currentPrice")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    OrderItem cartItemAndProduct2OrderItem(CartItem cartItem, Product product);

    OrderListVO order2ListVO(Order order);
}
