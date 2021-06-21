package com.example.s19;

public class uploadPDF {


    public String nam;
    public String url;

    public uploadPDF() {
    }

    public uploadPDF(String nam, String url) {
        this.nam = nam;
        this.url = url;
    }

    public String getNam() {
        return nam;
    }

    public void setNam(String nam) {
        this.nam = nam;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}