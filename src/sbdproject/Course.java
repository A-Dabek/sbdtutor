package sbdproject;

/*
    klasa odpowiadająca rekordom kursów z tabeli "courses"
*/

public class Course {
    private String description;
    private String title;
    private String author;
    private String cat;
    private Integer lessons;
    private Integer views;
    private Boolean accepted;
    
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getDescritpion() {
        return description;
    }
    public String getCat() {
        return cat;
    }
    public Integer getLessons() {
        return lessons;
    }
    public Integer getViews() {
        return views;
    }
    public Boolean isAccepted() {
        return accepted;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCat(String cat) {
        this.cat = cat;
    }
    public void setLessons(Integer lessons) {
        this.lessons = lessons;
    }
    public void setViews(Integer views) {
        this.views = views;
    }
    public void isAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
    
}
