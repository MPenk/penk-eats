package umg.foka.penkeats.models;

public class User {

    private Integer userid;
    private String name;
    private String password;
    private String defaultaddress;
    private String premissions;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDefaultaddress() {
        return defaultaddress;
    }

    public void setDefaultaddress(String defaultaddress) {
        this.defaultaddress = defaultaddress;
    }

    public String getPremissions() {
        return premissions;
    }

    public void setPremissions(String premissions) {
        this.premissions = premissions;
    }

    public User(Integer userid, String name, String password, String defaultaddress, String premissions) {
        this.userid = userid;
        this.name = name;
        this.password = password;
        this.defaultaddress = defaultaddress;
        this.premissions = premissions;
    }
}
