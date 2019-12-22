package com.z5n.autoexcel.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 *  学生提交信息表
 * @author z5n
 */
@Data
@Entity
@Table(name = "stu_msg")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StuMsg extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 学生信息表主键
     */
    @Column(name = "stu_info_id", columnDefinition = "int(11) not null")
    private Integer stuInfoId;

    /**
     * 模板表主键
     */
    @Column(name = "template_id", columnDefinition = "int(11) not null")
    private Integer templateId;

    @Column(name = "content", columnDefinition = "text not null")
    private String content;

}
