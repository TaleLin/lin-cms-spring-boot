# websocket 模块

> 与前端即时通信模块，目前使用在消息推送方面

## 使用

websocket 默认关闭，在 io.github.talelin.merak.modules.message.WebsocketConfig 配置上
打上一个 @Configuration 注解即可：

```java
@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {
  // 省略代码
}
```

websocket 打开后，连接地址是`ws/message`。

使用客户端连接 websocket 时，必须携带用户的令牌信息，否则连接会被直接关闭。

如下，wsc 是 websocket 的命令客户端，供测试使用。

```bash
wsc "ws://127.0.0.1:5000/ws/message?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZGVudGl0eSI6Miwic2NvcGUiOiJsaW4iLCJ0eXBlIjoiYWNjZXNzIiwiZXhwIjoxNTgyNDQzNDk4fQ.4tKGcbb7eVs-wuH15gwPcGr4dziuj5xAu5dp3cWewks"
```

websocket 无法使用 header 传递令牌，因此这里使用 url params 携带令牌。

连接成功后，客户端与服务端就可以双向通信了。


**一个很麻烦的点，在 handshake 之前做权限校验时，当判断用户无法连接时，无法在连接中给一个错误的信息**