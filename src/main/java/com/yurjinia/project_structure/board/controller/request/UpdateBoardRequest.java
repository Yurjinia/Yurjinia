package com.yurjinia.project_structure.board.controller.request;

import com.yurjinia.project_structure.project.validation.UpperCase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBoardRequest {

    @UpperCase
    private String boardCode;

    private String boardName;

}
