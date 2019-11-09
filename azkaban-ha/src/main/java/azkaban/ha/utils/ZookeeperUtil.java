package azkaban.ha.utils;


import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
        ZooKeeper zk = null;
        try {
            return new ZooKeeper(zkHost, defaultSessionTimeOut, new MyWatcher());
        } catch (IOException e) {
            LOGGER.error(" ======================== 初始化 ZkClient 失败 ======================== ");
            LOGGER.error(e.getMessage());
            System.exit(-1);
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
            LOGGER.info(path + "在 Zookeeper 中创建成功，此节点为： Active ");
            if (null != result && result.equals(path))
                flag = true;
            return flag;
        } catch (KeeperException e) {
            LOGGER.warn(" ============================ Zk 中已存在 " + path + " 路径：注册临时节点失败 ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (!flag || isCloseSession) {
                LOGGER.info(" ============================ Zk Connect Is Closed ============================ ");
                close(zkClient);
            } else {
                LOGGER.warn(" ============================ Zk Connect Is Open ============================  ");
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
            return result;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            close(zkClient);
        }
        return result;
    }

}


class MyWatcher implements Watcher {
    @Override
    public void process(WatchedEvent event) {
    }
}
