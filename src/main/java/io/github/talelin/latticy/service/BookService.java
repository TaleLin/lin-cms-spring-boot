package io.github.talelin.latticy.service;

import io.github.talelin.latticy.model.BookDO;
import io.github.talelin.latticy.dto.book.CreateOrUpdateBookDTO;

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
