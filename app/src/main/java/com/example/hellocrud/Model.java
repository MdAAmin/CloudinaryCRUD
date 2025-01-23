package com.example.hellocrud;

public class Model {
    private String title, subtitle, pdfUrl, key;

    // Default constructor
    public Model() {}

    // Parameterized constructor
    public Model(String title, String subtitle, String pdfUrl, String key) {
        this.title = title;
        this.subtitle = subtitle;
        this.pdfUrl = pdfUrl;
        this.key = key;
    }

    // Getter and setter for the title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and setter for the subtitle
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    // Getter and setter for the PDF URL
    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    // Getter and setter for the key
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}