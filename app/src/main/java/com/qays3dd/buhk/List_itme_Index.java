package com.qays3dd.buhk;

/**
 * Created by Salim3DD on 7/14/2016.
 */
public class List_itme_Index {
    private String id;
    private String page_id;
    private String Main_Title;
    private String Sub_Title ;

    public List_itme_Index(String id, String page_id, String main_Title, String sub_Title) {
        this.id = id;
        this.page_id = page_id;
        Main_Title = main_Title;
        Sub_Title = sub_Title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPage_id() {
        return page_id;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }

    public String getMain_Title() {
        return Main_Title;
    }

    public void setMain_Title(String main_Title) {
        Main_Title = main_Title;
    }

    public String getSub_Title() {
        return Sub_Title;
    }

    public void setSub_Title(String sub_Title) {
        Sub_Title = sub_Title;
    }
}
