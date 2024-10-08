package com.tiss.vitagergateway.controller;

import com.tiss.vitagergateway.dto.UserRequestDto;
import com.tiss.vitagergateway.dto.UserResponseDto;
import com.tiss.vitagergateway.entity.UserRequest;
import com.tiss.vitagergateway.mapper.UserRequestMapper;
import com.tiss.vitagergateway.service.UserRequestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("request")
@CrossOrigin(origins = "*")
@Slf4j
public class RequestController {
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private UserRequestService userRequestService;

    private static final Logger logger = LogManager.getLogger(RequestController.class);

    @PostMapping("/UserRequest")
    public ResponseEntity<UserResponseDto> GetToken(@RequestBody UserRequestDto user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Hello from Log4j  : {}", user.getNationalCode());
        }

        UserRequest entity = UserRequestMapper.INSTANCE.UserRequestDTOToUserRequest(user) ;

        UserResponseDto result = userRequestService.CreateRequest(user) ;

        logger.info("Hello from Log4j  : {}", result.getDate().toString());

        return ResponseEntity.ok(result);
    }
}
