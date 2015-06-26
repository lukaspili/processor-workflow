package processorworkflow;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class ProcessingBuilder<T_Extractor extends AbstractExtractor, T_Model> {

    protected final T_Extractor extractor;
    protected final Errors.ElementErrors errors;

    public ProcessingBuilder(T_Extractor extractor, Errors errors) {
        this.extractor = extractor;
        this.errors = errors.getFor(extractor.getElement());
    }

    protected abstract T_Model build();
}
