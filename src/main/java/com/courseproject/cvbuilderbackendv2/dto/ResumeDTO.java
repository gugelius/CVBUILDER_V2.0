package com.courseproject.cvbuilderbackendv2.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class ResumeDTO {
    private int resumeId;
    private int userId;
    private String userName;
    private JsonNode resumeData;
    private boolean isPublic;

    public ResumeDTO() {}

    public ResumeDTO(int resumeId, int userId, String userName, JsonNode resumeData, boolean isPublic) {
        this.resumeId = resumeId;
        this.userId = userId;
        this.userName = userName;
        this.resumeData = resumeData;
        this.isPublic = isPublic;
    }

    public int getResumeId() { return resumeId; }
    public void setResumeId(int resumeId) { this.resumeId = resumeId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public JsonNode getResumeData() { return resumeData; }
    public void setResumeData(JsonNode resumeData) { this.resumeData = resumeData; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }
}