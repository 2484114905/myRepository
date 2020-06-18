package csu.demo.interceptor;

import csu.demo.model.Ticket;
import csu.demo.service.TicketService;
import csu.demo.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private TicketService ticketService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String t = CookieUtils.getCookie("ticket", request);
        if (StringUtils.isEmpty(t)){
            response.sendRedirect("/user/login");
            return false;
        }

        Ticket ticket = ticketService.getTicket(t);
        if (ticket == null){
            response.sendRedirect("/user/login");
            return false;
        }

        if(ticket.getExpireAt().before(new Date())){
            response.sendRedirect("/user/login");
            return false;
        }
        return true;
    }
}
