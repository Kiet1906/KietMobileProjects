package com.akiet.ghichucanhan;

public class Note {
    private String id;         // Firestore document ID
    private String title;
    private String content;
    private long timestamp;
    private String color = "#FFFFFF"; // màu nền mặc định trắng

    // Constructor rỗng bắt buộc cho Firestore
    public Note() {}

    public Note(String title, String content, long timestamp, String color) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.color = color;
    }

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}