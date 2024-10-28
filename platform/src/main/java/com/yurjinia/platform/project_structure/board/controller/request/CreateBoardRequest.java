package com.yurjinia.platform.project_structure.board.controller.request;

import com.yurjinia.platform.project_structure.project.validation.UpperCase;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateBoardRequest {

    @NotBlank
    private String name;

    @NotBlank
    @UpperCase
    private String code;

}
