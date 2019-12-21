package com.z5n.autoexcel.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "stu_msg")
@NoArgsConstructor
@AllArgsConstructor
public class StuMsg implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;             //主键

    private String stuId;

    private String stuClass;

    private String stuName;

    private String stuTel;

    private String stuChoice;

    private String note;

}
