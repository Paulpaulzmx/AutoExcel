package com.z5n.autoexcel.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @program: autoexcel
 * @ClassName: Excel
 * @Description:  创建的excel模板类
 * @Author: chen qi
 * @Date: 2019/12/22 8:32
 * @Version: 1.0
 **/

@Data
@Entity
@Table(name = "privilege")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Privilege extends BaseEntity {

    @Id
    @Column(name = "uuid", length = 32)
    private String uuid;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_privilege",
            joinColumns = {@JoinColumn(name = "privilege_id", referencedColumnName = "uuid")},
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "uuid"))
    private List<Role> roles;

}
