package Models.Page;

import Logic.LogicalAgent;
import Models.User;

public abstract class LogPage extends Page {
    protected String username;
    protected String password;
    protected String secretInformation;

    public LogPage(){}

    public LogPage(User user , Page previousPage , LogicalAgent logicalAgent) {
        super(user,previousPage,logicalAgent);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecretInformation() {
        return secretInformation;
    }

    public void setSecretInformation(String secretInformation) {
        this.secretInformation = secretInformation;
    }

}
