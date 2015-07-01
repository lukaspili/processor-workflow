# Processor Workflow

Processor workflow is a library that facilitates the development of an annotation processor in Java.
It offers an efficient workflow to handle the steps of parsing annotations, extracting useful data, and then generating some java code.

Annotation processors that work successfully with processor workflow:
 - Auto Dagger2: [https://github.com/lukaspili/Auto-Dagger2](https://github.com/lukaspili/Auto-Dagger2)
 - Mortar Architect: [https://github.com/lukaspili/Mortar-architect](https://github.com/lukaspili/Mortar-architect)


## Gradle

```groovy
dependencies {
    compile 'com.github.lukaspili.processor-workflow:processor-workflow:1.0.1'
}
```