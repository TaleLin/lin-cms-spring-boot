package io.github.talelin.latticy.service;

import io.github.talelin.latticy.dto.book.CreateOrUpdateBookDTO;
import io.github.talelin.latticy.model.BookDO;

import java.util.List;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * 图书服务接口
 */
public interface BookService {

    /**
     * 新增图书
     * @param validator 数据传输对象
     * @return 是否成功
     */
    boolean createBook(CreateOrUpdateBookDTO validator);

    /**
     * 根据关键字获取图书集合
     * @param q 查询关键字
     * @return BookDO List
     */
    List<BookDO> getBookByKeyword(String q);

    /**
     * 更新图书
     * @param book 图书对象
     * @param validator 数据传输对象
     * @return 是否更新成功
     */
    boolean updateBook(BookDO book, CreateOrUpdateBookDTO validator);

    /**
     * 获取图书
     * @param id 主键id
     * @return 图书数据对象
     */
    BookDO getById(Integer id);

    /**
     * 查询所有图书
     * @return 图书数据对象集合
     */
    List<BookDO> findAll();

    /**
     * 删除图书
     * @param id 主键id
     * @return 是否删除成功
     */
    boolean deleteById(Integer id);
}
