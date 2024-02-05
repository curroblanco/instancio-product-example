package org.example.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.platform.commons.annotation.Testable;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Testable
public @interface BenchMark {

  /**
   * Difference percentage allowed between last and current test
   * @return Allowed value
   */
  double percentageAllowed() default 0;
}
