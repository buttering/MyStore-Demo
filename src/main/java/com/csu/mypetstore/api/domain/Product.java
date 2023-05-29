package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
public class Product {
        @TableId(type = IdType.AUTO)
        private Integer id;
        @TableField(value = "category_id")
        private @NotNull(message = "产品类别id不能为空") Integer categoryId;

        private @NotBlank(message = "产品名不能为空") String name;
        private String subtitle;
        private String detail;
        private @NotNull(message = "价格不能为空") BigDecimal price;
        private @NotNull(message = "库存不能为空") Integer stock;
        private Integer status;

        @TableField(value = "create_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime createTime;
        @TableField(value = "update_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
//        @Version  // 乐观锁，仅在insert时手动设值，update时会自动更新
        private LocalDateTime updateTime;

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Product product = (Product) o;
                return id.equals(product.id);
        }

        @Override
        public int hashCode() {
                return Objects.hash(id);
        }

        @Override
        public String toString() {
                return "Product[" +
                        "id=" + id + ", " +
                        "categoryId=" + categoryId + ", " +
                        "name=" + name + ", " +
                        "subtitle=" + subtitle + ", " +
                        "detail=" + detail + ", " +
                        "price=" + price + ", " +
                        "stock=" + stock + ", " +
                        "status=" + status + ", " +
                        "createTime=" + createTime + ", " +
                        "updateTime=" + updateTime + ']';
        }
}