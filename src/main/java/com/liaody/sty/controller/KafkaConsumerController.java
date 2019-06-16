package com.liaody.sty.controller;

import org.apache.juli.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaConsumerController {

    protected final Logger logger = LoggerFactory.getLogger(KafkaConsumerController.class);

}
