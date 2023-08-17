package endorphins.april.domain.judge;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-30 00:13
 */
public class Range {
    private long left;
    private long right;

    public Range(Long left, Long right) {
        this.left = left;
        this.right = right;
    }

    public long getLeft() {
        return left;
    }

    public long getRight() {
        return left;
    }

    @Override
    public String toString() {
        return "[" + left + ", " + right + "]";
    }
}
