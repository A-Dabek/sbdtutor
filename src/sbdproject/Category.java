/*
    klasa odpowiadająca rekordom kategorii z tabeli "cats"
 */
package sbdproject;

public class Category {
    private String name;
    private String parent;
    
    public String getName() {
        return name;
    }
    public String getParent() {
        return parent;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public void setParent(String pass) {
        this.parent = pass;
    }
}
