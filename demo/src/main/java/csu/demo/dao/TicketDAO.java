package csu.demo.dao;

import csu.demo.model.Ticket;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TicketDAO {
    String tableName = "ticket";
    String insertField = "user_id as userId, ticket as ticket,expired_at as expireAt";
    String selectField = "id," + insertField;

    @Insert({"insert into", tableName, "(", insertField, ") values (#{userId}, #{ticket}, #{expireAt})"})
    int addTicket(Ticket ticket);

    @Select({"select", selectField, "from",tableName,"where user_id = #{uid}"})
    Ticket selectByUserId(int uid);

    @Select({"select id as id, user_id as userId, ticket as ticket,expired_at as expireAt from", tableName, "where ticket = #{ticket}"})
    Ticket selectByTicket(String ticket);

    @Delete({"delete from", tableName, "where id = #{tid}"})
    void deleteTicketById(int tid);

    @Delete({"delete from", tableName, "where ticket = #{ticket}"})
    void deleteTicket(String ticket);
}
