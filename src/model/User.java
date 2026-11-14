package model;

public abstract class User {
    private final String userId;
    private final String name;
    private final String email;
    private final String password;

    protected User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public boolean authenticate(String candidate) { return password.equals(candidate); }
}
