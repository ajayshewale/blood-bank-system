package com.indorse.blood.bank.model;

import com.indorse.blood.bank.model.constant.FieldNames;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = FieldNames.CREATED_DATE, updatable = false)
    private Date createdDate;

    @CreatedBy
    @Column(name = FieldNames.CREATED_BY, updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = FieldNames.UPDATED_DATE)
    private Date updatedDate;

    @LastModifiedBy
    @Column(name = FieldNames.UPDATED_BY)
    private String updatedBy;

    @Column
    private Boolean markForDelete = false;

}
