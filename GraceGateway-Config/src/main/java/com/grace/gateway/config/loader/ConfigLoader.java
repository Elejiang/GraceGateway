package com.grace.gateway.config.loader;


import com.grace.gateway.config.config.Config;
import com.grace.gateway.config.util.ConfigUtil;

import static com.grace.gateway.common.constant.ConfigConstant.CONFIG_PATH;
import static com.grace.gateway.common.constant.ConfigConstant.CONFIG_PREFIX;

/**
 * 配置加载
 */
public class ConfigLoader {

    public static Config load(String[] args) {
        return ConfigUtil.loadConfigFromYaml(CONFIG_PATH, Config.class, CONFIG_PREFIX);
    }

}
