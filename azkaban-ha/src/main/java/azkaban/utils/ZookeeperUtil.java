package azkaban.utils;


import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * FileName: ZookeeperUtil
 * Author:   MAIBENBEN
 * Date:     2019/11/7 12:45
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class ZookeeperUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperUtil.class);
    private static String zkHost;
    private static int defaultSessionTimeOut = 2000;

    /**
     * 设置主机地址和 回话超时时间 若果设置的时间小于 2000 将会使用默认值 2000
     *
     * @param zkAddress      zk 连接地址
     * @param sessionTimeOut zk 连接超时时间
     */
    public ZookeeperUtil(String zkAddress, int sessionTimeOut) {
        LOGGER.info("初始化 ZookeeperUtil 工具类");
        zkHost = zkAddress;
        if (sessionTimeOut > 2000)
            defaultSessionTimeOut = sessionTimeOut;
    }

    /**
     * 初始化 zk 客户端
     *
     * @return 获取zk连接
     */
    private ZooKeeper initZkClient() {

        try {
            ZooKeeper zooKeeper = new ZooKeeper(zkHost, defaultSessionTimeOut, new MyWatcher());
            return zooKeeper;
        } catch (IOException e) {
            LOGGER.error("初始化 ZkClient 失败");
            LOGGER.error(" ======================== zkHost: " + zkHost + " ======================== ");
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    /**
     * 获取 zkPath 路径下的子节点
     *
     * @param zkPath 在zookeeper中创建的路径
     * @param watch  watcher
     * @return 返回路径下的所有子节点
     */
    public List<String> getChildren(String zkPath, boolean watch) {
        List<String> zooChildren;
        ZooKeeper zk = initZkClient();
        try {
            LOGGER.info("Azkaban_Ha 在 Zookeeper 中的路径为：" + zkPath);
            zooChildren = zk.getChildren(zkPath, watch);
            return zooChildren;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            close(zk);
        }
        return null;
    }

    /**
     * 关闭 zk 客户端连接
     *
     * @param zkCli 关闭客户端连接
     */
    public static void close(ZooKeeper zkCli) {
        try {
            if (null != zkCli) {
                zkCli.close();
                LOGGER.info(" Zk Connect Is Closed ");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在 zk 中创建临时节点
     *
     * @param path           zk 中的路径
     * @param acl            zk 的权限
     * @param mode           zk 节点类型
     * @param isCloseSession 是否关闭 zk 连接 如果关闭连接 临时节点 会马上删除
     * @return 是否成功
     */
    public boolean creteNode(String path, String data, List<ACL> acl, CreateMode mode, boolean isCloseSession) {
        ZooKeeper zkClient = initZkClient();
        boolean flag = false;
        try {
            String result = zkClient.create(path, data.getBytes(Charset.defaultCharset()), acl, mode);
            if (null != result && result.equals(path))
                flag = true;
        } catch (KeeperException e) {
            LOGGER.warn(e.getMessage());
        } catch (InterruptedException e) {
            LOGGER.warn(e.getMessage());
        } finally {
            if (!flag || isCloseSession) {
                close(zkClient);
            } else {
                LOGGER.warn(" Zk Connect Is Open ");
            }
        }
        return flag;
    }

    /**
     * 获取节点数据
     *
     * @param zkPath
     * @return
     */
    public String getZkData(String zkPath) {
        ZooKeeper zkClient = initZkClient();
        String result = "";
        try {
            byte[] data = zkClient.getData(zkPath, false, null);
            result = new String(data, Charset.defaultCharset());
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            close(zkClient);
        }
        return result;
    }

    /**
     * 判断zk路径是否存在
     *
     * @param path zk中的路径
     * @return true 为存在
     */
    public boolean exists(String path) {
        boolean flag = false;
        ZooKeeper zkClient = initZkClient();
        try {
            Stat exists = zkClient.exists(path, true);
            if (exists != null)
                flag = true;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            close(zkClient);
        }
        return flag;
    }

}


class MyWatcher implements Watcher {
    @Override
    public void process(WatchedEvent event) {
    }
}
