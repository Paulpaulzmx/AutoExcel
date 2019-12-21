package com.z5n.autoexcel.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="stu_msg")
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

    public StuMsg() {
    }

    @Override
    public String toString() {
        return "StuMsg{" +
                "id=" + id +
                ", stuId='" + stuId + '\'' +
                ", stuClass='" + stuClass + '\'' +
                ", stuName='" + stuName + '\'' +
                ", stuTel='" + stuTel + '\'' +
                ", stuChoice='" + stuChoice + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuClass() {
        return stuClass;
    }

    public void setStuClass(String stuClass) {
        this.stuClass = stuClass;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuTel() {
        return stuTel;
    }

    public void setStuTel(String stuTel) {
        this.stuTel = stuTel;
    }

    public String getStuChoice() {
        return stuChoice;
    }

    public void setStuChoice(String stuChoice) {
        this.stuChoice = stuChoice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
