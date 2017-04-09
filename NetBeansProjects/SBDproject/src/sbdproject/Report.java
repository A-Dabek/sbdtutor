/*
    klasa odpowiadająca rekordowi z tabeli "reports"
 */
package sbdproject;

public class Report {
    private String comment; //treść wiadomości
    private String course; //kurs - kontext komentarza
    private String user; //twórca komentarza
    private Integer id; //identyfikator komentarza
    private Integer rating; //ocena kursu (0 - zgłoszenie do admina)
    

    public String getComment() {
        return comment;
    }
    public String getCourse() {
        return course;
    }
    public String getUser() {
        return user;
    }
    public Integer getId() {
        return id;
    }
    public Integer getRating() {
        return rating;
    }
    
    public void setCourse(String course) {
        this.course = course;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
