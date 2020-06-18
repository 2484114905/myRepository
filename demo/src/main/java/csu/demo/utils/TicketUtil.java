package csu.demo.utils;

import csu.demo.model.Ticket;
import org.joda.time.DateTime;

import java.sql.Date;

public class TicketUtil {
    public static Ticket next(int uid){
        Ticket ticket = new Ticket();
        ticket.setTicket(UuidUtil.next());
        ticket.setUserId(uid);

        DateTime expiredTime = new DateTime();
        expiredTime = expiredTime.plusMonths(3);
        ticket.setExpireAt(new Date(expiredTime.toDate().getTime()));

        return ticket;
    }

}
