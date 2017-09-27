package interpretator.api.run;

/**
 * Sequence run time type.
 * 
 * <p>Sequence is a list of other run time types.
 *
 * @author alex
 */
public interface SequenceValue extends Value {

    /**
     * Sequence size.
     * 
     * @return number of elements in the sequence.
     */
    int getSize();

    /**
     * Gets sequence element.
     * 
     * @param i element index.
     * @return element at index.
     */
    Value getValueAt(int i);
}
