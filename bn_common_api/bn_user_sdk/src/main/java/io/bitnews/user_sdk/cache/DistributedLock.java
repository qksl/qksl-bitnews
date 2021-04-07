package io.bitnews.user_sdk.cache;

import io.bitnews.user_sdk.constant.AppEnum;
import io.bitnews.user_sdk.exception.BNException;

public interface DistributedLock {

	/**
	 * It returns false if it doesn't acquire lock.
	 * 
	 * @param module
	 * @param method
	 * @param seconds
	 * @return
	 * @throws BNException
	 */
	boolean tryLock(AppEnum module, String method, int seconds) throws BNException;
	
	/**
	 *  It throws TO_FASTER exception if it doesn't acquire lock.
	 * @param module
	 * @param method
	 * @param seconds
	 * @return
	 * @throws BNException
	 */
	void lock(AppEnum module, String method, int seconds) throws BNException;
	
	/**
	 * Unlock by current thread
	 * @throws BNException
	 */
	void unlock() throws BNException;

}
