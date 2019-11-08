package domain;

/**
 * FileName: ZkInfo
 * Author:   MAIBENBEN
 * Date:     2019/11/8 12:17
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class ZkInfo {
    private String data;
    private boolean isTrue;
    private String status;
    private String zkPath;

    public String getZkPath() {
        return zkPath;
    }

    public void setZkPath(String zkPath) {
        this.zkPath = zkPath;
    }



    public void setData(String data) {
        this.data = data;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public String getStatus() {
        return status;
    }

}
