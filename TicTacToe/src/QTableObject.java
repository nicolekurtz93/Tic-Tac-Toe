import java.util.*;

public class QTableObject {
    List<String> Actions = new ArrayList<String>();
    Double QValue = 0.00;

    QTableObject(List<String> actions, Double qvalue) {
        this.Actions.addAll(actions);
        this.QValue = qvalue;
    }
}
