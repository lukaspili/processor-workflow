package processorworkflow;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class AbstractProcessing<T_Model, T_State> {

    protected final Elements elements;
    protected final Types types;
    protected final Errors errors;
    protected final T_State state;
    protected final List<T_Model> specs;

    protected Class<? extends Annotation> processedAnnotation;
    protected RoundEnvironment roundEnvironment;

    public AbstractProcessing(Elements elements, Types types, Errors errors, T_State state) {
        this.elements = elements;
        this.types = types;
        this.errors = errors;
        this.state = state;
        specs = new ArrayList<>();
    }

    public abstract Set<Class<? extends Annotation>> supportedAnnotations();

    public void process(Set<? extends Element> annotationElements, Class<? extends Annotation> processedAnnotation, RoundEnvironment roundEnvironment) {
        this.processedAnnotation = processedAnnotation;
        this.roundEnvironment = roundEnvironment;

        processElements(annotationElements);
    }

    protected void processElements(Set<? extends Element> annotationElements) {
        for (Element e : annotationElements) {
            boolean success = processElement(e, errors.getFor(e));
            if (!success) {
                return;
            }
        }
    }

    /**
     * @return true if element processed with success, false otherwise and it will stop
     * the processing
     */
    public abstract boolean processElement(Element element, Errors.ElementErrors elementErrors);

    public abstract AbstractComposer<T_Model> createComposer();
}
