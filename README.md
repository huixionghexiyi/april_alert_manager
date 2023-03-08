# 告警抽象

alert manager 的java 版本实现

# concept

alert-->judge-->event-->inhibit-->silence-->notify

## alert

指 prometheus或其他第三方 推送过来的告警信息，格式为：

```json
[
  {
    "annotations": {
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

