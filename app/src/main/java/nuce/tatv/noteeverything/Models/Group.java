package nuce.tatv.noteeverything.Models;

public class Group {
    int mThumbnail;
    String mGroup;

    public Group(int mThumbnail, String mGroup) {
        this.mThumbnail = mThumbnail;
        this.mGroup = mGroup;
    }

    public int getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(int mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public String getmGroup() {
        return mGroup;
    }

    public void setmGroup(String mGroup) {
        this.mGroup = mGroup;
    }

}
