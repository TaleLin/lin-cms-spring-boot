# websocket 模块

> 与前端即时通信模块，目前使用在消息推送方面

## 开启

websocket 模块默认是关闭的，它的启用十分简单。只需增加上一个简单的配置即可，
如我们需要在开发环境中启用 websocket，那么只需在`application-dev.properties`
配置文件中加上如下配置：

```properties
# websocket
lin.cms.websocket.enable=true
```

websocket 模块开启后，对外暴露的连接地址为`ws/message`。

## 连接

wsc 是一个方便的 websocket 命令行工具，我们将使用它作为本小节的测试工具。
在实际的开发中，请务必选择合适的 websocket 客户端。

### 安装 wsc

```npm
npm install -g wsc
```

### 使用 wsc

wsc 的使用十分简单，如下，通过 wsc 连接本地的 websocket 端口：

```bash
wsc "ws://127.0.0.1:5000/ws/message"
```

连接成功后，客户端与服务端就可以双向通信了。

```bash
Connected to ws://127.0.0.1:5000/ws/message
>
```

## 推送

websocket 模块提供了 WsHandler 服务接口来向客户端推送消息，通过依赖注入拿到
WsHandler后，就可以方便地向客户端发送消息。

### 广播

你可以在任何地方，但必须是在 spring 容器内通过 WsHandler 来发送消息，比如我们
可以在客户端请求某个接口的时候来广播一条消息。

如下：

```java
@RestController
@RequestMapping("/cms/test")
public class TestController {

    @Autowired
    private WsHandler wsHandler;

    @RequestMapping("")
    public String index() {
        try {
            wsHandler.broadCast("广播一条消息");
        } catch (IOException e) {
            e.printStackTrace();
        }
    // 省略其它代码
    }
}
```

当其它客户端请求`/cms/test`接口时，当前的客户端便可接收到广播的消息：

```bash
< 广播一条消息
```

### 个人

广播消息时，任何一个连接的客户端都会收到消息，但是有时候我们只需给特定的某个
客户端（用户）发送一条消息，WsHandler 自然也支持这种做法。

```java

@RestController
@RequestMapping("/cms/test")
public class TestController {

    @Autowired
    private WsHandler wsHandler;

    @RequestMapping("")
    public String index() {
        Optional<WebSocketSession> session = wsHandler.getSessions().stream().findFirst();
        if (session.isPresent()) {
            try {
                wsHandler.sendMessage(session.get(), "对第一个会话发送了一条消息");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    // 省略其它代码
    }
}
```

这里，我们通过 WsHandler 的 getSessions 方法拿到了所有的连接会话，并
通过 sendMessage 方法向第一个会话发送了消息。

结果如下：

```bash
Connected to ws://127.0.0.1:5000/ws/message
< 对第一个会话发送了一条消息
>
```

### 群组

lin-cms 中是有分组这个概念存在的，WsHandler 支持向某个分组来发送消息，这个
分组中的连接用户都会收到这个消息。

::: warn
注意，我们并不能在不知道客户端身份信息的情况下进行分组消息推送。
:::

因此，我们需要在客户端进行 websocket 连接的时候传入通信凭证，也就是我们
lin-cms 一直在使用的令牌，并且开启 websocket 的鉴权拦截器，将非 lin-cms
的用户拦截在外。

在`application-dev.properties`中添加下面的配置项：

```properties
lin.cms.websocket.intercept=true
```

当开启拦截器后，使用客户端连接 websocket 时，必须携带用户的令牌信息，否则连接会被拒绝。

然后再次连接：

```bash
wsc "ws://127.0.0.1:5000/ws/message?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZGVudGl0eSI6MSwic2NvcGUiOiJsaW4iLCJ0eXBlIjoiYWNjZXNzIiwiZXhwIjoxNTg0MDk4Njg4fQ.kfKPoTf4j06KiFgOYn5TwKj4HYawQC9KyQ77kEttQu4"
```

::: tip
websocket 无法使用 header 传递令牌，因此这里使用 url params 携带令牌。
:::

然后，使用 WsHandler 提供的分组API向分组发送消息。

如下：

```java
@RestController
@RequestMapping("/cms/test")
public class TestController {

    @Autowired
    private WsHandler wsHandler;

    @RequestMapping("")
    public String index() {
        try {
            wsHandler.broadCastToGroup(1L, "对第一个分组发送了一条消息");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 省略其它代码
    }
}
```

id 为 1 的分组用户都会收到如下消息：

```bash
Connected to ws://127.0.0.1:5000/ws/message?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZGVudGl0eSI6MSwic2NvcGUiOiJsaW4iLCJ0eXBlIjoiYWNjZXNzIiwiZXhwIjoxNTg0MDk4Njg4fQ.kfKPoTf4j06KiFgOYn5TwKj4HYawQC9KyQ77kEttQu4
< 对第一个分组发送了一条消息
```

## WsHandler API

WsHandler 是 lin-cms 提供给开发者的消息推送接口，该接口有许多直接可用的API，
上面我们介绍到了一些，接下来我们来总结一下。

### sendMessage

给某个会话发送消息，有两个重载方法，如下：

```
/**
 * 发送消息
 *
 * @param session 当前会话
 * @param message 要发送的消息
 * @throws IOException 发送io异常
 */
void sendMessage(WebSocketSession session, String message) throws IOException;

/**
 * 发送消息
 *
 * @param session 当前会话
 * @param message 要发送的消息
 * @throws IOException 发送io异常
 */
void sendMessage(WebSocketSession session, TextMessage message) throws IOException;
```

方法的第二个参数可以直接接收字符串，也可以接收 TextMessage 类型的消息。

### broadCast

广播，给所有连接用户发送消息，也有两个重载方法，第一个直接发送字符串，
另一个发送 TextMessage 类型的消息。

```
/**
 * 广播
 *
 * @param message 字符串消息
 * @throws IOException 异常
 */
void broadCast(String message) throws IOException;

/**
 * 广播
 *
 * @param message 文本消息
 * @throws IOException 异常
 */
void broadCast(TextMessage message) throws IOException;
```

### broadCastToGroup

给某个分组广播消息，两个重载方法，第一个直接发送字符串，
另一个发送 TextMessage 类型的消息。

```
/**
 * 对某个分组广播
 *
 * @param groupId 分组id
 * @param message 消息
 * @throws IOException 异常
 */
void broadCastToGroup(Long groupId, String message) throws IOException;

/**
 * 对某个分组广播
 *
 * @param groupId 分组id
 * @param message 文本消息
 * @throws IOException 异常
 */
void broadCastToGroup(Long groupId, TextMessage message) throws IOException;
```

### getSessions

获得所有连接的会话。

```
/**
 * 获得所有的 websocket 会话
 *
 * @return 所有 websocket 会话
 */
Set<WebSocketSession> getSessions();
```

### getConnectionCount

获得当前连接数

```
/**
 * 得到当前连接数
 *
 * @return 连接数
 */
int getConnectionCount();
```

## 总结

websocket 模块是一个非常好用且常见的模块，当你在应用中需要它时，只需一个配置就可以
快速的使用到它了。

当然在你打开 websocket 时，我们推荐你也一起打开`lin.cms.websocket.intercept`，
这会极大的提高通信的安全。