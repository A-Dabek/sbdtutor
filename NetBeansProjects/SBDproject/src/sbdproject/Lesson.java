/*
    klasa odpowiadająca rekordowi w tabeli "lesson"
 */
package sbdproject;

public class Lesson {
    private String title; //tytuł lekcji
    private String content; //zawartość lekcji, treść
    private String course; //kurs, z którego lekcja pochodzi, kontekt lekcji
    private String cat; //kategoria kursu, kontekst lekcjo
    private Integer ordinal; //liczba porządkowa lekcji w kursie
    private Integer price; //cena za wykupienie dostępu lekcji
    
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public String getCourse() {
        return course;
    }
    public String getCat() {
        return cat;
    }
    public Integer getOrdinal() {
        return ordinal;
    }
    public Integer getPrice() {
        return price;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    public void setCourse(String course) {
        this.course = course;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setCat(String cat) {
        this.cat = cat;
    }
    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    
}
