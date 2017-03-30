package com.nowcoder.async;

import java.util.List;

/**
 * Created by Jacinth on 2017/3/27.
 * Event统一的接口。每一个handler对事件处理是不同的
 */
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();//发生EventType就要处理
}
