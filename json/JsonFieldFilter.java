package org.hangzhou.tool.autoToJson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
 
/**
 * * 描述： * *
 * 
 * <pre>
 * HISTORY * **************************************************************************** * 
 * ID DATE PERSON REASON * 1 2018年11月22日 cx Create 
 * * **************************************************************************** 
 * *
 * </pre>
 * 
 * * @author cx
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonFieldFilter {
	Class<?> type();// 对哪个类的属性进行过滤
 
	String include() default "";// 包含哪些字段，即哪些字段可以显示
 
	String exclude() default "";// 不包含哪些字段，即哪些字段不可以显示
}
