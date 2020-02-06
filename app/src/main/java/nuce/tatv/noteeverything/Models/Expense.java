package nuce.tatv.noteeverything.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Expense {
    private boolean header;
    @SerializedName("expense_id")
    @Expose
    private Integer expenseId;
    @SerializedName("expense_thumbnail")
    @Expose
    private Integer expenseThumbnail;
    @SerializedName("expense_title")
    @Expose
    private String expenseTitle;
    @SerializedName("expense_content")
    @Expose
    private String expenseContent;
    @SerializedName("expense_amount")
    @Expose
    private Integer expenseAmount;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("expense_date")
    @Expose
    private String expenseDate;

    public Expense(boolean header, String expenseDate) {
        this.header = header;
        this.expenseDate = expenseDate;
    }

    public Expense(Integer expenseThumbnail, String expenseTitle, String expenseContent, Integer expenseAmount, String userName, String expenseDate) {
        this.expenseThumbnail = expenseThumbnail;
        this.expenseTitle = expenseTitle;
        this.expenseContent = expenseContent;
        this.expenseAmount = expenseAmount;
        this.userName = userName;
        this.expenseDate = expenseDate;
    }

    public Expense(Integer expenseId, Integer expenseThumbnail, String expenseTitle, String expenseContent, Integer expenseAmount, String userName, String expenseDate) {
        this.expenseId = expenseId;
        this.expenseThumbnail = expenseThumbnail;
        this.expenseTitle = expenseTitle;
        this.expenseContent = expenseContent;
        this.expenseAmount = expenseAmount;
        this.userName = userName;
        this.expenseDate = expenseDate;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public Integer getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Integer expenseId) {
        this.expenseId = expenseId;
    }

    public Integer getExpenseThumbnail() {
        return expenseThumbnail;
    }

    public void setExpenseThumbnail(Integer expenseThumbnail) {
        this.expenseThumbnail = expenseThumbnail;
    }

    public String getExpenseTitle() {
        return expenseTitle;
    }

    public void setExpenseTitle(String expenseTitle) {
        this.expenseTitle = expenseTitle;
    }

    public String getExpenseContent() {
        return expenseContent;
    }

    public void setExpenseContent(String expenseContent) {
        this.expenseContent = expenseContent;
    }

    public Integer getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(Integer expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }


    @Override
    public String toString() {
        return "Expense{" +
                "header=" + header +
                ", expenseId=" + expenseId +
                ", expenseThumbnail=" + expenseThumbnail +
                ", expenseTitle='" + expenseTitle + '\'' +
                ", expenseContent='" + expenseContent + '\'' +
                ", expenseAmount=" + expenseAmount +
                ", userName='" + userName + '\'' +
                ", expenseDate='" + expenseDate + '\'' +
                '}';
    }
}