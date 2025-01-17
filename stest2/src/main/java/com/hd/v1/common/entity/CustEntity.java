package com.hd.v1.common.entity;

import com.hd.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Table(name="db_cust")
@NoArgsConstructor
@ToString(callSuper = true)
public class CustEntity extends BaseEntity {
    @Id
    @Column(name = "cust_id", length = 30)
    private String id;
    @Column(nullable = false,columnDefinition = "varchar(60)")
    private String pwd;
    @Column(nullable = false,columnDefinition = "varchar(30)")
    private String name;

    @Builder
    public CustEntity(String id, String pwd, String name) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
    }
}
