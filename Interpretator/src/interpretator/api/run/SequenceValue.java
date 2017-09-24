package interpretator.api.run;

/**
 *
 * @author alex
 */
public interface SequenceValue extends Value {

    int getSize();

    Value getValueAt(int i);
}
