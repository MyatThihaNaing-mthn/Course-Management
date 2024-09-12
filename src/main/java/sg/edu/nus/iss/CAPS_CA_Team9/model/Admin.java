package sg.edu.nus.iss.CAPS_CA_Team9.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Admin {
    @Id
    @Column(name="adminId")
    private String id;

    @Column(name = "adminName")
    private String adminName;

    @Column(name="adminPwd")
    private String adminPwd;

    public Admin(){}
    
    public Admin(String adminName, String adminPwd) {
        this.adminName = adminName;
        this.adminPwd = adminPwd;
    }
}