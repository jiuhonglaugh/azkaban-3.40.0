package azkaban.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;


public class PropertiesUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);
    private static final Properties zkPro = new Properties();

    /**
     * 获取 resources 目录中的 zookeeper.properties 配置文件
     */
    public PropertiesUtils() {
        LOGGER.info("从 Resouces目录中 加载 配置文件");
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("zookeeper.properties");
        getPro(in);
    }

    /**
     * 获取 对应 path 目录中的 zookeeper.properties 配置文件
     *
     * @param path zookeeper.properties 配置文件 目录
     */
    public PropertiesUtils(String path) {
        LOGGER.info("从 " + path + " 目录中 加载 配置文件");
        File file = new File(path);
        if (!file.exists()) {
            LOGGER.error(path + " 配置文件不存在 ");
            LOGGER.error("取消配置 AZKABAN_HA=true 参数或者配置正确的文件路径 ");
            System.exit(-1);
        }
        try {
            getPro(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getPro(InputStream inputStream) {
        try {
            zkPro.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据 key 获取对应 String 类型的值
     *
     * @param key
     * @return
     */
    public String getStr(String key) {
        return zkPro.getProperty(key).trim();
    }

    /**
     * 根据 key 获取对应 String 类型的值
     * 没有值时设置默认值
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return
     */
    public String getStr(String key, String defaultValue) {
        String property = zkPro.getProperty(key, defaultValue);
        if (property.length() == 0) {
            return defaultValue;
        }
        return property.trim();
    }

    /**
     * 根据 key 获取对应 Integer 类型的值
     *
     * @param key
     * @return
     */
    public Integer getInteger(String key) {
        return Integer.valueOf(zkPro.getProperty(key).trim());
    }

    /**
     * 根据 key 获取对应 Long 类型的值
     *
     * @param key
     * @return
     */
    public Long getLong(String key) {
        return Long.valueOf(zkPro.getProperty(key));
    }
}
