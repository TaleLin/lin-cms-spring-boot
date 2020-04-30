package io.github.talelin.latticy.mapper;

import io.github.talelin.latticy.model.BookDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
public class BookMapperTest {

    @Autowired
    private BookMapper bookMapper;

    public BookDO initData() {
        String title = "千里之外";
        String author = "pedro";
        String image = "千里之外.png";
        String summary = "千里之外，是周杰伦和费玉清一起发售的歌曲";
        BookDO bookDO = new BookDO();
        bookDO.setTitle(title);
        bookDO.setAuthor(author);
        bookDO.setImage(image);
        bookDO.setSummary(summary);
        bookMapper.insert(bookDO);
        return bookDO;
    }


    @Test
    public void selectByTitleLikeKeyword() {
        BookDO book = initData();
        List<BookDO> found = bookMapper.selectByTitleLikeKeyword("%千里%");
        boolean anyMatch = found.stream().anyMatch(it -> it.getTitle().equals(book.getTitle()));
        assertTrue(anyMatch);
    }


    @Test
    public void selectById() {
        BookDO book = initData();
        BookDO found = bookMapper.selectById(book.getId());
        assertEquals(found.getTitle(), book.getTitle());
    }

    @Test
    public void selectByTitle() {
        BookDO book = initData();
        List<BookDO> found = bookMapper.selectByTitle(book.getTitle());
        boolean anyMatch = found.stream().anyMatch(it -> it.getTitle().equals(book.getTitle()));
        assertTrue(anyMatch);
    }
}