package controllers;

import models.Member;
import play.Logger;
import play.mvc.Controller;

public class Accounts extends Controller {

  public static void signup() {
    render("signup.html");
  }

  public static void login() {
    render("login.html");
  }

  public static void register(String firstName, String lastName, String password, String repeatPassword, String email) {
    Logger.info("registering user: ");

    if (firstName.equals("") || lastName.equals("") || email.equals("") || password.equals("")) {
      String err = "Please complete all fields.";
      render("signup.html", err);
    }
    if (!password.equals(repeatPassword)) {
      String err = "Passwords did not match.";
      render("signup.html", err);
    }
    Member newUser = new Member(firstName, lastName, email, password);
    newUser.save();
    redirect("/login");
  }

  public static void profile() {
    Member member = Accounts.getLoggedInMember();
    render("profile.html", member);
  }

  public static void editProfile(String firstName, String lastName, String email, String password, String repeatPassword) {
    try {
      String err = null;
      Member member = Accounts.getLoggedInMember();
      if (!session.get("logged_in_id").equals(member.id.toString())) {
        redirect("/login");
      }
      member.firstName = firstName;
      member.lastName = lastName;
      member.email = email;
      if (!password.equals("")) {
        if (password.equals(repeatPassword)) {
          member.password = password;
        } else {
          err = "Passwords did not match";
        }
      }
      String msg = err != null ? err : "Details Saved.";
      member.save();
      render("profile.html", member, msg);
    } catch (Exception e) {
      Logger.info("Failed to edit profile: " + e.toString());
      redirect("/login");
    }

  }

  public static void authenticateUser(String email, String password) {
    Member member = Member.findMemberByEmail(email);
    if ((member != null) && (member.checkPassword(password))) {
      Logger.info("auth successful");
      session.put("logged_in_id", member.id);
      redirect("/stations");
    }
    Logger.info("auth failed");
    redirect("/login");
  }

  public static void logout() {
    session.clear();
    redirect("/");
  }

  public static Member getLoggedInMember() {
    Member member = null;
    if (session.contains("logged_in_id")) {
      String memberId = session.get("logged_in_id");
      member = Member.findById(Long.parseLong(memberId));
    } else {
      login();
    }
    return member;
  }

}
