//package com.courseproject.cvbuilderbackendv2.entity;
//
//import jakarta.persistence.*;
//
//import java.sql.Date;
//
//@Entity
//@Table(name = "resumes")
//public class Resume {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int resumeId;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @Column(length = 45)
//    private String title;
//
//    @Column
//    private java.sql.Date date;
//
//    @Column(length = 45)
//    private String name;
//
//    @Column(length = 45)
//    private String surname;
//
//    @Column(length = 45)
//    private String email;
//
//    @Column(length = 45)
//    private String phone;
//
//    @Column(length = 100)
//    private String address;
//
//    @Column(columnDefinition = "TEXT")
//    private String about;
//
//    public Resume(){}
//
//    public Resume(int resumeId, User user, String title, Date date, String name, String surname, String email, String phone, String address, String about) {
//        this.resumeId = resumeId;
//        this.user = user;
//        this.title = title;
//        this.date = date;
//        this.name = name;
//        this.surname = surname;
//        this.email = email;
//        this.phone = phone;
//        this.address = address;
//        this.about = about;
//    }
//
//    public int getResumeId() {
//        return resumeId;
//    }
//
//    public void setResumeId(int resumeId) {
//        this.resumeId = resumeId;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getSurname() {
//        return surname;
//    }
//
//    public void setSurname(String surname) {
//        this.surname = surname;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getAbout() {
//        return about;
//    }
//
//    public void setAbout(String about) {
//        this.about = about;
//    }
//}
package com.courseproject.cvbuilderbackendv2.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.databind.JsonNode;

@Entity
@Table(name = "resumes")
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int resumeId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JdbcTypeCode(SqlTypes.JSON) // Новый способ работы с JSON в Hibernate 6+
    @Column(columnDefinition = "jsonb")
    private JsonNode resumeData;

    public Resume(){}

    public Resume(User user, JsonNode resumeData) {
        this.user = user;
        this.resumeData = resumeData;
    }

    public int getResumeId() {
        return resumeId;
    }

    public void setResumeId(int resumeId) {
        this.resumeId = resumeId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public JsonNode getResumeData() {
        return resumeData;
    }

    public void setResumeData(JsonNode resumeData) {
        this.resumeData = resumeData;
    }

}

