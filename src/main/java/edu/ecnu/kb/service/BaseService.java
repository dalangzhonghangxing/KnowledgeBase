package edu.ecnu.kb.service;

import edu.ecnu.kb.service.upload.RowProcessor;
import edu.ecnu.kb.service.upload.UploadProcessor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseService {

    protected final Sort SORT_ID_DESC = new Sort(Sort.Direction.DESC, "id");

    protected final Sort SORT_ID_ASC = new Sort(Sort.Direction.ASC, "id");

    /**
     * 解析上传文件并保存，返回保存的数量
     *
     * @param file         待解析文件
     * @param rowProcessor 对每一行的解析器
     * @param tag          进度条的key
     * @param target       目标位置
     * @param repository
     * @return
     */
    public Map<String, Object> uploadProcess(InputStream file, RowProcessor rowProcessor, String tag, List target
            , JpaRepository repository) {
        UploadProcessor.process(file, rowProcessor, tag, target);
        repository.saveAll(target);
        Map<String, Object> res = new HashMap<>();
        res.put("size", target.size());
        return res;
    }
}
