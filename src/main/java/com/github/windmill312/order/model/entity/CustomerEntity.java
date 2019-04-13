package com.github.windmill312.order.model.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "customer", schema = "orders")
public class CustomerEntity {
    private Integer id;
    private String name;
    private Instant birthDate;
    private UUID extId = UUID.randomUUID();

    public CustomerEntity() {
    }

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(
            schema = "orders", name = "orders.customer_id_seq",
            sequenceName = "orders.customer_id_seq", allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "orders.customer_id_seq")
    public Integer getId() {
        return id;
    }


    public CustomerEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public CustomerEntity setName(String name) {
        this.name = name;
        return this;
    }

    @Column(name = "birth_dt")
    public Instant getBirthDate() {
        return birthDate;
    }

    public CustomerEntity setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    @Type(type = "pg-uuid")
    @Column(name = "ext_id", nullable = false)
    public UUID getExtId() {
        return extId;
    }

    public CustomerEntity setExtId(UUID extId) {
        this.extId = extId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, false);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
