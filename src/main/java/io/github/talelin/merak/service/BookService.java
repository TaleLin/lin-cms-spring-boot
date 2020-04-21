package io.github.talelin.merak.service;

import io.github.talelin.merak.model.BookDO;
import io.github.talelin.merak.dto.book.CreateOrUpdateBookDTO;

import java.util.List;

/**
 * @author pedro@TaleLin
 */
public interface BookService {

    boolean createBook(CreateOrUpdateBookDTO validator);

    List<BookDO> getBookByKeyword(String q);

    boolean updateBook(BookDO book, CreateOrUpdateBookDTO validator);

    BookDO getById(Long id);

    List<BookDO> findAll();

    boolean deleteById(Long id);
}
