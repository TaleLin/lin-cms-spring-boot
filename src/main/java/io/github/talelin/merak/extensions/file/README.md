---
title: 文件上传
---

# 文件上传

lin-cms 默认集成了文件上传功能，并可自由切换存储位置，前端也有相应的组件匹配使用。

## 使用

lin-cms 默认对外暴露了`cms/file/`作为文件上传接口，通过该接口可以直接使用 HTTP
post 方法上传文件。

文件默认会上传到本地，当前工作目录下的 assets 目录。

## 实践

文件上传是开箱即用的，使用 postman 测试一下文件上传：

<img-wrapper>
  <img src="http://imglf4.nosdn0.126.net/img/Qk5LWkJVWkF3NmlvOHFlZzFHSk95OGhiL0lSSFo3OFNPSGc1WEFnc0JRVERUb2JSU0cvSUlnPT0.png?imageView&thumbnail=2090y1120&type=png&quality=96&stripmeta=0">
</img-wrapper>

上传成功后，我们会得到如下的结果：

```
[
  {
    key: 'one',
    id: 13,
    url:
      'http://localhost:5000/assets/2019/05/19/3428c02f-dfb5-47a0-82cf-2d6c05285473.png'
  },
  {
    key: 'two',
    id: 14,
    url:
      'http://localhost:5000/assets/2019/05/19/bfcff6f4-8dd7-4dd8-af9d-660781d0e076.jpg'
  }
];
```

由于上传了两个文件，因此我们得到了两个元素的数组，它们的结构如下：

| name |         说明          |  类型  |
| ---- | :-------------------: | :----: |
| key  |    文件上传的 key     | string |
| id   | 文件存储到数据库的 id | string |
| url  |     可访问的 url      | string |

:::tip

系统会自动帮助我们上传的文件做 md5，因此你大可不必担心文件重复上传，如果你上传了
重复的文件，服务器会返回已传文件的数据。

:::

## 配置

### 本地配置

通过配置文件`src/main/java/io/github/talelin/merak/extensions/file/config.properties`，
可以改变文件上传的行为，配置项如下：

```properties
# upload
# 只能从max-file-size设置总体文件的大小
# 上传文件总大小
spring.servlet.multipart.max-file-size=20MB
# 每个文件的大小
lin.cms.file.single-limit=2MB
# 上传文件总数量
lin.cms.file.nums=10
# 禁止某些类型文件上传，文件格式以,隔开
lin.cms.file.exclude=
# 允许某些类型文件上传，文件格式以,隔开
lin.cms.file.include=.jpg,.png,.jpeg
# 文件上传后，访问域名配置
lin.cms.file.domain=http://localhost:5000/
# 文件存储位置，默认在工作目录下的assets目录
lin.cms.file.store-dir=assets/
# 具体实现
lin.cms.file.uploader=local
```

在每个配置的后面，我们均以注释的方式解释了每项的作用。

当然还需要着重解释一下`exclude`和`include`这两项配置，默认情况下，这两者只会有一
项生效；若这二者中有一项为空，则另一项不为空的配置会生效；如果两项皆为空的话，会
通过所有文件类型；如果二者都不为空，则`include`为有效配置，而`exclude`会失效；总
而言之，系统只会支持使用一项，二者都为为空的情况下，则通过所有文件类型。

### 远程配置

将文件上传到本地显然不是一个很好的选择，lin-cms 额外提供了上传到云的支持，只需更改几个配置
即可更换文件上传的存储位置。

目前，我们仅支持七牛云一种，后续会提供更多的选择。

:::tip

请确保你已经拥有七牛云的相关服务账号和配置信息。

:::

修改七牛相关配置，如下：

```properties
# 七牛云域名
lin.cms.file.domain={domain}
# 七牛
lin.cms.file.qiniuyun.access-key={******&}
lin.cms.file.qiniuyun.secret-key={&******}
lin.cms.file.qiniuyun.bucket={bucket}
# 具体实现
lin.cms.file.uploader=qiniuyun
```

这里，我们增加了三个配置，分别是 access-key，secret-key 和 bucket，这三个配置
你必须从你的七牛云账号获得，然后将其准确的填写入配置。

另外，我们还需要修改两个配置，一个是文件服务器域名（七牛云配置中的域名），另一个是
uploader 的实现，我们将其修改为了 qiniuyun。

配置修改完毕后，文件上传就可以无缝切换到云上了。

:::warning

一定要填写正确的 access-key，secret-key，bucket 以及 domain。

:::

<RightMenu />
