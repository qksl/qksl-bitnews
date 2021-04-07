package io.bitnews.framework.file;

import java.io.InputStream;

/**
 * Created by ywd on 2019/11/21.
 */
public interface FileStorage {

    /**
     * 存储输入流
     * @param input
     * @param key
     */
    void store(InputStream input, String key);
}
