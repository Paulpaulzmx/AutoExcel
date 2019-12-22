package com.z5n.autoexcel.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * @program: autoexcel
 * @ClassName: Template
 * @Description:  创建的excel模板类
 * @Author: chen qi
 * @Date: 2019/12/22 8:32
 * @Version: 1.0
 **/

@Data
@Entity
@Table(name = "template")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Template extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "stu_info_id", columnDefinition = "int(11) not null")
    private Integer stuInfoId;

    @Column(name = "head_content", columnDefinition = "text not null")
    private String headContent;
}
