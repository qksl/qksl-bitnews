package io.bitnews.user_sdk.constant;

/**
 * KYC审核状态
 * @author apple
 *
 */
public enum KycStatus {
	/**
	 * 待审核
	 */
	INITIAL(0), 
	/**
	 * 审核通过
	 */
	PASSED(1), 
	/**
	 * 审核拒绝
	 */
	REJECT(2), 
	/**
	 * 不确定状态
	 */
	UNSURE(3), 
	/**
	 * 审核中
	 */
	PROCESS(4);
	private int value;

	KycStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
