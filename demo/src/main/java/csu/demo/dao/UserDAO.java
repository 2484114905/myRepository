package csu.demo.dao;

import csu.demo.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDAO {
    String tableName = "user";
    String insertField = "name, email, password";
    String selectField = "id," + insertField;

    @Insert({"insert into", tableName, "(", insertField,") values(#{name}, #{email}, #{password})"})
    int addUser(User user);

    @Select({"select", selectField, "from", tableName, "where id = #{id}"})
    User selectById(int id);

    @Select({"select", selectField, "from", tableName, "where name = #{name}"})
    User selectByName(String name);

    @Select({"select", selectField, "from", tableName,"where email=#{email}"})
    User selectByEmail(String email);

    @Update({"update", tableName, "set password = #{password} where id = #{id}"})
    void updatePassword(User user);
}
