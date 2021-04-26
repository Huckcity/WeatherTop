package controllers;

import play.mvc.Controller;

public class Start extends Controller {

    public static void index() {
        render("start.html");
    }

}
