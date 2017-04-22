/*
    Klasa odpowiadająca rekordowi z tabeli "clients"
 */
package sbdproject;

public class UserAccount {
    private String name; //nazwa użytkownika
    private String password; //hasło
    private Boolean contributor; //czy ma prawa do tworzenia kursów
    private Boolean accepted; //czy jego konto jest zatwierdzone
    private Boolean elevated; //czy jego konto ma prawa administratora
    private int account; //środki na koncie
    
    public String getName() {
        return name;
    }
    public String getPass() {
        return password;
    }
    public Boolean isContributor() {
        return contributor;
    }
    public Boolean isAccepted() {
        return accepted;
    }
    public Boolean isElevated() {
        return this.elevated;
    }
    public Integer getAccount() {
        return account;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public void setPass(String pass) {
        this.password = pass;
    }
    public void isContributor(Boolean contributor) {
        this.contributor = contributor;
    }
    public void isAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
    public void isElevated(Boolean elevated) {
        this.elevated = elevated;
    }
    public void setAccount(Integer account) {
        this.account = account;
    }
}
