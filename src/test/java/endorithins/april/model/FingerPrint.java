package endorithins.april.model;

import com.sun.javafx.collections.SortHelper;
import lombok.Data;

import java.util.List;

/**
 * @author timothy
 * @data 2022年09月27日 16:48
 */
@Data
public class FingerPrint {
    private long code;

    public int len() {
        return Long.toString(this.code).length();
    }

    public static FingerPrint labelsToFingerprint(List<String> labelNames) {
        SortHelper sortHelper = new SortHelper();
        sortHelper.sort(labelNames);
        for (String labelName : labelNames) {

        }
        return null;
    }
}
