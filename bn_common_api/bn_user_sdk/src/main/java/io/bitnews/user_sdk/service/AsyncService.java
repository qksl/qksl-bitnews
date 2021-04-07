package io.bitnews.user_sdk.service;

import io.bitnews.user_sdk.exception.BNException;

public interface AsyncService {
	void asyncInvoke(AsyncExec consumer) throws BNException;

	@FunctionalInterface
	interface AsyncExec {
		void exec() throws BNException;
	}
}
