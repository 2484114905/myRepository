package csu.demo.controllers;

import csu.demo.biz.LoginBiz;
import csu.demo.model.User;
import csu.demo.service.HostHolder;
import csu.demo.service.UserService;
import csu.demo.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    @Autowired
    private LoginBiz loginBiz;
    @Autowired
    private UserService service;
    @Autowired
    private HostHolder holder;

    @RequestMapping(path = {"/user/register"}, method = {RequestMethod.GET})
    public String register(){
        return "login/register";
    }

    @RequestMapping(path = {"/user/register/do"}, method = {RequestMethod.POST})
    public String doRegister(Model model,
                             HttpServletResponse response,
                             @RequestParam("name") String name,
                             @RequestParam("email") String email,
                             @RequestParam("password") String password){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        try {
            String ticket = loginBiz.register(user);
            CookieUtils.writeCookie("ticket", ticket, response);
            return "redirect:/";
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "404";
        }
    }

    @RequestMapping(path = {"/user/login"}, method = RequestMethod.GET)
    public String login(){
        return "login/login";
    }

    @RequestMapping(path = {"/user/login/do"}, method = RequestMethod.POST)
    public String doLogin(
            Model model,
            HttpServletResponse response,
            @RequestParam("email") String email,
            @RequestParam("password") String password){
        try {
            String ticket = loginBiz.login(email, password);
            CookieUtils.writeCookie("ticket", ticket, response);
            return "redirect:/";
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "404";
        }
    }

    @RequestMapping(path = {"/user/logout/do"}, method = {RequestMethod.GET})
    public String doLogout(@CookieValue("ticket") String ticket){
        loginBiz.logout(ticket);
        return "redirect:/";
    }
}
