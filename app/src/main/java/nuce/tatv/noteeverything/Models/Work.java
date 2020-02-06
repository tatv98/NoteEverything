package nuce.tatv.noteeverything.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Work {
    @SerializedName("work_id")
    @Expose
    private Integer workId;
    @SerializedName("work_title")
    @Expose
    private String workTitle;
    @SerializedName("work_place")
    @Expose
    private String workPlace;
    @SerializedName("work_deadline")
    @Expose
    private String workDeadline;
    @SerializedName("work_status")
    @Expose
    private String workStatus;
    @SerializedName("user_name")
    @Expose
    private String userName;

    public Work(String workTitle, String workPlace, String workDeadline, String workStatus, String userName) {
        this.workTitle = workTitle;
        this.workPlace = workPlace;
        this.workDeadline = workDeadline;
        this.workStatus = workStatus;
        this.userName = userName;
    }

    public Work(Integer workId, String workTitle, String workPlace, String workDeadline, String workStatus, String userName) {
        this.workId = workId;
        this.workTitle = workTitle;
        this.workPlace = workPlace;
        this.workDeadline = workDeadline;
        this.workStatus = workStatus;
        this.userName = userName;
    }

    public Integer getWorkId() {
        return workId;
    }

    public void setWorkId(Integer workId) {
        this.workId = workId;
    }

    public String getWorkTitle() {
        return workTitle;
    }

    public void setWorkTitle(String workTitle) {
        this.workTitle = workTitle;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getWorkDeadline() {
        return workDeadline;
    }

    public void setWorkDeadline(String workDeadline) {
        this.workDeadline = workDeadline;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Work{" +
                "workId=" + workId +
                ", workTitle='" + workTitle + '\'' +
                ", workPlace='" + workPlace + '\'' +
                ", workDeadline='" + workDeadline + '\'' +
                ", workStatus='" + workStatus + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}