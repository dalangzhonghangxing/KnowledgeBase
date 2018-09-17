package edu.ecnu.kb.service;

import edu.ecnu.kb.service.upload.RowProcessor;
import edu.ecnu.kb.service.upload.UploadProcessor;
import org.hibernate.boot.jaxb.hbm.spi.JaxbHbmParentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseService {

    protected final Sort SORT_ID_DESC = new Sort(Sort.Direction.DESC, "id");

    protected final Sort SORT_ID_ASC = new Sort(Sort.Direction.ASC, "id");

    /**
     * 解析上传文件并保存，返回保存成功的数量
     *
     * @param file         待解析文件
     * @param rowProcessor 对每一行的解析器
     * @param tag          进度条的key
     * @param target       目标位置
     * @param repository
     * @return
     */
    @Transactional
    public Map<String, Object> uploadProcess(InputStream file, RowProcessor rowProcessor, String tag, List target
            , JpaRepository repository) {
        UploadProcessor.process(file, rowProcessor, tag, target);
        int size = 0;
        for (Object o : target) {
            try {
                size++;
                repository.save(o);
            } catch (Exception e) {
                // 如果出现异常，则应该是重复。
                size--;
            }
        }
        Map<String, Object> res = new HashMap<>();
        res.put("size", size);
        return res;
    }

    /**
     * 批量保存，能够为了避免事物过大导致update或insert失败
     *
     * @param target     待保存列表
     * @param repository
     */
    public void batchSave(List target, JpaRepository repository) {
        int begin = 0;
        int end = begin + 200;
        while (end <= target.size()) {
            batchSave(target, begin, end, repository);
            begin = end;
            end = begin + 200;
        }
        // 将剩下不足一个batch的数据进行保存
        batchSave(target, begin, target.size(), repository);
    }

    /**
     * 批量保存，能够为了避免事物过大导致update或insert失败
     *
     * @param target     待保存列表
     * @param begin      开始位置
     * @param end        结束位置
     * @param repository
     */
    @Transactional
    public void batchSave(List target, int begin, int end, JpaRepository repository) {
        if (target.size() > 0)
            repository.saveAll(target.subList(begin, end));
    }

    /**
     * 分页获取对象
     *
     * @param page
     * @param size
     * @param repository
     * @return
     */
    public Page getByPage(Integer page, Integer size, JpaRepository repository) {
        return repository.findAll(PageRequest.of(page - 1, size, SORT_ID_DESC));
    }
}
