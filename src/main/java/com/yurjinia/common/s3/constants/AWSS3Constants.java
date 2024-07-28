package com.yurjinia.common.s3.constants;

import org.springframework.beans.factory.annotation.Value;

public class AWSS3Constants {

    @Value("${APP.MAIN_PACKAGE}")
    public static String MAIN_PACKAGE;
    public static final String FORWARD_SLASH = "/";

    @Value("${APP.DEFAULT_AVATAR_NAME}")
    public static String DEFAULT_AVATAR_NAME;//ToDO: Implement the adaptation of the image to PNG format

}
