package com.tiss.vitagergateway.service;

import com.tiss.vitagergateway.dto.UserRequestDto;
import com.tiss.vitagergateway.dto.UserResponseDto;
import com.tiss.vitagergateway.entity.UserRequest;
import com.tiss.vitagergateway.mapper.UserRequestMapper;
import com.tiss.vitagergateway.mapper.UserResponseMapper;
import com.tiss.vitagergateway.properties.VitagerProperties;
import com.tiss.vitagergateway.repository.UserRequestRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
@Slf4j
public class UserRequestService {
    private final VitagerProperties props;
    private final RestTemplate restTemplate;
    private final UserRequestRepository requestRepository;
    //private final UserRequestMapper userMapper = Mappers.getMapper(UserRequestMapper.class);
    //private final UserResponseMapper userRresponseMapper = Mappers.getMapper(UserResponseMapper.class);

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    public UserRequestService(VitagerProperties props, RestTemplate restTemplate, UserRequestRepository requestRepository) {
        this.props = props;
        this.restTemplate = restTemplate;
        this.requestRepository = requestRepository;
    }

    @SneakyThrows
    public UserResponseDto CreateRequest(UserRequestDto userRequestDto) {
        UserRequest entity = UserRequestMapper.INSTANCE.UserRequestDTOToUserRequest(userRequestDto) ;
        entity.setDate(java.sql.Date.valueOf(LocalDate.now()));
        entity.setSuccess(false);

        UserRequest saveresult =  requestRepository.save(entity);
        UserResponseDto result = UserResponseMapper.INSTANCE.UserResponseToUUserResponseDTO(saveresult);
        return result;
    }

}
