package com.qays3dd.buhk;

import android.app.Application;

public class GlobalVar extends Application {
    private String listType ="index" ;
    private String page_id;
    private String Main_Title;
    private String Sub_Title ;

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
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
