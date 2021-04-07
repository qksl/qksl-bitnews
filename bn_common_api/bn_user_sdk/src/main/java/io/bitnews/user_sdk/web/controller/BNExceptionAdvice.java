package io.bitnews.user_sdk.web.controller;


import org.beetl.sql.core.BeetlSQLException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.netflix.hystrix.exception.HystrixRuntimeException;

import io.bitnews.user_sdk.constant.UserSdkErrorCode;
import io.bitnews.user_sdk.exception.BNException;
import io.bitnews.user_sdk.model.BNResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@ControllerAdvice
@Slf4j
public class BNExceptionAdvice {

	@ExceptionHandler(value = BNException.class)
	public BNResponse<?> handleUserSdkException(BNException exception) {
		if (UserSdkErrorCode.UNAUTHORIZED.equals(exception.getErrorCode())) {
			return new BNResponse<>(exception.getErrorCode(), true);
		} else {
			log.info("handleBNException: " + exception.getMessage());
		}
		return new BNResponse<>(exception.getErrorCode());
	}

	@ExceptionHandler(value = HystrixRuntimeException.class)
	public BNResponse<?> handleHystrixException(HystrixRuntimeException exception) {
		log.error("handleHystrixException: ", exception);
		
		return new BNResponse<>(UserSdkErrorCode.API_INVOKE_ERROR, false);
	}

	@ExceptionHandler(value = BeetlSQLException.class)
	public BNResponse<?> handleSQLException(BeetlSQLException exception) {
		log.error("handleSQLException: ", exception);
		
		return new BNResponse<>(UserSdkErrorCode.DB_SQL_FAILURE, false);
	}

	@ExceptionHandler(value = RedisConnectionFailureException.class)
	public BNResponse<?> handleRedisException(RedisConnectionFailureException exception) {
		log.error("handleRedisException: ", exception);
		
		return new BNResponse<>(UserSdkErrorCode.REDIS_CONNECTION_FAILURE, false);
	}



	@ExceptionHandler(value = Exception.class)
	public BNResponse<?> handleUnknownException(Exception exception) {
		log.error("handleUnknownException: ", exception);
		
		return new BNResponse<>(UserSdkErrorCode.INTERNAL_SERVER_ERROR, false);
	}

	@ExceptionHandler(value = JsonParseException.class)
	public BNResponse<?> handleJsonParseException(JsonParseException exception) {
		log.error("handleJsonParseException: ", exception);
		return BNResponse
				.error(UserSdkErrorCode.JSON_PARSE_ERROR, exception.getMessage(), exception.getMessage());
	}

	@ExceptionHandler(value = JsonEOFException.class)
	public BNResponse<?> handleJsonEOFException(JsonEOFException exception) {
		log.error("handleJsonEOFException: ", exception);
		return BNResponse
				.error(UserSdkErrorCode.JSON_PARSE_ERROR, exception.getMessage(), exception.getMessage());
	}

	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	public BNResponse<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
		log.error("handleHttpRequestMethodNotSupportedException: ", exception);
		return BNResponse
				.error(UserSdkErrorCode.REQUEST_METHOD_NOT_SUPPORTED, exception.getMessage(), exception.getMessage());
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public BNResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		log.error("handleMethodArgumentNotValidException: ", exception);
		return BNResponse.error(UserSdkErrorCode.BAD_REQUEST);
	}

}
