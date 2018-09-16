package edu.ecnu.kb.service.upload;

/**
 * 用于处理上传文件中的每一行
 */
public interface RowProcessor {
    Object processor(String[] line);
}
