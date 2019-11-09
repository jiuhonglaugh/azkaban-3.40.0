package azkaban.ha;

import azkaban.ha.utils.PropertiesUtils;
import azkaban.ha.utils.ZookeeperUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * FileName: ZkManager
 * Author:   MAIBENBEN
 * Date:     2019/11/8 9:40
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class ZkManager {

    private static String zkHost;
    private static String zkPath;
    private static int sessionTimeOut;
    private static ZookeeperUtil zkUtil;

    /**
     * 初始化基础参数
     *
     * @param confPath  zookeeper.properties 配置文件的文件夹路径
     */
    public ZkManager(String confPath) {
        PropertiesUtils zkPro = new PropertiesUtils(confPath);
        zkHost = zkPro.getStr("ZK_HOST");
        sessionTimeOut = zkPro.getInteger("SESSION_TIME_OUT");
        zkPath = zkPro.getStr("AZKABAN_HA_ZK_PATH");
        zkUtil = new ZookeeperUtil(zkHost, sessionTimeOut);
    }

    /**
     * 创建 zk 临时节点
     *
     * @return      判断此节点是主还是备
     */
    public boolean getStatus() {
        String hostNameAndAddress = getAddress();
        boolean b = zkUtil.creteNode(zkPath, hostNameAndAddress, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, false);
        if (!b) {
            String zkData = zkUtil.getZkData(zkPath);
            if (zkData.equals(hostNameAndAddress)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 获取本机 主机名称 和 ip
     *
     * @return       返回 ip 地址
     */
    private String getAddress() {
        String address = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            address = addr.getHostAddress();
            return address;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address;
    }
}
