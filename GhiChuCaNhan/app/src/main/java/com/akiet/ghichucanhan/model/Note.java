package com.akiet.ghichucanhan.model;

public class Note {

    private String id;
    private String title;
    private String content;
    private boolean pinned;
    private String color;
    private long timestamp;
    private String userId;

    // Constructor rỗng cho Firestore
    public Note() {
    }

    public Note(String id, String title, String content,
                boolean pinned, String color,
                long timestamp, String userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.pinned = pinned;
        this.color = color;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    // Getter / Setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public String getColor() {
        return color == null ? "#FFFFFF" : color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}