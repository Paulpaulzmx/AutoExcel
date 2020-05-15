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
    @Column(name = "uuid")
    private String uuid;

    /**
     * 学生信息表主键
     */
//    @Column(name = "stu_info_id", columnDefinition = "int(11) not null")
//    private Integer stuInfoId;

    /**
     * 模板表主键
     */
    @Column(name = "template_id", length = 32)
    private String templateId;

    @Column(name = "content", columnDefinition = "text not null")
    private String content;

}
