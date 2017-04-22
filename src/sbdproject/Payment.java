/*
    klasa odpowiadająca rekordowi transakcji kupna w tabeli "payments"
 */
package sbdproject;

public class Payment {
    private String user; //użytkownik kupujący
    private String author; //użytkownik sprzedający
    private String course; //kurs, kontekst płatności
    private String cat; //kategoria kursu, kontekst płatności
    private Integer lessons; //ilośc lekcji jakie chcemy wykupić
    private Integer price; //cena jaką zapłaciliśmy
    
    public String getAuthor() {
        return author;
    }
    public String getCourse() {
        return course;
    }
    public String getUser() {
        return user;
    }
    public String getCat() {
        return cat;
    }
    public Integer getLessons() {
        return lessons;
    }
    public Integer getPrice() {
        return price;
    }
    
    public void setCourse(String course) {
        this.course = course;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setCat(String cat) {
        this.cat = cat;
    }
    public void setLessons(Integer lessons) {
        this.lessons = lessons;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
}
