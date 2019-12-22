package com.z5n.autoexcel.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * @program: autoexcel
 * @ClassName: StuInfo
 * @Description: 学生个人信息表
 * @Author: chen qi
 * @Date: 2019/12/22 8:40
 * @Version: 1.0
 **/
@Data
@Entity
@Table(name = "stu_info")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StuInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     *  学号
     */
    @Column(name = "stu_id", columnDefinition = "varchar(50) not null")
    private String stuId;

    @Column(name = "classes", columnDefinition = "varchar(127) not null")
    private String classes;

    /**
     * 专业
     */
    @Column(name = "profession", columnDefinition = "varchar(127) not null")
    private String profession;

    /**
     * 院系
     */
    @Column(name = "faculty", columnDefinition = "varchar(127) not null")
    private String faculty;

    /**
     * User email.
     */
    @Column(name = "email", columnDefinition = "varchar(127) not null")
    private String email;

    @Column(name = "name", columnDefinition = "varchar(127) not null")
    private String name;

    @Column(name = "tel", columnDefinition = "varchar(50) not null")
    private String tel;

}
