package com.lin.cms.merak.service;

import com.lin.cms.merak.model.BookDO;
import com.lin.cms.merak.dto.book.CreateOrUpdateBookDTO;

import java.util.List;

public interface BookService {

    boolean createBook(CreateOrUpdateBookDTO validator);

    List<BookDO> getBookByKeyword(String q);

    boolean updateBook(BookDO book, CreateOrUpdateBookDTO validator);

    BookDO getById(Long id);

    List<BookDO> findAll();

    boolean deleteById(Long id);
}
