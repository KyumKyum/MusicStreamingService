package ServiceFunc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private String SSN;
    private String name;
    private Integer age;
    private String email;
    private String id;
    private String password;
    private String nickname;

    public User() {
    }

    public User(String SSN, String name, int age, String email, String id, String password, String nickname) {
        this.SSN = SSN;
        this.name = name;
        this.age = age;
        this.email = email;
        this.id = id;
        this.password = password;
        this.nickname = nickname;
    }

    public User(ResultSet rs) throws SQLException {
        this.SSN = rs.getString("SSN");
        this.name = rs.getString("name");
        this.age = rs.getInt("age");
        this.email = rs.getString("email");
        this.id = rs.getString("ID");
        this.password = rs.getString("PW");
        this.nickname = rs.getString("Nickname");
    }

    public String getSSN() {
        return SSN;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "SSN: " + SSN + "\n" +
            "name: " + name + "\n" +
            "age: " + age + "\n" +
            "email: " + email + "\n" +
            "id: " + id + "\n" +
            "password: " + password + "\n" +
            "nickname: " + nickname + "\n";
    }
}
