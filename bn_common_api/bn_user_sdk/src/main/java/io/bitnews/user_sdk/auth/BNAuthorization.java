package io.bitnews.user_sdk.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import io.bitnews.user_sdk.constant.UserType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BNAuthorization {
	/**
	 * A <em>qualifier</em> value for the authorization.
	 * 
	 * @see #UserType
	 */
	@AliasFor("value")
	UserType type() default UserType.USER;

	@AliasFor("type")
	UserType value() default UserType.USER;

	boolean requireKyc() default false;

	/**
	 * 仅app可以访问
	 * 
	 * @return
	 */
	boolean onlyApp() default false;

	/**
	 * 仅web可以访问
	 * 
	 * @return
	 */
	boolean onlyWeb() default false;
}
