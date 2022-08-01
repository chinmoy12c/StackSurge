package com.stacksurge.StackSurge.utility;

import org.springframework.core.io.FileSystemResource;

public class Constants {
    public static final String SCRIPTS_HOME = new FileSystemResource("").getFile().getAbsolutePath();
    public static final String CADDY_PASS_GEN_CONTAINER = "caddypassgen";
    public static final String CADDY_CONNECTOR = "caddy";
    public static final String DOCKER_NETWORK = "stacksurge";
    // TODO: change this
    public static final String HASH_SECRET_KEY = "ZjFlNzQ1ZjA1ODUzZWUzZmY3Y2E3ZTJlODVhMTA3MzE3M2Y0OTc0ZGQ3NmQz";
    // TODO: change this
    public static final String JWT_SECRET_KEY = "NDAxNDQ5NTE5MGU0NzI1OTE5NDIwNDI4MTVkMjZkMWU5ZGY4NjdmNDJiN2U4";
    public static final long JWT_EXPIRE_TIME = 1800000;
}
