package com.tiss.vitagergateway.mapper;

import com.tiss.vitagergateway.dto.UserRequestDto;
import com.tiss.vitagergateway.dto.UserResponseDto;
import com.tiss.vitagergateway.entity.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserResponseMapper {
    UserResponseMapper INSTANCE = Mappers.getMapper(UserResponseMapper.class);

    UserResponseDto UserResponseToUUserResponseDTO(UserRequest user);
    UserRequest UUserResponseDTOToUserResponse(UserResponseDto user);
}
