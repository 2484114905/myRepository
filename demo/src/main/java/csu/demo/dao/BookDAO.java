package csu.demo.dao;

import csu.demo.model.Book;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookDAO {
    String tableName = "book";
    String insertField = "name,author,price";
    String selectField = "id,status," + insertField;

    @Select({"select", selectField, "from", tableName})
    List<Book> selectAll();

    @Insert({"insert into", tableName, "(",insertField,") values (#{name}, #{author}, #{price})"})
    int addBook(Book book);

    @Update({"update ", tableName, " set status=#{status} where id=#{id}"})
    void updateBookStatus(@Param("id") int id, @Param("status") int status);
}
