package model.data.user;

public class User {
    private final int id;
    private String name;
    private String email;

    private final UserType typeUser;

    public User(int id, String name, String email, UserType typeUser) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.typeUser = typeUser;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getTypeUser() {
        return typeUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", typeUser=" + typeUser +
                '}';
    }
}
