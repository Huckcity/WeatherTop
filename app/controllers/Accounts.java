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

    public static void register(String firstName, String lastName, String password, String repeatPass, String email) {
        Logger.info("registering user: ");

        // validate user

        Member newUser = new Member(firstName, lastName, email, password);
        newUser.save();
        redirect("/");
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

    public static Member getLoggedInMember()
    {
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
