package com.z5n.autoexcel.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "user_info")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserInfo extends BaseEntity{
    @Id
    @Column(name = "uuid", length = 32)
    private String uuid;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "stu_num")
    private String stuNum;

    @Column(name = "tel")
    private String tel;

    @Column(name = "address")
    private String address;

    @Column(name = "id_number")
    private String idNum;

    @Column(name = "qq")
    private String qq;

}
