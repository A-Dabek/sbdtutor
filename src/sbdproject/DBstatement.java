package sbdproject;
import java.sql.*;
import java.util.ArrayList;

/*
    klasa odpowiadająca za przechowywanie i wykonywanie zapytań
*/

public class DBstatement {
    
    //klasa reprezentująca argument zapytania (unia 3 zmiennych)
    public static class arg {
        String arg_class;
        String s_val;
        Integer i_val;
        Boolean b_val;
        public arg(String s_val, Integer i_val, Boolean b_val) {
            this.s_val = s_val;
            this.i_val = i_val;
            this.b_val = b_val;
        }
        public String getSValue() {
            return s_val;
        }
        public Integer getIValue() {
            return i_val;
        }
        public Boolean getBvalue() {
            return b_val;
        }
    }
    
    //dane logowania do konta gościa
    private static final String def_name = "guest";
    private static final String def_pass = "";
    
    //kolekcja zapytań
    private static ArrayList<PreparedStatement> prepStates = null;
    
    //zamykanie zapytań
    public static void closeStates() {
        if(prepStates == null) return;
        try {
            for(int i=0; i<prepStates.size(); i++) {
                prepStates.get(i).close();
            }
        } catch (SQLException e) {
            System.out.println("On close: " + e.getMessage());
        }
        prepStates.clear();
        prepStates = null;
    }
    
    //inicjalizowanie kolekcji zapytań
    public static void initStates() {
        closeStates();
        prepStates = new ArrayList<>();
    }
    
    //dodanie nowego zapytania do kolekcji
    public static int prepState(String query) {
        Connection conn = DBcontrol.getConnection();
        if(conn == null) {
            System.out.println("Connection is null!");
            return 1;
        }
        try {
            prepStates.add(conn.prepareStatement(query));
        } catch(SQLException e) {
            System.out.println("On prepare: " + e.getMessage());
            return 1;
        } catch(Exception e) {
            System.out.println("On prepare: " + e.getMessage());
            return 1;
        }
        return 0;
    }
    
    //wykonanie zapytania numer "state" typu insert / update / delete
    public static int execState(int state, ArrayList<arg> args) {
        Integer answer = 0;
        try {
            if(prepStates.get(state) == null) {
                System.out.println("No statement!");
                return 1;
            }
            for(int i=0; i<args.size(); i++) {
                Integer arg_i = args.get(i).getIValue();
                String arg_s = args.get(i).getSValue();
                Boolean arg_b = args.get(i).getBvalue();
                if(arg_i != null) {
                    prepStates.get(state).setInt(i+1, arg_i);
                } else if(arg_s != null) {
                    prepStates.get(state).setString(i+1, arg_s);
                } else if(arg_b != null) {
                    prepStates.get(state).setBoolean(i+1, arg_b);
                } else {
                    System.out.println("Invalid argument!");
                    return 1;
                }
            }
            prepStates.get(state).executeUpdate();
        } catch(SQLException e) {
            System.out.println("On execute: " + e.getMessage());
            return 1;
        }
        return answer;
    }
    
    //wykonanie zapytania numer "state" typu fetch
    public static <T> ArrayList<T> execFetch(String record_type, int state, ArrayList<arg> args) {
        ArrayList<T> records = new ArrayList();
        try {
            if(prepStates.get(state) == null) {
                System.out.println("No statement!");
                return null;
            }
            for(int i=0; i<args.size(); i++) {
                Integer arg_i = args.get(i).getIValue();
                String arg_s = args.get(i).getSValue();
                Boolean arg_b = args.get(i).getBvalue();
                if(arg_i != null) {
                    prepStates.get(state).setInt(i+1, arg_i);
                } else if(arg_s != null) {
                    prepStates.get(state).setString(i+1, arg_s);
                } else if(arg_b != null) {
                    prepStates.get(state).setBoolean(i+1, arg_b);
                } else {
                    System.out.println("Invalid argument!");
                    return null;
                }
            }
            ResultSet rs = prepStates.get(state).executeQuery();
            while(rs.next()) {
                Object user;
                switch (record_type) {
                    case "client":
                        user = new UserAccount();
                        ((UserAccount)user).setName(rs.getString("name"));
                        ((UserAccount)user).setAccount(rs.getInt("account"));
                        ((UserAccount)user).setPass(rs.getString("password"));
                        ((UserAccount)user).isAccepted(rs.getBoolean("accepted"));
                        ((UserAccount)user).isContributor(rs.getBoolean("tutor"));
                        ((UserAccount)user).isElevated(rs.getBoolean("elevated"));
                        records.add((T)user);
                        break;
                    case "cat":
                        user = new Category();
                        ((Category)user).setName(rs.getString("name"));
                        ((Category)user).setParent(rs.getString("parent"));
                        records.add((T)user);
                        break;
                    case "course":
                        user = new Course();
                        ((Course)user).setTitle(rs.getString("title"));
                        ((Course)user).setDescription(rs.getString("description"));
                        ((Course)user).setAuthor(rs.getString("author"));
                        ((Course)user).setCat(rs.getString("cat"));
                        ((Course)user).setLessons(rs.getInt("lessons"));
                        ((Course)user).isAccepted(rs.getBoolean("accepted"));
                        ((Course)user).setViews(rs.getInt("views"));
                        records.add((T)user);
                        break;
                    case "lesson":
                        user = new Lesson();
                        ((Lesson)user).setTitle(rs.getString("title"));
                        ((Lesson)user).setContent(rs.getString("content"));
                        ((Lesson)user).setOrdinal(rs.getInt("ordinal"));
                        ((Lesson)user).setPrice(rs.getInt("price"));
                        ((Lesson)user).setCourse(rs.getString("course"));
                        ((Lesson)user).setCat(rs.getString("cat"));
                        records.add((T)user);
                        break;
                    case "payment":
                        user = new Payment();
                        ((Payment)user).setUser(rs.getString("user"));
                        ((Payment)user).setAuthor(rs.getString("author"));
                        ((Payment)user).setCourse(rs.getString("course"));
                        ((Payment)user).setCat(rs.getString("cat"));
                        ((Payment)user).setLessons(rs.getInt("lessons"));
                        ((Payment)user).setPrice(rs.getInt("price"));
                        records.add((T)user);
                        break;
                    case "report":
                        user = new Report();
                        ((Report)user).setRating(rs.getInt("rating"));
                        ((Report)user).setComment(rs.getString("comment"));
                        ((Report)user).setId(rs.getInt("id"));
                        ((Report)user).setUser(rs.getString("user"));
                        ((Report)user).setCourse(rs.getString("course"));
                        records.add((T)user);
                        break;
                    default:
                        System.out.println("Unknown data type!");
                        return null;
                }
            }
        } catch(SQLException e) {
            System.out.println("On execute: " + e.getMessage());
            return null;
        }
        return records;
    }
}
