# 告警抽象

alert manager 的java 版本实现

# concept

alert-->judge-->event-->inhibit-->silence-->notify

- alert: 告警，告警源 推送到 april_alert_manager 的消息
- judge: 判定，对一批 alert 进行处理，并生成一个 event
- event: 事件，表示一个预期的故障被发现
- inhibit: 抑制，一个 event 是否被更重要的event所抑制
- silence: 静默，手动设置，满足某个条件的 event 不发出 notify
- notification: 通知，如果一个事件没有被inhibit，且没有被 silence，就会发出 notify

## alert

指 prometheus或其他第三方 推送过来的告警信息，格式为：

```json
[
  {
    "annotations": {
      "desc": "${job}触发了告警"
    },
    "endsAt": "2023-02-16T12:09:15.797Z",
    "startsAt": "2023-02-16T11:03:15.797Z",
    "generatorURL": "http://72ebf0bb55da:9090/graph?g0.expr=up%7Bjob%3D%22hx-mysql%22%7D+%3D%3D+0&g0.tab=1",
    "labels": {
      "alertname": "mysql:alert",
      "instance": "10.0.20.234:9104",
      "job": "hx-mysql",
      "severity": "critical"
    }
  },
  {
    ...
  }
]
```

## judge

```java
public interface JudgeService {
    EventOption judge(List<Alert> alerts, JudgeContext context);
}
```

3. inhibit 抑制
4. silence 静默
5. event 时间
6. notify 通知

