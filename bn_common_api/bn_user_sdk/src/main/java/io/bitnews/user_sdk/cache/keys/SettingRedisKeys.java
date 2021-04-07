package io.bitnews.user_sdk.cache.keys;

public enum SettingRedisKeys {

	INVITATION_CODE_REQUIRE("invitation.code.require", "系统是否要求邀请码注册"), 
	;

	private String key;
	private String desc;

	SettingRedisKeys(String key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
