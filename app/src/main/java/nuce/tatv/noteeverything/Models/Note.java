package nuce.tatv.noteeverything.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Note {

    @SerializedName("note_id")
    @Expose
    private Integer noteId;
    @SerializedName("note_title")
    @Expose
    private String noteTitle;
    @SerializedName("note_content")
    @Expose
    private String noteContent;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("note_date")
    @Expose
    private String noteDate;

    public Note(String noteTitle, String noteContent, String userName, String noteDate) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.userName = userName;
        this.noteDate = noteDate;
    }

    public Note(Integer noteId, String noteTitle, String noteContent, String userName, String noteDate) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.userName = userName;
        this.noteDate = noteDate;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteId=" + noteId +
                ", noteTitle='" + noteTitle + '\'' +
                ", noteContent='" + noteContent + '\'' +
                ", userName='" + userName + '\'' +
                ", noteDate='" + noteDate + '\'' +
                '}';
    }
}
