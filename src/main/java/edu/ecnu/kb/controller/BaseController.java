package edu.ecnu.kb.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(maxAge = 3600, origins = "*", allowCredentials = "true")
@RequestMapping(value = "/api")
public abstract class BaseController {
}
