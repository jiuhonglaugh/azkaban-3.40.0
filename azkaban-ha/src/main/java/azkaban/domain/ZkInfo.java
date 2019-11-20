package azkaban.domain;

/**
 * FileName: ZkInfo
 * Author:   MAIBENBEN
 * Date:     2019/11/20 23:15
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class ZkInfo {
    private String zkHost;
    private String zkPath;

    public ZkInfo(String zkHost, String zkPath, int timeOut) {
        this.zkHost = zkHost;
        this.zkPath = zkPath;
        this.timeOut = timeOut;
    }

    private int timeOut;

    public String getZkHost() {
        return zkHost;
    }

    public String getZkPath() {
        return zkPath;
    }

    public int getTimeOut() {
        return timeOut;
    }
}
