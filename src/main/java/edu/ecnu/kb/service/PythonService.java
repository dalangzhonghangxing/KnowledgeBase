package edu.ecnu.kb.service;

import edu.ecnu.kb.service.util.PythonUtil;
import org.springframework.stereotype.Service;

@Service
public class PythonService extends BaseService {

    public void test(String name) {
        Thread thread = new Thread(() -> {
            PythonUtil.execute(name,"rnn/mnist.py");
        });
        thread.start();
    }
}
