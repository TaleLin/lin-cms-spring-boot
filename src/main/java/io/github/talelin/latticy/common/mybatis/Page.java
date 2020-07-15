package io.github.talelin.latticy.common.mybatis;

/**
 * 为和其他端保持一致
 * 重写 MyBatis-Plus 分页对象，将起始页从 1 改为 0
 *
 * @author Juzi@TaleLin
 */
public class Page<T> extends com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> {

    private static final long serialVersionUID = -2183463672525305273L;

    /**
     * 该构造方法使得 current 总为 0
     */
    public Page() {
        super.setCurrent(0);
    }

    public Page(int current, int size) {
        this(current, size, 0);
    }

    public Page(int current, int size, int total) {
        this(current, size, total, true);
    }

    public Page(int current, int size, boolean isSearchCount) {
        this(current, size, 0, isSearchCount);
    }

    /**
     * 该构造方法将小于 0 的 current 置为 0
     *
     * @param current       当前页
     * @param size          每页显示条数，默认 10
     * @param total         总数
     * @param isSearchCount 是否进行 count 查询
     */
    public Page(int current, int size, int total, boolean isSearchCount) {
        super(current, size, total, isSearchCount);

        if (current < 0) {
            current = 0;
        }
        super.setCurrent(current);
    }

    @Override
    public boolean hasPrevious() {
        return super.getCurrent() > 0;
    }

    @Override
    public boolean hasNext() {
        return super.getCurrent() + 1 < this.getPages();
    }

    /**
     * 重写计算偏移量，将分页从第 0 开始
     *
     * @return 偏移量
     */
    @Override
    public long offset() {
        return getCurrent() > 0 ? super.getCurrent() * getSize() : 0;
    }
}
