package azkaban.impl;

import azkaban.AzkabanHaControl;
import azkaban.utils.Props;
import azkaban.utils.ZookeeperUtil;
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
public class ZkAzkabanHaControl implements AzkabanHaControl {


    private static final Logger LOGGER = LoggerFactory.getLogger(ZkAzkabanHaControl.class);
    private static final String DEFAULT_AZKABAN_HOST_ID = getHostUUID();
    private static String zkHost;
    private static String zkPath;
    private static int SESSION_TIME_OUT;
    private static ZookeeperUtil zkUtil;
    private static String USER_AZKABAN_HOST_ID;

    @Override
    public void setParam(Props pro) {
        LOGGER.info("初始化 " + ZkAzkabanHaControl.class.getSimpleName());
        this.USER_AZKABAN_HOST_ID = pro.getString("azkaban.ha.zookeeper.my.id", DEFAULT_AZKABAN_HOST_ID);
        if ("".equals(this.USER_AZKABAN_HOST_ID))
            this.USER_AZKABAN_HOST_ID = DEFAULT_AZKABAN_HOST_ID;
        this.zkHost = pro.getString("azkaban.ha.zookeeper.host");
        this.SESSION_TIME_OUT = pro.getInt("azkaban.ha.zookeeper.session.timeout");
        this.zkPath = pro.getString("azkaban.ha.zookeeper.base.path", "/azkaban_ha");
        this.zkUtil = new ZookeeperUtil(zkHost, SESSION_TIME_OUT);
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
            if (!flag && zkUtil.getZkData(zkPath).equals(USER_AZKABAN_HOST_ID))
                flag = true;
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
        LOGGER.info("默认主机 HOST—UUID 为 ：" + uuid);
        try {
            InetAddress addr = InetAddress.getLocalHost();
            uuid = addr.getHostAddress() + "@" + addr.getHostName();
            LOGGER.info("根据主机 IP地址 和 主机名 生成 默认的HOST—UUID 为：" + uuid);
            LOGGER.info("如果在配置文件中设置了 azkaban.ha.zookeeper.my.id 默认生成的 HOST—UUID 将会被覆盖");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return uuid;
    }
}
