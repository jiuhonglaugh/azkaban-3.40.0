package azkaban;

import azkaban.utils.Props;

public interface AzkabanHaControl {
    /**
     * 将azkaban.properties 配置文件参数传给 ha 服务
     *
     * @param pro azkaban.properties 配置文件
     */

    void setParam(Props pro);

    /**
     * 判断此节点是不是主节点
     * True     执行任务
     * False    不执行任务
     *
     * @return
     */
    boolean getStatus();
}
