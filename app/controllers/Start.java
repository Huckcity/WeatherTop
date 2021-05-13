package controllers;

import play.mvc.Controller;

public class Start extends Controller {

  public static void index() {
    if (session.get("logged_in_id") != null) {
      redirect("/stations");
    }
    render("start.html");
  }

  public static void about() {
    render("about.html");
  }

  public static void signup() {
    render("signup.html");
  }

}
