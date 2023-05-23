[TOC]

说明
========================

## 排版约定

| 排版格式 |  含义  |
|:----:|:----:|
| < >  |  变量  |
| [ ]  | 可选项  |
| { }  | 必选项  |
|  \|  | 互斥关系 |

## 时间与日期

日期与时间的表示有多种方式。为统一起见，除非是约定俗成或者有相应规范的，凡需要日期时间表示的地方一律采用UTC东八区时间，遵循ISO 8601，并做以下约束：

1. 表示日期一律采用`YYYY-MM-DD`方式，例如`2016-06-01`表示2016年6月1日
2. 表示时间一律采用`hh:mm:ss`方式，并在最后加上`+08:00`表示东八区时间。例如`23:00:10+08:00`表示东八区时间23点0分10秒。
3. 凡涉及日期和时间合并表示时，在两者中间加大写字母`T`，例如`2016-06-01T23:00:10+08:00`表示东八区时间2016年6月1日23点0分10秒

## 错误返回格式

统一为如下格式。后续各接口不再单独列出。

| 参数      | 类型     | 描述     |
|---------|--------|--------|
| code    | int    | 错误代码   |
| message | String | 错误描述信息 |

## 公共错误码

| 错误码 | 错误描述             | HTTP状态码 | 中文解释   |      |
|-----|------------------|---------|--------|------|
| 0   | SUCCESS          | 200     | 成功请求   |      |
| 1   | ERROR            | 500     | 服务器通用异常 |      |
| 100 | ARGUMENT_ILLEGAL | 400     | 请求参数错误 |      |
|     |                  |         |        |      |



用户相关接口
========================

## 1. 用户登录

### 接口功能

用户登录

### URL

```
/api/session
```

### 请求方法

```
POST
```

### 请求体参数

| 参数       | 类型     | 描述   | 可控 |
|----------|--------|------|----|
| username | String | 用户名  | 否  |
| password | String | 用户密码 | 否  |

### 返回字段

| 参数      | 类型     | 描述   |
|---------|--------|------|
| code    | Int    | 状态码  |
| message | String | 状态信息 |
| data    | Object | 用户信息 |

### 请求体示例

```json
{
    "username": "wang",
    "password": "password"
}
```

### 应答示例

```json
{
    "code": 0,
    "message": "SUCCESS",
    "data": {
        "id": 2,
        "username": "user1",
        "email": "user1@csu.edu.cn",
        "phone": "13800138001",
        "role": 1
    }
}
```

## 2. 用户登出

### 接口功能

用户登出账号

### URL

```
/api/session/{id}
```

### 请求方法

```
DELETE
```

### url参数

| 参数 | 类型  | 描述   | 可空 | 类别            |
|----|-----|------|----|---------------|
| id | Int | 用户id | 否  | path variable |

### 返回字段

| 参数      | 类型     | 描述   |
|---------|--------|------|
| code    | Int    | 状态码  |
| message | String | 状态信息 |

### 应答示例

```json
{
    "code": 0,
    "message": "SUCCESS"
}
```


## 3. 用户注册

### 接口功能

进行用户注册

### URL

```
/api/user
```

### 请求方法

```
POST
```

### 请求体参数

| 参数            | 类型   | 描述     | 可空 |
| --------------- | ------ | -------- | ---- |
| username        | String | 用户名称 | 否   |
| password        | String | 用户密码 | 否   |
| confirmPassword | String | 重复密码 | 否   |
| email           | String | 邮箱     | 是   |
| phone           | String | 电话     | 是   |
| question        | String | 密保问题 | 否   |
| answer          | String | 密保答案 | 否   |

### 返回字段

| 参数      | 类型     | 描述   |
|---------|--------|------|
| code    | String | 状态码  |
| message | String | 状态信息 |

### 请求体示例

```json
{
    "username": "wang",
    "password": "password",
    "confirmPassword": "password",
    "email": "wang@163.com",
    "phone": 1869544651,
    "question": "我的妻子是谁",
    "answer": "MIKU"
}
```

### 应答示例

```json
{
    "code": 0,
    "message": "SUCCESS"
}
```

## 4. 获取用户信息

### 接口功能

获取对应用户信息

### URL

```
/api/user/{id}
```

### 请求方法

```
GET
```

### url参数

| 参数 | 类型  | 描述   | 可空 | 类别            |
|----|-----|------|----|---------------|
| id | Int | 用户id | 否  | path variable |

### 返回字段

| 参数      | 类型     | 描述   |
|---------|--------|------|
| code    | Int    | 状态码  |
| message | String | 状态信息 |
| data    | Object | 用户信息 |

### 应答示例

```json
{
    "code": 0,
    "message": "SUCCESS",
    "data": {
        "id": 1,
        "username": "wang",
        "email": "wang@163.com",
        "phone": 1869544651,
        "role": 1
    }
}
```

## 5. 更新用户信息

### 接口功能

更新对应用户信息

### URL

```
/api/user/{id}
```

### 请求方法

```
PATCH
```

### url参数

| 参数 | 类型  | 描述   | 可空 | 类别            |
|----|-----|------|----|---------------|
| id | Int | 用户id | 否  | path variable |

### 请求体参数

| 参数     | 类型   | 描述     | 可空 |
| -------- | ------ | -------- | ---- |
| username | String | 用户名称 | 是   |
| email    | String | 邮箱     | 是   |
| phone    | String | 电话     | 是   |
| question | String | 密保问题 | 是   |
| answer   | String | 密保答案 | 是   |

如果 question 不为空，则 answer 必须非空。

### 返回字段

| 参数      | 类型     | 描述   |
|---------|--------|------|
| code    | Int    | 状态码  |
| message | String | 状态信息 |

### 请求体示例

```json
{
    "username": "wang",
    "email": "wang@163.com",
    "phone": 1869544651
}
```

### 应答示例

```json
{
    "code": 0,
    "message": "SUCCESS"
}
```

## 6. 注销用户

### 接口功能

注销对应用户

### URL

```
/api/user/{id}
```

### 请求方法

```
DELETE
```

### url参数

| 参数 | 类型     | 描述   | 可空 | 类别            |
|----|--------|------|----|---------------|
| id | String | 用户id | 否  | path variable |

### 请求体参数

| 参数       | 类型     | 描述     | 可空 |
|----------|--------|--------|----|
| password | String | 用户密码   | 否  |
| answer   | String | 用户密保答案 | 否  |

### 返回字段

| 参数      | 类型     | 描述   |
|---------|--------|------|
| code    | Int    | 状态码  |
| message | String | 状态信息 |

### 请求体示例

```json
{
    "password": "wang",
    "answer": "MIKU"
}
```

### 应答示例

```json
{
    "code": 0,
    "message": "SUCCESS"
}
```

## 7. 忘记密码-获取密保问题

### 接口功能

用户在忘记密码时，获得其设置的密保问题

> 使用get请求，把username置于path中，会限制用户名不能有特殊字符和空格

### URL

```
/api/getpwdquestion
```

### 请求方法

```
POST
```

### 请求体参数

| 参数     | 类型   | 描述   | 可空 |
| -------- | ------ | ------ | ---- |
| username | String | 用户名 | 否   |

### 返回字段

| 参数    | 类型   | 描述                         |
| ------- | ------ | ---------------------------- |
| code    | Int    | 状态码                       |
| message | String | 状态信息                     |
| data    | Object | 用户设置的密保问题以及用户id |

### 应答示例

success：

```json
{
    "code": 0,
    "message": "SUCCESS",
    "data": {
        "question": "我的妻子是谁",
        "id": 1
    }
}
```

error：

```json
{
    "code": 1,
    "message": "未设置密保问题，如需找回密码请联系管理员"
}
```

```json
{
    "code": 1,
    "message": "用户不存在"
}
```

## 8. 忘记密码-验证密保

### 接口功能

用户忘记密码时，根据用户名和设置的密保答案验证身份。

### URL

```
/api/user/{id}/pwdtoken
```

### 请求方法

```
GET
```

### url参数

| 参数     | 类型    | 描述     | 可空 | 类别          |
| -------- | ------- | -------- | ---- | ------------- |
| id       | Integer | 用户id   | 否   | path variable |
| question | String  | 密保问题 | 否   | request param |
| answer   | String  | 密保回答 | 否   | request param |

### 返回字段

| 参数      | 类型     | 描述       |
|---------|--------|----------|
| code    | Int    | 状态码      |
| message | String | 状态信息     |
| data    | String | 携带的token |

### 应答示例

回答正确：

```json
{
    "code": 0,
    "message": "SUCCESS",
    "data": "21ce02a7e-7f12-42e6-bdf8-a343e5f3462c"
}
```

回答错误：

```json
{
    "code": 1,
    "message": "密保验证错误"
}
```

## 9. 修改密码

### 接口功能

用户登录状态修改密码

### URL

```
/api/user/{id}/password
```

### 请求方法

```
POST
```

### url参数

| 参数 | 类型      | 描述   | 可空 | 类别            |
|----|---------|------|----|---------------|
| id | Integer | 用户id | 否  | path variable |

### 请求体参数

| 参数          | 类型     | 描述  | 可空 |
|-------------|--------|-----|----|
| oldPassword | String | 旧密码 | 否  |
| newPassword | String | 新密码 | 否  |

### 返回字段

| 参数      | 类型     | 描述   |
|---------|--------|------|
| code    | Int    | 状态码  |
| message | String | 状态信息 |

### 请求体示例

```json
{
    "oldPassword": "wang",
    "newPassword": "WANG"
}
```

### 应答示例

success：

```json
{
    "code": 0,
    "message": "SUCCESS"
}
```

error：

```json
{
    "code": 1,
    "message": "用户不存在"
}
```
```json
{
    "code": 1,
    "message": "旧密码输入错误"
}
```
```json
{
    "code": 1,
    "message": "密码长度不足"
}
```
```json
{
    "code": 1,
    "message": "密码不能包含空格"
}
```

## 10. 忘记密码-修改密码

### 接口功能

用户忘记密码时修改密码。

### URL

```
/api/user/{id}/password
```

### 请求方法

```
POST
```

### url参数

| 参数 | 类型    | 描述   | 可空 | 类别          |
| ---- | ------- | ------ | ---- | ------------- |
| id   | Integer | 用户id | 否   | path variable |

### 请求体参数

| 参数        | 类型   | 描述                  | 可空 |
| ----------- | ------ | --------------------- | ---- |
| newpassword | String | 新密码                | 否   |
| token       | String | 验证密保时返回的token | 否   |

### 返回字段

| 参数      | 类型     | 描述   |
|---------|--------|------|
| code    | Int    | 状态码  |
| message | String | 状态信息 |

### 请求体示例

```json
{
    "newpassowd": "WANG",
    "token": "21ce02a7e-7f12-42e6-bdf8-a343e5f3462c"
}
```



### 应答示例
success:

```json
{
    "code": 0,
    "message": "SUCCESS"
}
```
error:
```json
{
    "code": 1,
    "message": "用户不存在"
}
```
```json
{
    "code": 1,
    "message": "token无效或已过期"
}
```
```json
{
    "code": 1,
    "message": "密码更新失败"
}
```
## 11. 验证注册字段可用

### 接口功能

用户注册时，验证指定字段是否已重复

### URL

```
api/fields/register
```

### 请求方法

```
GET
```

### url参数

| 参数    | 类型     | 描述  | 可空 | 类别            |
|-------|--------|-----|----|---------------|
| key   | String | 字段名 | 否  | request param |
| value | String | 字段值 | 否  | request param |

key可取以下值：

| key 值    | 描述   |
|----------|------|
| username | 电话号码 |
| phone    | 电话号码 |
| email    | 电子邮件 |

### 返回字段

| 参数      | 类型     | 描述   |
|---------|--------|------|
| code    | Int    | 状态码  |
| message | String | 状态信息 |

### 应答示例

字段非重复：

```json
{
    "code":0,
    "message": "SUCCESS"
}
```

字段重复：

```json
{
    "code": 1,
    "message": "用户名已存在"
}
```

参数错误：

```json
{
    "code": 100,
    "message": "ARGUMENT ILLEGAL"
}
```

地址相关接口
==========================

## 1. 添加地址

## 2. 删除地址

## 3. 更新地址

## 4. 查看所有地址

## 5. 查看详细地址

商品相关接口
==========================

## 1. 查找商品

### 接口功能

根据不同的参数，给出对应的商品列表

### URL

```
/api/products
```

### 请求方法

```
GET
```

### url参数

| 参数    | 类型    | 描述                              | 可空 | 类型          |
| ------- | ------- | --------------------------------- | ---- | ------------- |
| cid     | Integer | 商品类别id，与keyword不能同时为空 | 是   | request param |
| keyword | String  | 商品查找关键字                    | 是   | request param |
| order   | String  | 排序关键字                        | 是   | request param |
| asc     | Boolean | 是否升序，默认为false             | 是   | request param |
| page    | Integer | 分页页数                          | 是   | request param |
| size    | Integer | 分页大小                          | 是   | request param |

id 和 keyword 至少有一个非空。

### 返回字段

| 参数    | 类型   | 描述                                  |
| ------- | ------ | ------------------------------------- |
| code    | Int    | 状态码                                |
| message | String | 状态信息                              |
| data    | Object | 分页展示的商品列表，由records字段存储 |

### 应答示例

```json
{
    "code": 0,
    "data": {
        "records": [
            {
                "id": 9,
                "categoryId": 2,
                "name": "Zheng Jialun",
                "subtitle": "Miss.",
                "price": 910,
                "status": 1,
                "imageList": []
            },
            {
                "id": 10,
                "categoryId": 2,
                "name": "Au On Na",
                "subtitle": "Mrs.",
                "price": 74,
                "status": 1,
                "imageList": []
            }
        ],
        "total": 10,
        "size": 2,
        "current": 5,
        "orders": [],
        "optimizeCountSql": true,
        "searchCount": true,
        "maxLimit": null,
        "countId": null,
        "pages": 5
    },
    "message": "SUCCESS"
}
```

imageList仅返回主图信息

data字段信息见下表

| 参数    | 类型    | 描述                              |
| ------- | ------- | --------------------------------- |
| total   | Integer | 查找到的商品数量，为0表示没有商品 |
| records | List    | 各商品信息                        |
| size    | Integer | 当前页大小                        |
| current | Integer | 当前页                            |
| pages   | Integer | 总页数                            |



## 2. 商品详细信息

### 接口功能

获得指定商品的详细信息

### URL

```
/api/products/{id}
```

### 请求方法

```
GET
```

### url参数

| 参数 | 类型      | 描述   | 可空 | 类型            |
|----|---------|------|----|---------------|
| id | Integer | 商品编号 | 否  | path variable |

### 返回字段

| 参数      | 类型     | 描述   |
|---------|--------|------|
| code    | Int    | 状态码  |
| message | String | 状态信息 |
| data    | Object | 商品信息 |

### 应答示例

```json
{
    "code": 0,
    "message": "SUCCESS",
    "data":{
        "id": 10,
        "categoryId": 241,
        "name": "Au On Na",
        "subtitle": "Mrs.",
        "detail": "Y01tSWl4e7",
        "price": 74,
        "stock": 175,
        "status": 1,
        "createTime": "2012-12-28T05:31:52+08:00",
        "updateTime": "2018-04-05T03:34:49+08:00",
        "parentCategoryId": 2,
        "imageList": [
            {
                "image": {
                    "id": "b088d2bb92587204fae8d7e8521adc67",
                    "pid": 1,
                    "type": 1,
                    "alt": "picture",
                    "createTime": "2023-05-16T15:49:56+08:00"
                },
                "token": {
        			"tmpSecretId": "",
                    "tmpSecretKey": "",
                    "sessionToken": ""
    			}
            }
        ]
    }
}
```

imageList字段信息见下表

| 参数       | 类型    | 描述                                                         |
| ---------- | ------- | ------------------------------------------------------------ |
| image      | Object  | 产品的图像                                                   |
| token      | Object  | 图像对应的临时密钥，用于向对象存储服务获取图片，字段与获取图片临时密钥接口一致。可为null，表示服务端获取token失败，为空时可使用图片密钥接口再次尝试获取。 |
| id         | String  | 图像编号                                                     |
| pid        | Integer | 对应产品编号                                                 |
| type       | Integer | 图像类型,main image:1, sub image: 2                          |
| alt        | String  | 图像文字描述                                                 |
| createTime | String  | 图像上传时间                                                 |

图片相关接口
==========================

## 1. 获取图片临时密钥

### 接口功能

获取指定图片的密钥，用于向对象存储服务获取图片。

### URL

```
/api/image/{id}
```

### 请求方法

```
GET 
```

### url参数

| 参数 | 类型   | 描述     | 可空 | 类别          |
| ---- | ------ | -------- | ---- | ------------- |
| id   | String | 图像编号 | 否   | path variable |

### 返回字段

| 参数    | 类型   | 描述               |
| ------- | ------ | ------------------ |
| code    | Int    | 状态码             |
| message | String | 状态信息           |
| data    | Object | 获取图片必要的信息 |
data内容：
| 参数         | 类型   | 描述                       |
| ------------ | ------ | -------------------------- |
| tmpSecretId  | String | 临时密钥SecretId           |
| tmpSecretKey | String | 临时密钥SecretKey          |
| sessionToken | String | 临时密钥sessionToken       |
| startTime    |        | 暂时禁用，看客户端是否需要 |
| expiredTime  |        | 暂时禁用                   |

### 应答示例

```json
{
    "code": 0,
    "message": "SUCCESS",
    "data": {
        "tmpSecretId": "AKIDLphLyrJ0E-P_wbT7LwdnvSjk_7WvX67X0kQ2PPV7wuo2MQMqKyfHkrK_Yap81Fqx",
        "tmpSecretKey": "Xb72buUNay4/4xE994m7CNSuJYqsmPhGAJ5oX4m7wwI=",
        "sessionToken": "4TLfetbjxgoBG3uWKgeei4S3FhBOZ20afc5e600e39700b44525a2bde14ecdce48p5zfItn5K2eiQIyk7m8KvVjfT3HGur-xFFgF_u3cRw4oLY_RuajilL9R9i2-gjuPeeEy_dZD42OcNseJl51ivRs8vM3MGL4b_ZjlU2qdzG1uW0md5pCqcdfVJ0ILyLCwI10d0r74W0zULIT-i0iyD27a0d-nQGFhcqwNT4wnllKV1L8yVUSLlTWWxc_9iu76g5wOvygNfLUdAiZWkDSEMwUQmNNhR2tSBOecsVTHdJk5rjBlpZbRXKHZVeiuwhZNWmO8YuOwyw7NgrbdjfTdM9rHwDZELy7OX57VB_2p0KEm02Oq7r3MKBXONulrd5x6sNOrxkeFULzodvTdAfCL29JI4mbduLbiB2yKgAfVT0cnjp5Za9prZeZ0zbkwxQgvMh2u3zxNccHwiH0GXEkg7uzLvsnBr9vyJUfjQJIo0Q"
    }
}
```

## 2. 上传商品图片

商品类别相关接口
==========================
## 1. 查看类别信息

### 接口功能

根据类别id获得商品类别列表

### URL

```
admin/categories/{id}
```

### 请求方法

```
GET
```

### url参数

| 参数 | 类型    | 描述   | 可空 | 类别          |
| ---- | ------- | ------ | ---- | ------------- |
| id   | Integer | 类别id | 否   | path variable |

### 返回字段

| 参数    | 类型   | 描述     |
| ------- | ------ | -------- |
| code    | Int    | 状态码   |
| message | String | 状态信息 |
| data    | Object | 类别信息 |

### 应答示例

success:

```json
{
    "code": 0,
    "data": {
        "id": 2,
        "parentId": 1,
        "name": "computer",
        "status": 1,
        "sortOrder": 1
    },
    "message": "SUCCESS"
}
```

error:

```json
{
    "code": 1,
    "message": "未登录或非管理员账号，授权失败"
}
```

```json
{
    "code": 12,
    "message": "EMPTY OBJECT"
}
```

## 2. 查看指定类别及子类别

### 接口功能

根据类别id获取一级子类别的信息。

### URL

```
admin/categories/{id}/children
```

### 请求方法

```
GET
```

### url参数

| 参数 | 类型    | 描述   | 可空 | 类别          |
| ---- | ------- | ------ | ---- | ------------- |
| id   | Integer | 类别id | 否   | path variable |

### 返回字段

| 参数    | 类型   | 描述                             |
| ------- | ------ | -------------------------------- |
| code    | Int    | 状态码                           |
| message | String | 状态信息                         |
| data    | List   | 子类别列表，若无子类别时列表为空 |

### 应答示例

```json
{
    "code": 0,
    "data": [
        {
            "id": 2,
            "parentId": 1,
            "name": "computer",
            "status": 1,
            "sortOrder": 1
        },
        {
            "id": 3,
            "parentId": 1,
            "name": "book",
            "status": 1,
            "sortOrder": 3
        },
        {
            "id": 4,
            "parentId": 1,
            "name": "butterfly",
            "status": 1,
            "sortOrder": 4
        }
    ],
    "message": "SUCCESS"
}
```

## 3. 获取所有子类别

### 接口功能

获取某类别下所有子类别（递归）的id。

### URL

```
admin/categories/{id}/children/id
```

### 请求方法

```
GET
```

### url参数

| 参数 | 类型    | 描述   | 可空 | 类别          |
| ---- | ------- | ------ | ---- | ------------- |
| id   | Integer | 类别id | 否   | path variable |

### 返回字段

| 参数    | 类型   | 描述                    |
| ------- | ------ | ----------------------- |
| code    | Int    | 状态码                  |
| message | String | 状态信息                |
| data    | List   | 包括自身的所有子类别 id |

### 应答示例

```json
{
    "code": 0,
    "data": [
        1,
        4,
        6,
        7,
        5,
        3,
        2
    ],
    "message": "SUCCESS"
}
```

购物车相关接口
==========================

## 1. 添加商品

### 接口功能

向购物车添加商品

### URL

```
api/cartitem
```

### 请求方法

```
POST
```

### 请求体参数

| 参数      | 类型    | 描述     | 可空 |
| --------- | ------- | -------- | ---- |
| productId | Integer | 商品id   | 否   |
| quantity  | Integer | 商品数量 | 否   |

### 返回字段

| 参数    | 类型   | 描述           |
| ------- | ------ | -------------- |
| code    | Int    | 状态码         |
| message | String | 状态信息       |
| data    | Object | 用户当前购物车 |

### 请求体示例

```json
{
    "productId": 2,
    "quantity": 50
}
```

### 应答示例

```json
{
    "code": 0,
    "message": "SUCCESS"
}
```

## 2. 删除商品

### 接口功能



### URL

```
/api/cartitem
```

### 请求方法

```
DELETE
```

### url参数

| 参数 | 类型 | 描述 | 可空 | 类别                           |
| ---- | ---- | ---- | ---- | ------------------------------ |
|      |      |      |      | path variable \| request param |

### 请求体参数

| 参数 | 类型 | 描述 | 可空 |
| ---- | ---- | ---- | ---- |
|      |      |      |      |

### 返回字段

| 参数    | 类型   | 描述     |
| ------- | ------ | -------- |
| code    | Int    | 状态码   |
| message | String | 状态信息 |

### 请求体示例

```json

```

### 应答示例

```json
{
    "code": 0,
    "message": "SUCCESS"
}
```

## 3. 获取购物车

### 接口功能



### URL

```
/api/cartitem
```

### 请求方法

```
GET
```

### url参数

| 参数 | 类型 | 描述 | 可空 | 类别                           |
| ---- | ---- | ---- | ---- | ------------------------------ |
|      |      |      |      | path variable \| request param |

### 请求体参数

| 参数 | 类型 | 描述 | 可空 |
| ---- | ---- | ---- | ---- |
|      |      |      |      |

### 返回字段

| 参数    | 类型   | 描述     |
| ------- | ------ | -------- |
| code    | Int    | 状态码   |
| message | String | 状态信息 |

### 请求体示例

```json

```

### 应答示例

```json
{
    "code": 0,
    "message": "SUCCESS"
}
```

## 4. 更新商品数量

### 接口功能



### URL

```
api/cartitem
```

### 请求方法

```
PATCH
```

### url参数

| 参数 | 类型 | 描述 | 可空 | 类别                           |
| ---- | ---- | ---- | ---- | ------------------------------ |
|      |      |      |      | path variable \| request param |

### 请求体参数

| 参数 | 类型 | 描述 | 可空 |
| ---- | ---- | ---- | ---- |
|      |      |      |      |

### 返回字段

| 参数    | 类型   | 描述     |
| ------- | ------ | -------- |
| code    | Int    | 状态码   |
| message | String | 状态信息 |

### 请求体示例

```json

```

### 应答示例

```json
{
    "code": 0,
    "message": "SUCCESS"
}
```

## 5. 购物车项选中/不选

### 接口功能



### URL

```

```

### 请求方法

```

```

### url参数

| 参数 | 类型 | 描述 | 可空 | 类别                           |
| ---- | ---- | ---- | ---- | ------------------------------ |
|      |      |      |      | path variable \| request param |

### 请求体参数

| 参数 | 类型 | 描述 | 可空 |
| ---- | ---- | ---- | ---- |
|      |      |      |      |

### 返回字段

| 参数    | 类型   | 描述     |
| ------- | ------ | -------- |
| code    | Int    | 状态码   |
| message | String | 状态信息 |

### 请求体示例

```json

```

### 应答示例

```json
{
    "code": 0,
    "message": "SUCCESS"
}
```

## 6  购物车全选/全不选

### 接口功能



### URL

```
api/cart
```

### 请求方法

```
PATCH
```

### url参数

| 参数 | 类型 | 描述 | 可空 | 类别                           |
| ---- | ---- | ---- | ---- | ------------------------------ |
|      |      |      |      | path variable \| request param |

### 请求体参数

| 参数 | 类型 | 描述 | 可空 |
| ---- | ---- | ---- | ---- |
|      |      |      |      |

### 返回字段

| 参数    | 类型   | 描述     |
| ------- | ------ | -------- |
| code    | Int    | 状态码   |
| message | String | 状态信息 |

### 请求体示例

```json

```

### 应答示例

```json
{
    "code": 0,
    "message": "SUCCESS"
}
```

## 7. 获取购物车总数

### 接口功能



### URL

```
api/cart
```

### 请求方法

```
GET
```

### url参数

| 参数 | 类型 | 描述 | 可空 | 类别                           |
| ---- | ---- | ---- | ---- | ------------------------------ |
|      |      |      |      | path variable \| request param |

### 请求体参数

| 参数 | 类型 | 描述 | 可空 |
| ---- | ---- | ---- | ---- |
|      |      |      |      |

### 返回字段

| 参数    | 类型   | 描述     |
| ------- | ------ | -------- |
| code    | Int    | 状态码   |
| message | String | 状态信息 |

### 请求体示例

```json

```

### 应答示例

```json
{
    "code": 0,
    "message": "SUCCESS"
}
```

订单相关接口
==========================

## 1. 创建普通订单

- 订单中需要保存当前商品信息
- 订单编号order_no需要一种生成机制：唯一、非自增、能索引
- 生成订单成功后需要减少库存，必须加锁

## 2. 从购物车生成订单

## 3. 取消订单

## 4. 获取订单详情

## 5. 获取订单列表

支付相关接口
==========================











模板
===========================

### 接口功能



### URL

```

```

### 请求方法

```

```

### url参数

| 参数 | 类型 | 描述 | 可空 | 类别                             |
|----|----|----|----|--------------------------------|
|    |    |    |    | path variable \| request param |

### 请求体参数

| 参数 | 类型 | 描述 | 可空 |
|----|----|----|----|
|    |    |    |    |

### 返回字段

| 参数      | 类型     | 描述   |
|---------|--------|------|
| code    | Int    | 状态码  |
| message | String | 状态信息 |

### 请求体示例

```json

```

### 应答示例

```json
{
    "code": 0,
    "message": "SUCCESS"
}
```





