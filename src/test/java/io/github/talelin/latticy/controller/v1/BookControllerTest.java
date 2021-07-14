package io.github.talelin.latticy.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.github.talelin.latticy.dto.book.CreateOrUpdateBookDTO;
import io.github.talelin.latticy.mapper.BookMapper;
import io.github.talelin.latticy.model.BookDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
@Rollback
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookMapper bookMapper;

    private Integer id;
    private final String title = "千里之外";
    private final String author = "pedro";
    private final String image = "千里之外.png";
    private final String summary = "千里之外，是周杰伦和费玉清一起发售的歌曲";


    @Test
    public void getBook() throws Exception {
        BookDO bookDO = new BookDO();
        bookDO.setTitle(title);
        bookDO.setAuthor(author);
        bookDO.setImage(image);
        bookDO.setSummary(summary);
        bookMapper.insert(bookDO);

        this.id = bookDO.getId();
        this.mvc.perform(get("/v1/book/" + this.id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.title").value(title));
    }

    @Test
    public void getBooks() throws Exception {
        BookDO bookDO = new BookDO();
        bookDO.setTitle(title);
        bookDO.setAuthor(author);
        bookDO.setImage(image);
        bookDO.setSummary(summary);
        bookMapper.insert(bookDO);
        this.id = bookDO.getId();

        mvc.perform(get("/v1/book"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    public void searchBook() throws Exception {
        BookDO bookDO = new BookDO();
        bookDO.setTitle(title);
        bookDO.setAuthor(author);
        bookDO.setImage(image);
        bookDO.setSummary(summary);
        bookMapper.insert(bookDO);
        this.id = bookDO.getId();

        mvc.perform(get("/v1/book/search?q=千里"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$").isArray());
    }

    @Test
    public void createBook() throws Exception {
        CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
        dto.setAuthor(author);
        dto.setImage(image);
        dto.setSummary(summary);
        dto.setTitle(title);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        String content = mapper.writeValueAsString(dto);

        mvc.perform(post("/v1/book/")
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.message").value("新建图书成功"));
    }

    @Test
    public void updateBook() throws Exception {
        BookDO bookDO = new BookDO();
        bookDO.setTitle(title);
        bookDO.setAuthor(author);
        bookDO.setImage(image);
        bookDO.setSummary(summary);
        bookMapper.insert(bookDO);
        this.id = bookDO.getId();

        CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
        dto.setAuthor(author);
        dto.setImage(image);
        dto.setSummary(summary);
        dto.setTitle(title + "lol");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        String content = mapper.writeValueAsString(dto);

        mvc.perform(put("/v1/book/" + this.id)
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.message").value("更新图书成功"));
    }

    @Test
    public void deleteBook() throws Exception {
        BookDO bookDO = new BookDO();
        bookDO.setTitle(title);
        bookDO.setAuthor(author);
        bookDO.setImage(image);
        bookDO.setSummary(summary);
        bookMapper.insert(bookDO);
        this.id = bookDO.getId();

        mvc.perform(delete("/v1/book/" + this.id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.message").value("删除图书成功"));
    }

}