package com.github.windmill312.order.model.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class OrderProducts {

    private UUID productUid;
    private Integer quantity;

    public OrderProducts(UUID productUid, Integer quantity) {
        this.productUid = productUid;
        this.quantity = quantity;
    }

    public OrderProducts() {
    }

    @Column(name = "product_uid", nullable = false)
    public UUID getProductUid() {
        return productUid;
    }

    public void setProductUid(UUID productUid) {
        this.productUid = productUid;
    }

    @Column(name = "quantity", nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, false);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}

