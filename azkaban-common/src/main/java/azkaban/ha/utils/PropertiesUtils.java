//package azkaban.ha.utils;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.*;
//import java.util.Properties;
//
//
//public class PropertiesUtils {
//    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);
//    private String ZK_CONF_NAME = "zookeeper.properties";
//    private static final Properties zkPro = new Properties();
//
//    /**
//     * 获取 resources 目录中的 zookeeper.properties 配置文件
//     */
//    public PropertiesUtils() {
//        LOGGER.info("从 Resouces目录中 加载 zookeeper.properties 配置文件");
//        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(ZK_CONF_NAME);
//        getPro(in);
//    }
//
//    /**
//     * 获取 对应 path 目录中的 zookeeper.properties 配置文件
//     *
//     * @param path zookeeper.properties 配置文件 目录
//     */
//    public PropertiesUtils(String path) {
//        String zkConfPath = path + "/" + ZK_CONF_NAME;
//        LOGGER.info("从 " + path + " 目录中 加载 " + ZK_CONF_NAME + " 配置文件");
//        File file = new File(path);
//        if (!file.exists()) {
//            LOGGER.error("" + path + " 目录中不存在 " + ZK_CONF_NAME + " 配置文件");
//            System.out.println("" + path + " 目录中不存在 " + ZK_CONF_NAME + " 配置文件");
//            System.exit(-1);
//        }
//        try {
//            getPro(new FileInputStream(zkConfPath));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void getPro(InputStream inputStream) {
//        try {
//            zkPro.load(inputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * 根据 key 获取对应 String 类型的值
//     *
//     * @param key
//     * @return
//     */
//    public String getStr(String key) {
//        return zkPro.getProperty(key);
//    }
//
//    /**
//     * 根据 key 获取对应 Integer 类型的值
//     *
//     * @param key
//     * @return
//     */
//    public Integer getInteger(String key) {
//        return Integer.valueOf(zkPro.getProperty(key));
//    }
//
//    /**
//     * 根据 key 获取对应 Long 类型的值
//     * @param key
//     * @return
//     */
//    public Long getLong(String key) {
//        return Long.valueOf(zkPro.getProperty(key));
//    }
//}
