package neu.edu.csye6225.assignment2.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name="user")
public class User {
    @Id
    private String id;
    @NotNull
    private String first_name;
    @NotNull
    private String last_name;
    @NotNull
    private String password;
    @NotNull
    @Column(unique = true)
    private String email;
    @Temporal(TemporalType.TIMESTAMP)
    private Date account_created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date account_updated;
    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    public String getPassword() {
        return password;
    }

    public User(){}
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email_address) {
        this.email = email_address;
    }

    public void setAccount_created(Date account_created) {
        this.account_created = account_created;
    }

    public void setAccount_updated(Date account_updated) {
        this.account_updated = account_updated;
    }
    public Date getAccount_created() {
        return account_created;
    }

    public Date getAccount_updated() {
        return account_updated;
    }
    @Override
    public String toString()
    {

        return "User[first_name="+first_name+",last_name="+last_name+",email="+email+
                ",password="+password+"]";
    }
    public boolean checkUpdateInfo()
    {
        if(this.email!=null||this.id!=null||this.account_created!=null&&this.account_updated!=null)
            return false;
        return true;
    }


}
