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
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Orders implements Serializable {
    @Serial
    private static final long serialVersionUID = 8943404997604566009L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", unique = true, nullable = false)
    private Integer orderId;

    @Column(name = "pax_id_gsi")
    private Integer paxIdGsi;

    @Column(name = "order_date")
    private Date orderDate;

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

    @Column(name = "pax_id")
    private String paxId;

    private String state;

    @Column(name = "total_item")
    private Integer totalItem;

    @Column(name = "total_order_price")
    private Integer totalOrderPrice;

    @OneToMany(mappedBy = "orders")
    private Set<Carts> cartses;

    @OneToMany(mappedBy = "orders")
    private Set<Pax> paxs;
}



















