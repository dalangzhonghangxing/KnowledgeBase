package edu.ecnu.kb.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用于对controller进行公共配置，或进行统一方法的封装。
 */
@CrossOrigin(maxAge = 3600, origins = "*", allowCredentials = "true")
@RequestMapping(value = "/api")
public abstract class BaseController {
}
