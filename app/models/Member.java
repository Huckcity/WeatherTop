package models;

import play.Logger;
import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member extends Model {

  public String firstName;
  public String lastName;
  public String email;
  public String password;

  @OneToMany(cascade = CascadeType.ALL)
  public List<Station> stations = new ArrayList<Station>();

  public Member(String firstName, String lastName, String email, String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
  }

  public static Member findMemberByEmail(String email) {
    return find("email", email).first();
  }

  public boolean checkPassword(String password) {
    return this.password.equals(password);
  }
}
