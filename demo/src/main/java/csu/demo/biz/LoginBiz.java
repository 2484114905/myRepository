package csu.demo.biz;

import csu.demo.model.Ticket;
import csu.demo.model.User;
import csu.demo.service.HostHolder;
import csu.demo.service.TicketService;
import csu.demo.service.UserService;
import csu.demo.utils.ConcurrentUtil;
import csu.demo.utils.MD5;
import csu.demo.utils.TicketUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.LobRetrievalFailureException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoginBiz {
    @Autowired
    private UserService userService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private HostHolder holder;

    public String login(String email, String password) throws Exception{
        User user = userService.getUser(email);
        if (user == null){
            throw new LobRetrievalFailureException("email not exist");
        }
        if (!StringUtils.equals(MD5.next(password),user.getPassword())){
            throw new LobRetrievalFailureException("incorrect password");
        }
        Ticket ticket = ticketService.getTicket(user.getId());
        if (ticket == null){
            ticket = TicketUtil.next(user.getId());
            ticketService.addTicket(ticket);
            return ticket.getTicket();
        }

        if (ticket.getExpireAt().before(new Date())){
            ticketService.deleteTicket(ticket.getId());
        }

        ticket = TicketUtil.next(user.getId());
        ticketService.addTicket(ticket);
        ConcurrentUtil.setHost(user);
        return ticket.getTicket();
    }

    public void logout(String ticket){
        ticketService.deleteTicket(ticket);
    }

    public String register(User user) throws Exception{
        if (userService.getUser(user.getEmail()) != null){
            throw new LobRetrievalFailureException("email exists");
        }
        String plain = user.getPassword();
        String md5 = MD5.next(plain);
        user.setPassword(md5);
        userService.addUser(user);
        System.out.println(user.getId());
        User user2 = userService.getUser(user.getEmail());
        System.out.println(user2.getId());
        Ticket ticket = TicketUtil.next(user2.getId());
        ticketService.addTicket(ticket);
        ConcurrentUtil.setHost(user2);
        return ticket.getTicket();
    }
}
