# api 文档

## admin 部分

### getAllPermissions 查询所有可分配的权限

`url: /cms/admin/permission`

`method: get`

`result:`

```json
{
  "用户": [
    {
      "id": 6,
      "name": "查询自己拥有的权限",
      "module": "用户"
    },
    {
      "id": 7,
      "name": "查询自己信息",
      "module": "用户"
    }
  ],
  "图书": [
    {
      "id": 8,
      "name": "删除图书",
      "module": "图书"
    }
  ],
  "信息": [
    {
      "id": 4,
      "name": "测试日志记录",
      "module": "信息"
    },
    {
      "id": 5,
      "name": "查看lin的信息",
      "module": "信息"
    }
  ],
  "日志": [
    {
      "id": 1,
      "name": "查询所有日志",
      "module": "日志"
    },
    {
      "id": 2,
      "name": "搜索日志",
      "module": "日志"
    },
    {
      "id": 3,
      "name": "查询日志记录的用户",
      "module": "日志"
    }
  ]
}
```

### getUsers 查询所有用户

`url: /cms/admin/users`

`method: get`

`param:`

| name     | required | default |              含义               |
| -------- | :------: | ------: | :-----------------------------: |
| group_id |  false   |      无 | 分组 id，传入后获得该分组的用户 |
| count    |  false   |      10 |             分页数              |
| page     |  false   |       0 |             分页值              |

`result:`

```json
{
  "total": 2,
  "items": [
    {
      "id": 1,
      "username": "root",
      "nickname": "root",
      "avatar": null,
      "email": null
    },
    {
      "id": 2,
      "username": "pedro",
      "nickname": null,
      "avatar": null,
      "email": null
    }
  ],
  "page": 0,
  "count": 10
}
```

### changeUserPassword 修改用户密码

`url: /cms/admin/user/{id}/password`

`method: put`

`body:`

| name             | required | default |   含义   |
| ---------------- | :------: | ------: | :------: |
| new_password     |   true   |      无 |  新密码  |
| confirm_password |   true   |      无 | 确认密码 |

```json
{
  "new_password": "147258",
  "confirm_password": "147258"
}
```

`result:`

```json
{
  "error_code": 2,
  "msg": "密码修改成功",
  "url": "/cms/admin/user/2/password"
}
```

### deleteUser 删除用户

`url: /cms/admin/user/{id}`

`method: delete`

`result:`

```json
{
  "error_code": 3,
  "msg": "删除用户成功",
  "url": "/cms/admin/user/2"
}
```

### updateUser 管理员更新用户信息

`url: /cms/admin/user/{id}`

`method: put`

`body:`

| name      | required | default |           含义            |
| --------- | :------: | ------: | :-----------------------: |
| group_ids |  false   |      无 | 分字 id，数组，如 [1,2,3] |

```json
{
  "group_ids": [2]
}
```

`result:`

```json
{
  "error_code": 4,
  "msg": "更新用户成功",
  "url": "/cms/admin/user/2"
}
```

### getGroups 查询所有权限组及其权限

`url: /cms/admin/group`

`method: get`

`param:`

| name  | required | default |  含义  |
| ----- | :------: | ------: | :----: |
| count |  false   |      10 | 分页数 |
| page  |  false   |       0 | 分页值 |

`result:`

```json
{
  "total": 2,
  "items": [
    {
      "id": 1,
      "name": "root",
      "info": "超级用户组"
    },
    {
      "id": 2,
      "name": "guest",
      "info": "游客组"
    }
  ],
  "page": 0,
  "count": 10
}
```

### getAllGroup 查询所有权限组

`url: /cms/admin/group/all`

`method: get`

`result:`

```json
[
  {
    "id": 1,
    "name": "root",
    "info": "超级用户组"
  },
  {
    "id": 2,
    "name": "guest",
    "info": "游客组"
  }
]
```

### getGroup 查询一个权限组及其权限

`url: /cms/admin/group/{id}`

`method: get`

`result:`

```json
{
  "id": 1,
  "name": "root",
  "info": "超级用户组",
  "permissions": []
}
```

### createGroup 新建权限组

`url: /cms/admin/group`

`method: post`

`body:`

| name           | required | default |         含义         |
| -------------- | :------: | ------: | :------------------: |
| name           |   true   |      无 |       分组名称       |
| info           |   true   |      无 |       分组信息       |
| permission_ids |  false   |      无 | 权限 id，例如[1,2,3] |

```json
{
  "name": "武庚纪",
  "info": "玄机科技",
  "permission_ids": [1, 2]
}
```

`result:`

```json
{
  "error_code": 13,
  "msg": "新建分组成功",
  "url": "/cms/admin/group"
}
```

### updateGroup 更新一个权限组

`url: /cms/admin/group/{id}`

`method: put`

`body:`

| name | required | default |   含义   |
| ---- | :------: | ------: | :------: |
| name |  false   |      无 | 分组名称 |
| info |  false   |      无 | 分组信息 |

```json
{
  "name": "天行九歌",
  "info": "玄机科技"
}
```

`result:`

```json
{
  "error_code": 5,
  "msg": "更新分组成功",
  "url": "/cms/admin/group/5"
}
```

### deleteGroup 删除一个权限组

`url: /cms/admin/group/{id}`

`method: delete`

`result:`

```json
{
  "error_code": 6,
  "msg": "删除分组成功",
  "url": "/cms/admin/group/5"
}
```

### dispatchPermission 分配单个权限

`url: /cms/admin/permission/dispatch`

`method: post`

`body:`

| name          | required | default |  含义   |
| ------------- | :------: | ------: | :-----: |
| group_id      |   true   |      无 | 分组 id |
| permission_id |   true   |      无 | 权限 id |

```json
{
  "group_id": 5,
  "permission_id": 3
}
```

`result:`

```json
{
  "error_code": 7,
  "msg": "添加权限成功",
  "url": "/cms/admin/permission/dispatch"
}
```

### dispatchPermissions 分配多个权限

`url: /cms/admin/permission/dispatch/batch`

`method: post`

`body:`

| name           | required | default |  含义   |
| -------------- | :------: | ------: | :-----: |
| group_id       |   true   |      无 | 分组 id |
| permission_ids |   true   |      无 | 权限 id |

```json
{
  "group_id": 5,
  "permission_ids": [4, 5]
}
```

`result:`

```json
{
  "error_code": 7,
  "msg": "添加权限成功",
  "url": "/cms/admin/permission/dispatch/batch"
}
```

### removePermissions 删除多个权限

`url: /cms/admin/permission/remove`

`method: post`

`body:`

| name           | required | default |  含义   |
| -------------- | :------: | ------: | :-----: |
| group_id       |   true   |      无 | 分组 id |
| permission_ids |   true   |      无 | 权限 id |

```json
{
  "group_id": 5,
  "permission_ids": [4, 5]
}
```

`result:`

```json
{
  "error_code": 8,
  "msg": "删除权限成功",
  "url": "/cms/admin/permission/remove"
}
```

### upload 上传文件

`url: /cms/file`

`method: post`

`body:` form-data

`result:`

```json
[
  {
    "id": 1,
    "path": "http://localhost:5000/assets/75dd15d3-bf35-4bda-a927-e3c2506968b6.png",
    "type": "LOCAL",
    "name": "75dd15d3-bf35-4bda-a927-e3c2506968b6.png",
    "extension": ".png",
    "size": 505894
  }
]
```

### getLogs 查询所有日志

`url: /cms/log`

`method: get`

`param:`

| name  | required | default |                含义                 |
| ----- | :------: | ------: | :---------------------------------: |
| start |  false   |      无 | 开始日期，格式：yyyy-MM-dd HH:mm:ss |
| end   |  false   |      无 | 结束日期，格式：yyyy-MM-dd HH:mm:ss |
| name  |  false   |      无 |               用户名                |
| count |  false   |      10 |               分页数                |
| page  |  false   |       0 |               分页值                |

`result:`

```json
{
  "total": 1,
  "items": [
    {
      "id": 1,
      "message": "root又皮了一波",
      "user_id": 1,
      "username": "root",
      "status_code": 200,
      "method": "GET",
      "path": "/cms/test/json",
      "permission": "测试日志记录"
    }
  ],
  "page": 0,
  "count": 10
}
```

### searchLogs 搜索日志

`url: /cms/log/search`

`method: get`

`param:`

| name    | required | default |                含义                 |
| ------- | :------: | ------: | :---------------------------------: |
| start   |  false   |      无 | 开始日期，格式：yyyy-MM-dd HH:mm:ss |
| end     |  false   |      无 | 结束日期，格式：yyyy-MM-dd HH:mm:ss |
| name    |  false   |      无 |               用户名                |
| keyword |  false   |      "" |               关键字                |
| count   |  false   |      10 |               分页数                |
| page    |  false   |       0 |               分页值                |

`result:`

```json
{
  "total": 1,
  "items": [
    {
      "id": 1,
      "message": "root又皮了一波",
      "user_id": 1,
      "username": "root",
      "status_code": 200,
      "method": "GET",
      "path": "/cms/test/json",
      "permission": "测试日志记录"
    }
  ],
  "page": 0,
  "count": 10
}
```

### getUsers 查询日志记录的用户

`url: /cms/log/users`

`method: get`

`param:`

| name  | required | default |  含义  |
| ----- | :------: | ------: | :----: |
| count |  false   |      10 | 分页数 |
| page  |  false   |       0 | 分页值 |

`result:`

```json
{
  "total": 1,
  "items": ["root"],
  "page": 0,
  "count": 10
}
```

### register 用户注册

`url: cms/user/register`

`method: post`

`body:`

| name             | required | default |   含义   |
| ---------------- | :------: | ------: | :------: |
| username         |   true   |      无 |  用户名  |
| email            |  false   |      无 |   邮箱   |
| group_ids        |  false   |      无 | 分组 id  |
| password         |   true   |      无 |   密码   |
| confirm_password |   true   |      无 | 确认密码 |

```json
{
  "username": "范闲",
  "group_ids": [2],
  "password": "123456",
  "confirm_password": "123456"
}
```

`result:`

```json
{
  "error_code": 9,
  "msg": "注册成功",
  "url": "/cms/user/register"
}
```

### register 用户登陆

`url: cms/user/login`

`method: post`

`body:`

| name     | required | default |  含义  |
| -------- | :------: | ------: | :----: |
| username |   true   |      无 | 用户名 |
| password |   true   |      无 |  密码  |

```json
{
  "username": "root",
  "password": "123456"
}
```

`result:`

```json
{
  "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZGVudGl0eSI6MSwic2NvcGUiOiJsaW4iLCJ0eXBlIjoiYWNjZXNzIiwiZXhwIjoxNTc2ODk1NjQ0fQ.0qsD_FSxcDUa8z0xgml0hKbrlWn5XRGoREb4I4nKY5I",
  "refresh_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZGVudGl0eSI6MSwic2NvcGUiOiJsaW4iLCJ0eXBlIjoicmVmcmVzaCIsImV4cCI6MTU3OTQ4NDA0NH0.OG5n5VZ-mL1YccWI2ij0q-ENJRgOSpZzyFWgRf1FCrY"
}
```

### update 更新用户信息

`url: cms/user`

`method: put`

`body:`

| name     | required | default |   含义   |
| -------- | :------: | ------: | :------: |
| username |  false   |      无 |  用户名  |
| email    |  false   |      无 |   邮箱   |
| nickname |  false   |      无 | 用户昵称 |
| avatar   |  false   |      无 |   头像   |

```json
{
  "nickname": "范闲",
  "avatar": "https://pic3.zhimg.com/v2-fad5304306ca78c380d58fb628ed1386_400x224.jpeg",
  "email": "1312342604@qq.com"
}
```

`result:`

```json
{
  "error_code": 4,
  "msg": "更新用户成功",
  "url": "/cms/user"
}
```

### updatePassword 修改密码

`url: cms/user/change_password`

`method: put`

`body:`

| name             | required | default |   含义   |
| ---------------- | :------: | ------: | :------: |
| new_password     |   true   |      无 |  新密码  |
| confirm_password |   true   |      无 | 确认密码 |
| old_password     |   true   |      无 |  旧密码  |

```json
{
  "new_password": "147258",
  "confirm_password": "147258",
  "old_password": "123456"
}
```

`result:`

```json
{
  "error_code": 2,
  "msg": "密码修改成功",
  "url": "/cms/user/change_password"
}
```

### refreshToken 刷新令牌

`url: cms/user/refresh`

`method: get`

`result:`

**注意：一定要使用 refresh_token 来刷新令牌**

```json
{
  "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZGVudGl0eSI6MSwic2NvcGUiOiJsaW4iLCJ0eXBlIjoiYWNjZXNzIiwiZXhwIjoxNTc2OTAwMjEwfQ.8Z-qRo2VvfLhMkJTd5bOxSHqC-kYhKhi11zrVg0hSVQ",
  "refresh_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZGVudGl0eSI6MSwic2NvcGUiOiJsaW4iLCJ0eXBlIjoicmVmcmVzaCIsImV4cCI6MTU3OTQ4ODYxMH0.wu_dHpzd2mxlxqJF8L_6qBmr-FujGrDLAc2iJmZNT6M"
}
```

### getPermissions 查询自己拥有的权限

`url: cms/user/permissions`

`method: get`

`result:`

```json
{
  "id": 1,
  "nickname": "范闲",
  "avatar": "https://pic3.zhimg.com/v2-fad5304306ca78c380d58fb628ed1386_400x224.jpeg",
  "admin": true,
  "email": "1312342604@qq.com",
  "permissions": []
}
```

### getInformation 查询自己信息

`url: cms/user/information`

`method: get`

`result:`

```json
{
  "id": 1,
  "username": "root",
  "nickname": "范闲",
  "avatar": "https://pic3.zhimg.com/v2-fad5304306ca78c380d58fb628ed1386_400x224.jpeg",
  "email": "1312342604@qq.com",
  "groups": [
    {id: 1, name: 'root', info: '超级用户组'}
  ]
}
```
