package csu.demo.controllers;

import csu.demo.model.Book;
import csu.demo.model.User;
import csu.demo.service.BookService;
import csu.demo.service.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
public class BookController {

    @Autowired
    private BookService service;
    @Autowired
    private HostHolder holder;
    private void loadAllBooksView(Model model){
        model.addAttribute("books", service.getAllBooks());
    }

    @RequestMapping(path = {"/"}, method = RequestMethod.GET)
    public String bookList(Model model){
        User host = holder.getUser();
        if (host != null){
            model.addAttribute("host", host);
        }
        loadAllBooksView(model);
        return "book/books";
    }

    @RequestMapping(path = {"/books/add"}, method = RequestMethod.GET)
    public String addBook(){
        return "book/addbook";
    }

    @RequestMapping(path = {"/books/add/do"}, method = RequestMethod.POST)
    public String doAddBook(@RequestParam("name") String name,
                            @RequestParam("author") String author,
                            @RequestParam("price") String price)
    {
        Book book = new Book();
        book.setAuthor(author);
        book.setName(name);
        book.setPrice(price);
        service.addBooks(book);

        return "redirect:/";
    }
    @RequestMapping(path = {"/books/{bookId:[0-9]+}/delete"}, method = {RequestMethod.GET})
    public String deleteBook(@PathVariable("bookId") int bookId) {
        service.deleteBooks(bookId);
        return "redirect:/";

    }
    @RequestMapping(path = {"/books/{bookId:[0-9]+}/recover"}, method = RequestMethod.GET)
    public String recoverBook(@PathVariable("bookId") int bookId){
        service.recoverBook(bookId);
        return "redirect:/";
    }

}
