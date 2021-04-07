package io.bitnews.common.core.model;

import io.bitnews.common.core.constant.BNErrorCode;
import io.bitnews.common.core.constant.UserSdkErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class BNResponse<T> extends BaseResponse implements Serializable {

	private static final long serialVersionUID = -1421709553520799038L;
	private T body;

	public static <T> BNResponse<T> ok() {
		return new BNResponse<T>();
	}

	public static <T> BNResponse<T> ok(T body) {
		return new BNResponse<T>(body);
	}

	public static <T> BNResponse<T> error(BNErrorCode code, T body) {
		return new BNResponse<T>(code, body);
	}

	public static <T> BNResponse<T> error(BNErrorCode code) {
		return new BNResponse<T>(code);
	}

	public static <T> BNResponse<T> error(BNErrorCode code, String message, String messageZh) {
		BNResponse<T> thqResponse = new BNResponse<>(code);
		thqResponse.setMessageZh(messageZh);
		thqResponse.setMessage(message);
		return thqResponse;
	}

	public BNResponse() {
		super();
	}

	//成功操作
	public BNResponse(T body) {
		this(true, UserSdkErrorCode.SUCCESS, body, true);
	}

	//业务逻辑验证异常
	public BNResponse(BNErrorCode code) {
		this(code, null);
	}

	//业务逻辑验证异常
	public BNResponse(BNErrorCode code, T body) {
		this(false, code, body, false);
	}
	
	public BNResponse(BNErrorCode code, boolean toast) {
		this(false, code, null, toast);
	}
	
	public BNResponse(boolean result, BNErrorCode code, T body, boolean toast) {
		super(result, code, toast);
		this.body = body;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
