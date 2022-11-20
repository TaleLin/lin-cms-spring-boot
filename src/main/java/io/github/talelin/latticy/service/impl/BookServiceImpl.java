package io.github.talelin.latticy.service.impl;

import io.github.talelin.latticy.dto.book.CreateOrUpdateBookDTO;
import io.github.talelin.latticy.mapper.BookMapper;
import io.github.talelin.latticy.model.BookDO;
import io.github.talelin.latticy.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * 图书服务实现类
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public boolean createBook(CreateOrUpdateBookDTO validator) {
        BookDO book = new BookDO();
        book.setAuthor(validator.getAuthor());
        book.setTitle(validator.getTitle());
        book.setImage(validator.getImage());
        book.setSummary(validator.getSummary());
        return bookMapper.insert(book) > 0;
    }

    @Override
    public List<BookDO> getBookByKeyword(String q) {
        return bookMapper.selectByTitleLikeKeyword(q);
    }

    @Override
    public boolean updateBook(BookDO book, CreateOrUpdateBookDTO validator) {
        book.setAuthor(validator.getAuthor());
        book.setTitle(validator.getTitle());
        book.setImage(validator.getImage());
        book.setSummary(validator.getSummary());
        return bookMapper.updateById(book) > 0;
    }

    @Override
    public BookDO getById(Integer id) {
        return bookMapper.selectById(id);
    }

    @Override
    public List<BookDO> findAll() {
        return bookMapper.selectList(null);
    }

    @Override
    public boolean deleteById(Integer id) {
        return bookMapper.deleteById(id) > 0;
    }
}
