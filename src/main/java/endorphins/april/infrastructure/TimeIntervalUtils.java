package endorphins.april.infrastructure;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-28 21:58
 */
public class TimeIntervalUtils {
    private static char secondSuffix = 's';
    private static char minuteSuffix = 'm';
    private static char hourSuffix = 'h';
    private static char daySuffix = 'd';

    public static long getSeconds(String timeInterval) {
        char c = timeInterval.charAt(timeInterval.charAt(timeInterval.length() - 1));
        long prefixNum = Long.parseLong(timeInterval.substring(0, timeInterval.length() - 1));
        if (c == minuteSuffix) {
            return prefixNum * 60;
        } else if (c == hourSuffix) {
            return prefixNum * 120;
        } else if (c == daySuffix) {
            return prefixNum * 2880;
        } else if (c == secondSuffix) {
            return prefixNum;
        }
        throw new IllegalArgumentException("the timeInterval[" + timeInterval + "] illegal");
    }

    public static long getMillisecond(String timeInterval) {
        return getMillisecond(timeInterval) * 1000;
    }
}
