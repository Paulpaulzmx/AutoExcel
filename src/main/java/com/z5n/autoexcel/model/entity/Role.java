package com.z5n.autoexcel.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

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
@Table(name = "role")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

    @Id
    @Column(name = "uuid", length = 32)
    private String uuid;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private List<User> users;
    //急加载 会查询role表
    @ManyToMany(mappedBy = "roles",fetch = FetchType.EAGER)
    private List<Privilege> privileges;

}
