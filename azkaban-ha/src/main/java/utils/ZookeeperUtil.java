package utils;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

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
    private String zkHost;
    private int sessionTimeOut = 2000;

    /**
     * 设置主机地址和 回话超时时间 若果设置的时间小于 2000 将会使用默认值 2000
     *
     * @param zkHost
     * @param sessionTimeOut
     */
    public ZookeeperUtil(String zkHost, int sessionTimeOut) {
        this.zkHost = zkHost;
        if (sessionTimeOut > 2000)
            this.sessionTimeOut = sessionTimeOut;
    }

    /**
     * 初始化 zk 客户端
     *
     * @return
     */
    public ZooKeeper initZkClient() {

        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(zkHost, sessionTimeOut, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zk;
    }

    /**
     * 获取 zkPath 路径下的子节点
     *
     * @param zkPath
     * @param watch
     * @return
     */
    public List<String> getChildren(String zkPath, boolean watch) {
        List<String> zooChildren = new ArrayList<String>();
        ZooKeeper zk = initZkClient();
        if (zk != null) {
            try {
                System.out.println("Znodes of ' " + zkPath + " ' ");
                zooChildren = zk.getChildren(zkPath, watch);
                return zooChildren;
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                close(zk);
            }
        }
        return zooChildren;
    }

    /**
     * 关闭 zk 客户端连接
     *
     * @param zkCli
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
     * @return
     */
    public boolean creteNode(String path, String data, List<ACL> acl, CreateMode mode, boolean isCloseSession) {
        ZooKeeper zkClient = initZkClient();
        String result = "";
        boolean flag = false;

        try {
            result = zkClient.create(path, data.getBytes(Charset.defaultCharset()), acl, mode);
            System.out.println(path + "路径创建成功，此节点为： active ");
            if (null != result && result.equals(path))
                flag = true;
            return flag;
        } catch (KeeperException e) {
        } catch (InterruptedException e) {
        } finally {
            if (!flag || isCloseSession) {
                close(zkClient);
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
