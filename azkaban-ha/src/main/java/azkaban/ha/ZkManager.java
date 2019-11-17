package azkaban.ha;

import azkaban.ha.impl.AzkabanHa;
import azkaban.ha.utils.PropertiesUtils;
import azkaban.ha.utils.ZookeeperUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * FileName: ZkManager
 * Author:   MAIBENBEN
 * Date:     2019/11/8 9:40
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class ZkManager implements AzkabanHa {

    private static String zkHost;
    private static String zkPath;
    private static int sessionTimeOut;
    private static ZookeeperUtil zkUtil;
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkManager.class);
    private static final String DEFAULT_AZKABAN_HOST_ID = getHostUUID();
    private static String USER_AZKABAN_HOST_ID;


    /**
     * 初始化基础参数
     *
     * @param confPath zookeeper.properties 配置文件的文件夹路径
     */
    public ZkManager(String confPath) {
        LOGGER.info("初始化 zkManager");
        PropertiesUtils zkPro = new PropertiesUtils(confPath);
        USER_AZKABAN_HOST_ID = zkPro.getStr("AZKABAN_HOST_ID", DEFAULT_AZKABAN_HOST_ID);
        zkHost = zkPro.getStr("ZK_HOST");
        sessionTimeOut = zkPro.getInteger("SESSION_TIME_OUT");
        zkPath = zkPro.getStr("AZKABAN_HA_ZK_PATH");
        zkUtil = new ZookeeperUtil(zkHost, sessionTimeOut);
    }

    /**
     * 首先查看 zk 中是否存在此节点
     * 如果存在则查看节点中存储的数据信息是不是此节点的信息
     * 如果是就返回 true 确定此节点为 active
     * 如果 zk 中不存在此节点则创建此节点，如果创建失败
     * <p>
     * 创建 zk 临时节点
     *
     * @return 判断此节点是主还是备 true 代表此节点为主节点
     */
    @Override
    public boolean getStatus() {
        boolean flag = false;
        boolean exists = zkUtil.exists(zkPath);
        if (exists) {
            String zkData = zkUtil.getZkData(zkPath);
            LOGGER.info(zkPath + "： 临时节点已经存在");
            LOGGER.info(zkPath + "： 临时节点中的信息为： " + zkData);
            if (zkData.equals(USER_AZKABAN_HOST_ID))
                flag = true;
        } else {
            flag = zkUtil.creteNode(zkPath, USER_AZKABAN_HOST_ID, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, false);
            if (!flag) {
                String zkData = zkUtil.getZkData(zkPath);
                if (zkData.equals(USER_AZKABAN_HOST_ID)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * 获取本机 主机名称 和 ip
     *
     * @return 返回 ip 地址
     */
    private static String getHostUUID() {
        String uuid = UUID.randomUUID().toString();
        LOGGER.info("默认主机 HOST—UUID 为 " + uuid);
        try {
            InetAddress addr = InetAddress.getLocalHost();
            uuid = addr.getHostAddress() + "@" + addr.getHostName();
            LOGGER.info("根据主机 IP地址 和 主机名 生成 默认的HOST—UUID 为：" + uuid);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return uuid;
    }
}
