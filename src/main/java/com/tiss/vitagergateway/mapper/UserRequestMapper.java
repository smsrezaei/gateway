package com.tiss.vitagergateway.mapper;

import com.tiss.vitagergateway.dto.UserRequestDto;
import com.tiss.vitagergateway.entity.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserRequestMapper {
    UserRequestMapper INSTANCE = Mappers.getMapper(UserRequestMapper.class);

    UserRequestDto UserRequestToUserRequestDTO(UserRequest user);
    UserRequest UserRequestDTOToUserRequest(UserRequestDto user);
}

//https://www.baeldung.com/mapstruct