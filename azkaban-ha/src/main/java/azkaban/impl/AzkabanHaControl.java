package azkaban.impl;

public interface AzkabanHaControl {
    /**
     * 判断此节点是不是主节点
     * True     执行任务
     * False    不执行任务
     * @return
     */
    boolean getStatus();
}
