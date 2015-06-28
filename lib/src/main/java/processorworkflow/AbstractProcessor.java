package processorworkflow;

import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.JavaFile;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class AbstractProcessor<T_State> extends javax.annotation.processing.AbstractProcessor {

    protected Elements elements;
    protected Types types;
    protected Filer filer;
    protected Errors errors;
    protected LinkedList<AbstractProcessing> processings;
    protected T_State state;
    protected boolean stop;

    protected abstract T_State processingState();

    protected abstract LinkedList<AbstractProcessing> processings();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        elements = processingEnv.getElementUtils();
        types = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
        errors = new Errors();

        state = processingState();
        processings = processings();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (stop) return false;

        List<AbstractComposer> composers = new ArrayList<>();

        for (AbstractProcessing processing : processings) {
            Set<Class<? extends Annotation>> supportedAnnotations = processing.supportedAnnotations();
            for (Class<? extends Annotation> annotation : supportedAnnotations) {
                Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
                processing.process(elements, annotation, roundEnv);

                if (isInvalid()) return false;
            }

            AbstractComposer composer = processing.createComposer();
            if (composer != null) {
                composers.add(composer);
            }
        }

        List<JavaFile> javaFiles = new ArrayList<>();
        for (AbstractComposer composer : composers) {
            javaFiles.addAll(composer.compose());
        }

        for (JavaFile javaFile : javaFiles) {
            try {
                javaFile.writeTo(filer);
            } catch (Exception e) {
                StringWriter stackTrace = new StringWriter();
                e.printStackTrace(new PrintWriter(stackTrace));
            }
        }

        return false;
    }

    protected boolean isInvalid() {
        if (errors.hasErrors()) {
            errors.deliver(processingEnv.getMessager());
            stop = true;
            return true;
        }

        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        for (AbstractProcessing processing : processings) {
            Set<Class<? extends Annotation>> annotations = processing.supportedAnnotations();
            for (Class<? extends Annotation> annotation : annotations) {
                builder.add(annotation.getName());
            }
        }
        return builder.build();
    }
}
