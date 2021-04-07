package io.bitnews.user_sdk.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * 币种
 * 
 * @Author: wangyufei
 * @Date: 2018/7/13 19:57
 */
public enum CoinCode {
	/**
	 * 量子链
	 */
	QTUM("QTUM","QTUM","https://qtum.info/tx", "qtum", "qtum", false),
	/**
	 * 以太坊
	 */
	ETH("ETH","Ethereum","", "eth", "eth", false),
	/**
	 * 唯链
	 */
	VET("VET","VET","https://explore.veforge.com/transactions", "vet", "vet", false),
	/**
	 * 唯链
	 */
	VTHO("VTHO","VTHO","https://explore.veforge.com/transactions", "vtho", "vtho", false),
	/**
	 * 比特币
	 */
	BTC("BTC","Bitcoin","", "btc", "btc", false),
	
	/**
	 * CYB
	 */
	CYB("CYB","Cybex","https://olddex.cybex.io/block", "cyb", "cyb", false),
	
	/**
	 * EOS
	 */
	EOS("EOS","EOS","https://www.eosx.io/tx", "eos", "eos", false),

	
	/**
	 * GNX
	 */
	GNX("GNX", "gnx", "https://www.gnx.io/txs", "gnx", "gnx",false),

	ATOM("ATOM", "atom", "https://www.mintscan.io/txs", "uatom", "stake", true),

	CMT("CMT", "cmt", "https://www.cmttracking.io/tx", "ucmt", "stake", true),

	VSYS("VSYS", "vsys", "https://explorer.v.systems/transactions", "vsys", "vsys", false),

	IRIS("IRIS", "isis", "https://www.irisplorer.io/#/tx?txHash=", "iris-atto", "isis", true)

	;


	private final String message;
	private final String fullName;
	private final String mainToken;
	private final String testToken;
	private final String blockChainUrl;
	private final Boolean needStake;

	/**
	 * 构造
	 */
	CoinCode(String message,String fullName,String blockChainUrl, String mainToken, String testToken, Boolean needStake) {
		this.message = message;
		this.fullName = fullName;
		this.blockChainUrl = blockChainUrl;
		this.mainToken = mainToken;
		this.testToken = testToken;
		this.needStake = needStake;
	}

	/**
	 * 根据code获取枚举
	 */
	public static CoinCode getByCode(String code) {
		if (StringUtils.isBlank(code)) {
			return null;
		}

		for (CoinCode tt : CoinCode.values()) {
			if (tt.name().equals(code)) {
				return tt;
			}
		}

		return null;
	}
	
	public static boolean hasMemo(CoinCode coinCode) {
		switch (coinCode) {
		case EOS:
		case CYB:
		case ATOM:
		case IRIS:
			return true;

		default:
			return false;
		}
	}

	/**
	 * 是否支持紧急提币
	 * 
	 * @param coinCode
	 * @return
	 */
	public static boolean supportUrgent(CoinCode coinCode) {
		switch (coinCode) {
		case ATOM:
		case IRIS:
		case CMT:
			return true;

		default:
			return false;
		}
	}

	/**
	 * 是否支持大额提币
	 * 
	 * @param coinCode
	 * @return
	 */
	public static boolean supportLarge(CoinCode coinCode) {
		switch (coinCode) {
		case EOS:
		case QTUM:
		case VET:
		case VTHO:
		case CYB:
			return true;

		default:
			return false;
		}
	}

	public String getFullName() {
		return fullName;
	}

	public String getMessage() {
		return message;
	}

	public String getBlockChainUrl() {
		return blockChainUrl;
	}

	public String getMainToken() {
		return mainToken;
	}

	public String getTestToken() {
		return testToken;
	}

	public Boolean getNeedStake() {
		return needStake;
	}
}
