package br.com.sysdesc.boletos.util.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlAnotation {

	String name();

	int size();

	int decimal() default 0;

	Class<?> type();
}
