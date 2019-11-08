//import utils.RegisterNode;

import com.google.inject.internal.cglib.proxy.$NoOp;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import utils.PropertiesUtils;
import utils.ZookeeperUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * FileName: Test
 * Author:   MAIBENBEN
 * Date:     2019/11/7 12:45
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class Test {
    public static void main(String[] args) throws IOException {

        while (true) {
            ZkManager zkManager = new ZkManager("");
            boolean status = zkManager.getStatus();
            if (status) {
                System.out.println("路径创建成功，此节点为： active ====================================");
            } else {
                System.out.println("路径已经存在，此节点为： standby ====================================");
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
