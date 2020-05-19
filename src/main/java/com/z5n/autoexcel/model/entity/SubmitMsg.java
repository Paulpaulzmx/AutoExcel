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
@Table(name = "submit_msg")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SubmitMsg extends BaseEntity {

    @Id
    @Column(name = "uuid")
    private String uuid;

    /**
     * 模板表主键
     */
    @Column(name = "excel_id", length = 32)
    private String excelId;

    @Column(name = "filler_id", length = 32)
    private String fillerId;

    @Column(name = "content", columnDefinition = "text not null")
    private String content;

}
