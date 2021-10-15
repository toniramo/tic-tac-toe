package tictactoe.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to exclude methods from Jacoco test report. 
 * impelemntation based on https://stackoverflow.com/a/66918619 .
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.MODULE})
public @interface ExcludeFromJacocoGeneratedReport {
}
