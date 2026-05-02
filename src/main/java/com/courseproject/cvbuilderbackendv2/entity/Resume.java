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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode resumeData;

    @Column(nullable = false)
    private boolean isPublic = false;

    public Resume(){}

    public Resume(User user, JsonNode resumeData, boolean isPublic) {
        this.user = user;
        this.resumeData = resumeData;
        this.isPublic = isPublic;
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
    public void setUser(User user) { this.user = user; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    public JsonNode getResumeData() { return resumeData; }
    public void setResumeData(JsonNode resumeData) { this.resumeData = resumeData; }

    @Override
    public String toString() {
        return "{" +
                "resumeId=" + resumeId +
                ", resumeData=" + resumeData +
                '}';
    }
}
