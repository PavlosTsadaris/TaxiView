package gr.softeng.team19.domain;

public class ManagementTeam extends User {
    private String recoveryEmail;

    public ManagementTeam(String userName, String password, String email,String recoveryEmail) {
        super(userName, password, email);
        this.recoveryEmail = recoveryEmail;
    }

    public String getFullDetails() {
        return "Management Team Details:\n" +
                "Username: " + getUserName()  + "\n" +
                "Password: " + getPassword() + "\n" +
                "Email: " + getEmail() + "\n" +
                "Recovery Email: " + getRecoveryEmail() + "\n" ;
    }

    public String getRecoveryEmail() {
        return recoveryEmail;
    }

    public void setRecoveryEmail(String recoveryEmail) {
        this.recoveryEmail = recoveryEmail;
    }

    public String getStatistics() {
        return "Statitics";
    }
}
