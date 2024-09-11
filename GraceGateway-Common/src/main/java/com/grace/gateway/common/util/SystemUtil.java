package com.grace.gateway.common.util;


public class SystemUtil {
	
    public static final String OS_NAME = System.getProperty("os.name");

    private static boolean isLinuxPlatform = false;

    private static boolean isWindowsPlatform = false;

    static {
        if (OS_NAME != null && OS_NAME.toLowerCase().contains("linux")) {
            isLinuxPlatform = true;
        }

        if (OS_NAME != null && OS_NAME.toLowerCase().contains("windows")) {
            isWindowsPlatform = true;
        }
    }

    public static boolean isWindowsPlatform() {
        return isWindowsPlatform;
    }

    public static boolean isLinuxPlatform() {
        return isLinuxPlatform;
    }

}
