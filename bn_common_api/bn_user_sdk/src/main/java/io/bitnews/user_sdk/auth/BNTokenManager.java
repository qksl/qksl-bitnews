package io.bitnews.user_sdk.auth;

import io.bitnews.user_sdk.constant.LoginSource;
import io.bitnews.user_sdk.constant.UserRole;
import io.bitnews.user_sdk.constant.UserStatus;

public interface BNTokenManager {

	/*************************
	 * Web Token
	 *******************************************/

	/**
	 * 新增Token
	 * 
	 * @param userId
	 * @param status
	 * @param userRole
	 * @param loginSource
	 * @return
	 */
	public String createToken(Integer userId, Integer status, Integer userRole, LoginSource loginSource);

	/**
	 * 根据Token和请求来源删除Token
	 * 
	 * @param token
	 * @param loginSource
	 * @return
	 */
	public boolean deleteToken(String token, LoginSource loginSource);

	/**
	 * 清空用户所有Token
	 * 
	 * @param userId
	 */
	public void cleanTokenByUserId(Integer userId);

	/**
	 * 通过Token和请求来源获取UserId
	 * 
	 * @param token
	 * @param loginSource
	 * @return
	 */
	public Integer getUserIdByToken(String token, LoginSource loginSource);

	/**
	 * 通过Token获取UserId
	 * 
	 * @param token
	 * @return
	 */
	public Integer getUserIdByToken(String token);

	/**
	 * 获取用户状态
	 * 
	 * @param userId
	 * @return
	 */
	public UserStatus getUserStatus(Integer userId);

	/**
	 * 获取用户角色
	 * 
	 * @param userId
	 * @return
	 */
	public UserRole getUserRole(Integer userId);

	/**
	 * 更新用户状态
	 * 
	 * @param userId
	 * @param newStatus
	 */
	public void updateUserStatusIfPresent(Integer userId, UserStatus newStatus);

	/**
	 * 刷新Token失效时间
	 * 
	 * @param platformId
	 * @param token
	 */
	public boolean refreshToken(String token, LoginSource loginSource);

	public String getSecretKey(Integer platformId, String deviceId);

	public void updateSecretKey(Integer platformId, String deviceId, String secretKey);
}
