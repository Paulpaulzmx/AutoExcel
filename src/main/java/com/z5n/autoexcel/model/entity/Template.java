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

    @Id()
    @Column(name = "uuid", length = 32)
    private String uuid;

    @Column(name = "uploader_id", length = 32, columnDefinition = " not null")
    private String uploaderId;

    @Column(name = "file_name",length = 255, columnDefinition = " not null")
    private String fileName;

    @Column(name = "head_content", columnDefinition = "text not null")
    private String headContent;
}
