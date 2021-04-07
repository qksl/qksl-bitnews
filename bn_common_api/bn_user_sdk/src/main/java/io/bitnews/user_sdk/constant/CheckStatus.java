package io.bitnews.user_sdk.constant;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CheckStatus implements Serializable {

	private static final long serialVersionUID = 4835032400696894551L;
	private boolean check;

	public CheckStatus() {
	}

	public CheckStatus(boolean check) {
		this.check = check;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

}
