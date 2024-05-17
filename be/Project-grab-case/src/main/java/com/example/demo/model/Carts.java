package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Carts implements Serializable {

    @Serial
    private static final long serialVersionUID = 3415139118703152541L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator_carts_cart_id_seq")
    @SequenceGenerator(name = "generator_carts_cart_id_seq", sequenceName = "carts_cart_id_seq", schema = "public", allocationSize = 1)
    @Column(name = "cart_id", unique = true, nullable = false)
    private Integer cartId;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Foods foods;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    private Integer qty;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_by")
    private String createdBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", length = 29)
    private Timestamp createdAt;

    @Column(name = "modified_by")
    private String modifiedBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_time", length = 29)
    private Timestamp modifiedTime;
}



















