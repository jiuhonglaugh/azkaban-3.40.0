package azkaban.ha;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import utils.PropertiesUtils;
import utils.ZookeeperUtil;

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

    private String zkHost;
    private String zkPath;
    private int sessionTimeOut;
    private ZookeeperUtil zkUtil;

    /**
     * 初始化基础参数
     *
     * @param confPath
     */
    public ZkManager(String confPath) {
        confPath = "D:\\workspaces\\src_idea\\azkaban-3.40.0\\azkaban-3.40.0\\azkaban-ha\\src\\main\\resources\\";
        PropertiesUtils zkPro = new PropertiesUtils(confPath);
        zkHost = zkPro.getStr("ZK_HOST");
        sessionTimeOut = zkPro.getInteger("SESSION_TIME_OUT");
        zkPath = zkPro.getStr("AZKABAN_HA_ZK_PATH");
        new ZookeeperUtil(zkHost, sessionTimeOut);
        zkUtil = new ZookeeperUtil(zkHost, sessionTimeOut);
    }

    /**
     * 创建 zk 临时节点
     *
     * @return
     */
    public boolean getStatus() {
        String hostNameAndAddress = getHostNameAndAddress();
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
     * @return
     */
    private static String getHostNameAndAddress() {
        InetAddress addr = null;
        String hostAddress = "";
        String hostName = "";
        try {
            addr = InetAddress.getLocalHost();
            hostAddress = addr.getHostAddress();
            hostName = addr.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return hostName + hostAddress;
    }

    public static void main(String[] args) {
        ZkManager zkManager = new ZkManager("");
        String hiveserver2_zk = zkManager.zkUtil.getZkData("/hiveserver2_zk");
        System.out.println(hiveserver2_zk);
    }
}
