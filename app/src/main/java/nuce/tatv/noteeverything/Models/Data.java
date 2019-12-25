package nuce.tatv.noteeverything.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("user")
    @Expose
    private List<User> user = null;
    @SerializedName("note")
    @Expose
    private List<Note> note = null;



    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public List<Note> getNote() {
        return note;
    }

    public void setNote(List<Note> note) {
        this.note = note;
    }
}