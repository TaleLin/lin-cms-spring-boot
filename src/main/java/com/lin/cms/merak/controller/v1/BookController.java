package com.lin.cms.merak.controller.v1;

import com.lin.cms.merak.model.BookDO;
import com.lin.cms.merak.service.BookService;
import com.lin.cms.merak.dto.book.CreateOrUpdateBookDTO;
import com.lin.cms.core.annotation.GroupRequired;
import com.lin.cms.core.annotation.RouteMeta;
import com.lin.cms.merak.vo.UnifyResponseVO;
import com.lin.cms.autoconfigure.exception.NotFoundException;
import com.lin.cms.merak.common.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/v1/book")
@Validated
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/{id}")
    public BookDO getBook(@PathVariable(value = "id") @Positive(message = "{id}") Long id) {
        BookDO book = bookService.getById(id);
        if (book == null) {
            throw new NotFoundException("book not found", 10022);
        }
        return book;
    }

    @GetMapping("")
    public List<BookDO> getBooks() {
        List<BookDO> books = bookService.findAll();
        return books;
    }


    @GetMapping("/search")
    public List<BookDO> searchBook(@RequestParam(value = "q", required = false, defaultValue = "") String q) {
        List<BookDO> books = bookService.getBookByKeyword("%" + q + "%");
        return books;
    }


    @PostMapping("")
    public UnifyResponseVO createBook(@RequestBody @Validated CreateOrUpdateBookDTO validator) {
        bookService.createBook(validator);
        return ResponseUtil.generateUnifyResponse(10);
    }


    @PutMapping("/{id}")
    public UnifyResponseVO updateBook(@PathVariable("id") @Positive(message = "{id}") Long id, @RequestBody @Validated CreateOrUpdateBookDTO validator) {
        BookDO book = bookService.getById(id);
        if (book == null) {
            throw new NotFoundException("book not found", 10022);
        }
        bookService.updateBook(book, validator);
        return ResponseUtil.generateUnifyResponse(11);
    }


    @DeleteMapping("/{id}")
    @RouteMeta(permission = "删除图书", module = "图书", mount = true)
    @GroupRequired
    public UnifyResponseVO deleteBook(@PathVariable("id") @Positive(message = "{id}") Long id) {
        BookDO book = bookService.getById(id);
        if (book == null) {
            throw new NotFoundException("book not found", 10022);
        }
        bookService.deleteById(book.getId());
        return ResponseUtil.generateUnifyResponse(12);
    }


}
