package csu.demo.service;

import csu.demo.dao.TicketDAO;
import csu.demo.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    @Autowired
    private TicketDAO ticketDAO;

    public void addTicket(Ticket ticket){
        ticketDAO.addTicket(ticket);
    }

    public Ticket getTicket(int uid){
        return ticketDAO.selectByUserId(uid);
    }

    public Ticket getTicket(String ticket){
        return ticketDAO.selectByTicket(ticket);
    }

    public void deleteTicket(int tid){
        ticketDAO.deleteTicketById(tid);
    }

    public void deleteTicket(String t){
        ticketDAO.deleteTicket(t);
    }
}
