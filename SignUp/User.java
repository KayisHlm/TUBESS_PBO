public class User {
    private String id;
    private String fullname;
    private String email;
    private String password;
    private String position;
    private String role;

    public User(String id, String fullname, String email, String password, String position, String role) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.position = position;
        this.role = role;
    }

    public String getId() { return id; }
    public String getFullname() { return fullname; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPosition() { return position; }
    public String getRole() { return role; }
}
