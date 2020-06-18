package csu.demo.interceptor;

import csu.demo.model.Ticket;
import csu.demo.model.User;
import csu.demo.service.TicketService;
import csu.demo.service.UserService;
import csu.demo.utils.ConcurrentUtil;
import csu.demo.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class HostInfoInterInterceptor implements HandlerInterceptor {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception{
        String ticket = CookieUtils.getCookie("ticket", request);
        if (ticket == null){
            System.out.println("ticket is null");
        }
        if (!StringUtils.isEmpty(ticket)){
            Ticket ticket1 = ticketService.getTicket(ticket);
            if (ticket1 == null){
                System.out.println("ticket1 is null--");
            }
//            System.out.println("expired Date" + ticket1.getExpireAt());
            if (ticket1 != null && ticket1.getExpireAt().after(new Date())){
                System.out.println("ticket1--userId" + ticket1.getUserId());
                User host = userService.getUser(ticket1.getUserId());
                if (host == null){
                    System.out.println("host is null");
                }
                System.out.println(host.getId());
                ConcurrentUtil.setHost(host);
            }
        }
        return true;
    }
}
