# AI Travel Assistant


**简介**:AI Travel Assistant


**HOST**:http://localhost:8080/api


**联系人**:Bread


**Version**:v2.0


**接口路径**:/api/v3/api-docs


[TOC]






# POI兴趣点管理


## 获取POI详细信息


**接口地址**:`/api/pois/{poiId}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>根据POI ID获取POI的详细信息</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;poiId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;externalApiId&quot;: &quot;B0FFH4XYZ&quot;,
    &quot;name&quot;: &quot;故宫博物院&quot;,
    &quot;type&quot;: &quot;museum&quot;,
    &quot;city&quot;: &quot;北京&quot;,
    &quot;address&quot;: &quot;北京市东城区景山前街4号&quot;,
    &quot;latitude&quot;: 39.9163,
    &quot;longitude&quot;: 116.3972,
    &quot;description&quot;: &quot;明清两朝的皇宫...&quot;,
    &quot;openingHours&quot;: &quot;08:30-17:00&quot;,
    &quot;avgVisitDuration&quot;: 180,
    &quot;avgCost&quot;: &quot;60元&quot;,
    &quot;photos&quot;: [&quot;url1&quot;, &quot;url2&quot;],
    &quot;phone&quot;: &quot;010-85007421&quot;,
    &quot;rating&quot;: &quot;4.8&quot;,
    &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|poiId|POI ID|path|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 语义搜索POI


**接口地址**:`/api/pois/search/semantic`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>使用语义搜索的POI</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;poiId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;externalApiId&quot;: &quot;B0FFH4XYZ&quot;,
      &quot;name&quot;: &quot;故宫博物院&quot;,
      &quot;type&quot;: &quot;museum&quot;,
      &quot;city&quot;: &quot;北京&quot;,
      &quot;address&quot;: &quot;北京市东城区景山前街4号&quot;,
      &quot;latitude&quot;: 39.9163,
      &quot;longitude&quot;: 116.3972,
      &quot;description&quot;: &quot;明清两朝的皇宫...&quot;,
      &quot;openingHours&quot;: &quot;08:30-17:00&quot;,
      &quot;avgVisitDuration&quot;: 180,
      &quot;avgCost&quot;: &quot;60元&quot;,
      &quot;photos&quot;: [&quot;url1&quot;, &quot;url2&quot;],
      &quot;phone&quot;: &quot;010-85007421&quot;,
      &quot;rating&quot;: &quot;4.8&quot;,
      &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|queryText|查询文本|query|true|string||
|city|城市|query|false|string||
|topK|返回数量|query|false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 数据库搜索POI


**接口地址**:`/api/pois/search/exact/db`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>数据库精确搜索的POI</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;poiId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;externalApiId&quot;: &quot;B0FFH4XYZ&quot;,
      &quot;name&quot;: &quot;故宫博物院&quot;,
      &quot;type&quot;: &quot;museum&quot;,
      &quot;city&quot;: &quot;北京&quot;,
      &quot;address&quot;: &quot;北京市东城区景山前街4号&quot;,
      &quot;latitude&quot;: 39.9163,
      &quot;longitude&quot;: 116.3972,
      &quot;description&quot;: &quot;明清两朝的皇宫...&quot;,
      &quot;openingHours&quot;: &quot;08:30-17:00&quot;,
      &quot;avgVisitDuration&quot;: 180,
      &quot;avgCost&quot;: &quot;60元&quot;,
      &quot;photos&quot;: [&quot;url1&quot;, &quot;url2&quot;],
      &quot;phone&quot;: &quot;010-85007421&quot;,
      &quot;rating&quot;: &quot;4.8&quot;,
      &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|keywords|关键词|query|true|string||
|city|城市|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 第三方API搜索POI并保存


**接口地址**:`/api/pois/search/exact/api`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>第三方API（高德/谷歌）搜索的POI</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;poiId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;externalApiId&quot;: &quot;B0FFH4XYZ&quot;,
      &quot;name&quot;: &quot;故宫博物院&quot;,
      &quot;type&quot;: &quot;museum&quot;,
      &quot;city&quot;: &quot;北京&quot;,
      &quot;address&quot;: &quot;北京市东城区景山前街4号&quot;,
      &quot;latitude&quot;: 39.9163,
      &quot;longitude&quot;: 116.3972,
      &quot;description&quot;: &quot;明清两朝的皇宫...&quot;,
      &quot;openingHours&quot;: &quot;08:30-17:00&quot;,
      &quot;avgVisitDuration&quot;: 180,
      &quot;avgCost&quot;: &quot;60元&quot;,
      &quot;photos&quot;: [&quot;url1&quot;, &quot;url2&quot;],
      &quot;phone&quot;: &quot;010-85007421&quot;,
      &quot;rating&quot;: &quot;4.8&quot;,
      &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|keywords|关键词|query|true|string||
|city|城市|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


# 用户管理


## 更新用户偏好设置


**接口地址**:`/api/users/preferences`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>更新用户偏好设置</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;username&quot;: &quot;testuser&quot;,
    &quot;preferencesText&quot;: &quot;喜欢自然风光和户外活动&quot;,
    &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|preferencesText|偏好文本|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 创建用户


**接口地址**:`/api/users`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>创建用户</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;username&quot;: &quot;testuser&quot;,
    &quot;preferencesText&quot;: &quot;喜欢历史文化和美食&quot;,
    &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|username|用户名|query|true|string||
|preferencesText|偏好文本|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 根据用户ID查询用户


**接口地址**:`/api/users/{userId}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>根据用户ID查询用户</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;username&quot;: &quot;testuser&quot;,
    &quot;preferencesText&quot;: &quot;喜欢历史文化和美食&quot;,
    &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|path|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 根据用户名查询用户


**接口地址**:`/api/users/username`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>根据用户名查询用户</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;username&quot;: &quot;testuser&quot;,
    &quot;preferencesText&quot;: &quot;喜欢历史文化和美食&quot;,
    &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|username|用户名|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 查找兴趣相似用户


**接口地址**:`/api/users/similar`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>查找兴趣相似用户</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;username&quot;: &quot;testuser&quot;,
      &quot;preferencesText&quot;: &quot;喜欢历史文化和美食&quot;,
      &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|limit|限制数量|query|false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


# 旅程


## 更新旅程可见性


**接口地址**:`/api/trips/visibility`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>更新旅程可见性</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;更新成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|isPrivate|是否私有|query|true|boolean||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 更新旅程信息


**接口地址**:`/api/trips/update`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>更新旅程信息</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;title&quot;: &quot;北京三日游（更新）&quot;,
    &quot;destinationCity&quot;: &quot;北京&quot;,
    &quot;startDate&quot;: &quot;2024-12-01&quot;,
    &quot;endDate&quot;: &quot;2024-12-03&quot;,
    &quot;totalBudget&quot;: 3500.00,
    &quot;status&quot;: &quot;PLANNED&quot;,
    &quot;description&quot;: &quot;更新后的北京三日游计划&quot;,
    &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
    &quot;isPrivate&quot;: false
  }
}
</code></pre>



**请求示例**:


```javascript
{
  "title": "",
  "destinationCity": "",
  "startDate": "",
  "endDate": "",
  "totalBudget": 0,
  "description": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|tripDto|旅程dto|body|true|TripDto|TripDto|
|&emsp;&emsp;title|标题||false|string||
|&emsp;&emsp;destinationCity|目的地城市||false|string||
|&emsp;&emsp;startDate|开始日期||false|string(date)||
|&emsp;&emsp;endDate|结束日期||false|string(date)||
|&emsp;&emsp;totalBudget|预算||false|number||
|&emsp;&emsp;description|描述||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 更新旅程状态


**接口地址**:`/api/trips/status`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>更新旅程状态</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;更新成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|newStatus|新状态,可用值:PLANNING,IN_PROGRESS,COMPLETED,CANCELLED|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 创建旅程


**接口地址**:`/api/trips/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>创建旅程</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;title&quot;: &quot;北京三日游&quot;,
    &quot;destinationCity&quot;: &quot;北京&quot;,
    &quot;startDate&quot;: &quot;2024-12-01&quot;,
    &quot;endDate&quot;: &quot;2024-12-03&quot;,
    &quot;totalBudget&quot;: 3000.00,
    &quot;status&quot;: &quot;PLANNED&quot;,
    &quot;description&quot;: &quot;北京三日游计划&quot;,
    &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
    &quot;isPrivate&quot;: false
  }
}
</code></pre>



**请求示例**:


```javascript
{
  "title": "",
  "destinationCity": "",
  "startDate": "",
  "endDate": "",
  "totalBudget": 0,
  "description": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripDto|旅程dto|body|true|TripDto|TripDto|
|&emsp;&emsp;title|标题||false|string||
|&emsp;&emsp;destinationCity|目的地城市||false|string||
|&emsp;&emsp;startDate|开始日期||false|string(date)||
|&emsp;&emsp;endDate|结束日期||false|string(date)||
|&emsp;&emsp;totalBudget|预算||false|number||
|&emsp;&emsp;description|描述||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## AI智能生成完整旅程


**接口地址**:`/api/trips/ai-generate`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>AI智能生成完整旅程</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;trip&quot;: { ... },
    &quot;tripDays&quot;: [
      {
        &quot;tripDay&quot;: { ... },
        &quot;tripDayItems&quot;: [ ... ]
      }
    ]
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取用户所有旅程


**接口地址**:`/api/trips/user`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取用户所有旅程</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;ownerId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;title&quot;: &quot;北京三日游&quot;,
      &quot;destinationCity&quot;: &quot;北京&quot;,
      &quot;startDate&quot;: &quot;2024-12-01&quot;,
      &quot;endDate&quot;: &quot;2024-12-03&quot;,
      &quot;totalBudget&quot;: 3000.00,
      &quot;status&quot;: &quot;IN_PROGRESS&quot;,
      &quot;description&quot;: &quot;北京三日游计划&quot;,
      &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
      &quot;isPrivate&quot;: false,
      &quot;memberRole&quot;: &quot;OWNER&quot;,
      &quot;isPass&quot;: true,
      &quot;joinedAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 分页获取公开旅程


**接口地址**:`/api/trips/public`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>分页获取公开旅程</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;records&quot;: [
      {
        &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
        &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
        &quot;title&quot;: &quot;北京三日游&quot;,
        &quot;destinationCity&quot;: &quot;北京&quot;,
        &quot;startDate&quot;: &quot;2024-12-01&quot;,
        &quot;endDate&quot;: &quot;2024-12-03&quot;,
        &quot;totalBudget&quot;: 3000.00,
        &quot;status&quot;: &quot;PLANNED&quot;,
        &quot;description&quot;: &quot;北京三日游计划&quot;,
        &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
        &quot;isPrivate&quot;: false
      }
    ],
    &quot;total&quot;: 100,
    &quot;size&quot;: 10,
    &quot;current&quot;: 1,
    &quot;pages&quot;: 10
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|destinationCity|目的地城市|query|false|string||
|startDate|开始日期|query|false|string(date)||
|endDate|结束日期|query|false|string(date)||
|pageNum|页码|query|false|integer(int32)||
|pageSize|每页大小|query|false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取完整旅程信息


**接口地址**:`/api/trips/detail`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取完整旅程信息</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;trip&quot;: { ... },
    &quot;tripDays&quot;: [
      {
        &quot;tripDay&quot;: { ... },
        &quot;tripDayItems&quot;: [ ... ]
      }
    ]
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 删除旅程，级联删除所有日程和item


**接口地址**:`/api/trips/delete`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>删除旅程，级联删除所有日程和item</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;删除成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


# 旅程成员管理


## 处理成员请求


**接口地址**:`/api/tripMembers/handleRequest`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>接受或拒绝成员的加入请求</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;处理成员请求成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tripId|旅程ID|query|true|string(uuid)||
|handleUserId|处理的用户ID|query|true|string(uuid)||
|currentUserId|当前用户ID|query|true|string(uuid)||
|accept|是否接受|query|true|boolean||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 邀请用户


**接口地址**:`/api/tripMembers/invite`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>邀请用户加入旅程</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;邀请成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tripId|旅程ID|query|true|string(uuid)||
|userId|用户ID|query|true|string(uuid)||
|inviteUserIds|被邀请用户ID列表|query|true|array|string|


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 申请加入旅程


**接口地址**:`/api/tripMembers/addRequest`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>向旅程添加成员请求</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;申请成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tripId|旅程ID|query|true|string(uuid)||
|userId|用户ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取旅程成员列表


**接口地址**:`/api/tripMembers/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取指定旅程的所有成员列表，支持按审批状态筛选</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;id&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
      &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174002&quot;,
      &quot;role&quot;: &quot;VIEWER&quot;,
      &quot;isPass&quot;: true,
      &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
      &quot;userName&quot;: &quot;testuser&quot;,
      &quot;preferencesText&quot;: &quot;喜欢历史文化和美食&quot;
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|isPass|是否通过审批|query|false|boolean||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 删除成员


**接口地址**:`/api/tripMembers/delete`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>从旅程中删除指定成员</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;删除成员成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tripId|旅程ID|query|true|string(uuid)||
|handleUserId|处理的用户ID|query|true|string(uuid)||
|currentUserId|当前用户ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


# 旅程日程


## 更新日程备注


**接口地址**:`/api/tripDays/note`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>更新日程备注</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;更新成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|tripDayId|日程ID|query|true|string(uuid)||
|note|备注|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 交换两个日程的顺序


**接口地址**:`/api/tripDays/exchange`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>交换两个日程的顺序</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;交换成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|aTripDayId|第一个日程ID|query|true|string(uuid)||
|bTripDayId|第二个日程ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 创建旅程日程


**接口地址**:`/api/tripDays/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>创建旅程日程</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;tripDayId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
    &quot;dayDate&quot;: &quot;2024-12-01&quot;,
    &quot;notes&quot;: &quot;第一天的行程安排&quot;
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|date|日期|query|true|string(date)||
|note|备注|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## AI智能重新规划某天旅程


**接口地址**:`/api/tripDays/aiReplan`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>AI智能重新规划某天旅程</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;tripDay&quot;: {
      &quot;tripDayId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
      &quot;dayDate&quot;: &quot;2024-12-01&quot;,
      &quot;notes&quot;: &quot;第一天的行程安排&quot;
    },
    &quot;tripDayItems&quot;: [
      {
        &quot;item&quot;: { ... },
        &quot;entity&quot;: { ... }
      }
    ]
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|tripDayId|日程ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取旅程所有日程


**接口地址**:`/api/tripDays/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取旅程所有日程</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;tripDay&quot;: {
        &quot;tripDayId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
        &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
        &quot;dayDate&quot;: &quot;2024-12-01&quot;,
        &quot;notes&quot;: &quot;第一天的行程安排&quot;
      },
      &quot;tripDayItems&quot;: [
        {
          &quot;item&quot;: { ... },
          &quot;entity&quot;: { ... }
        }
      ]
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取某天日程详情


**接口地址**:`/api/tripDays/detail`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取某天日程详情</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;tripDay&quot;: {
      &quot;tripDayId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
      &quot;dayDate&quot;: &quot;2024-12-01&quot;,
      &quot;notes&quot;: &quot;第一天的行程安排&quot;
    },
    &quot;tripDayItems&quot;: [
      {
        &quot;item&quot;: {
          &quot;itemId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
          &quot;tripDayId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
          &quot;entityId&quot;: &quot;123e4567-e89b-12d3-a456-426614174002&quot;,
          &quot;startTime&quot;: &quot;09:00:00&quot;,
          &quot;endTime&quot;: &quot;10:30:00&quot;,
          &quot;itemOrder&quot;: 1.0,
          &quot;transportNotes&quot;: &quot;步行10分钟&quot;,
          &quot;estimatedCost&quot;: 50.00,
          &quot;isPoi&quot;: true,
          &quot;notes&quot;: &quot;需要提前预约&quot;
        },
        &quot;entity&quot;: {
          &quot;poiId&quot;: &quot;123e4567-e89b-12d3-a456-426614174002&quot;,
          &quot;name&quot;: &quot;故宫博物院&quot;,
          &quot;type&quot;: &quot;museum&quot;,
          &quot;city&quot;: &quot;北京&quot;,
          &quot;address&quot;: &quot;北京市东城区景山前街4号&quot;,
          &quot;latitude&quot;: 39.9163,
          &quot;longitude&quot;: 116.3972,
          &quot;description&quot;: &quot;明清两朝的皇宫...&quot;,
          &quot;openingHours&quot;: &quot;08:30-17:00&quot;,
          &quot;avgVisitDuration&quot;: 180,
          &quot;avgCost&quot;: &quot;60元&quot;,
          &quot;photos&quot;: [&quot;url1&quot;],
          &quot;phone&quot;: &quot;010-85007421&quot;,
          &quot;rating&quot;: &quot;4.8&quot;,
          &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
        }
      }
    ]
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|tripDayId|日程ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 删除日程，级联删除所有item


**接口地址**:`/api/tripDays/delete`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>删除日程，级联删除所有item</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;删除成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|tripDayIds|日程ID列表|query|true|array|string|


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


# 日程item


## 更新日程item信息


**接口地址**:`/api/tripDayItems/update`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>更新日程item信息</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;itemId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;tripDayId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
    &quot;entityId&quot;: &quot;123e4567-e89b-12d3-a456-426614174002&quot;,
    &quot;startTime&quot;: &quot;09:00:00&quot;,
    &quot;endTime&quot;: &quot;10:30:00&quot;,
    &quot;itemOrder&quot;: 1.0,
    &quot;transportNotes&quot;: &quot;步行10分钟&quot;,
    &quot;estimatedCost&quot;: 50.00,
    &quot;isPoi&quot;: true,
    &quot;notes&quot;: &quot;需要提前预约&quot;
  }
}
</code></pre>



**请求示例**:


```javascript
{
  "itemId": "",
  "startTime": "",
  "endTime": "",
  "transportNotes": "",
  "estimatedCost": 0,
  "notes": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|tripDayItemDto|日程项dto|body|true|TripDayItemDto|TripDayItemDto|
|&emsp;&emsp;itemId|日程项id||false|string(uuid)||
|&emsp;&emsp;startTime|开始时间||false|string||
|&emsp;&emsp;endTime|结束时间||false|string||
|&emsp;&emsp;transportNotes|交通建议||false|string||
|&emsp;&emsp;estimatedCost|预计花费||false|number||
|&emsp;&emsp;notes|备注||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## AI更新日程item的交通建议


**接口地址**:`/api/tripDayItems/transport`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>AI更新日程item的交通建议</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;itemId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;tripDayId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
    &quot;entityId&quot;: &quot;123e4567-e89b-12d3-a456-426614174002&quot;,
    &quot;startTime&quot;: &quot;09:00:00&quot;,
    &quot;endTime&quot;: &quot;10:30:00&quot;,
    &quot;itemOrder&quot;: 1.0,
    &quot;transportNotes&quot;: &quot;步行10分钟&quot;,
    &quot;estimatedCost&quot;: 50.00,
    &quot;isPoi&quot;: true,
    &quot;notes&quot;: &quot;需要提前预约&quot;
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|itemId|日程item ID|query|true|string(uuid)||
|originAddress|起始地址|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 移动日程item的位置，改变顺序


**接口地址**:`/api/tripDayItems/move`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>移动日程item的位置，改变顺序</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;移动成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|currentId|当前item ID|query|true|string(uuid)||
|tripDayId|日程ID|query|true|string(uuid)||
|prevId|前一个item ID|query|false|string(uuid)||
|nextId|后一个item ID|query|false|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 添加日程item


**接口地址**:`/api/tripDayItems/add`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>添加日程item</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;itemId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;tripDayId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
    &quot;entityId&quot;: &quot;123e4567-e89b-12d3-a456-426614174002&quot;,
    &quot;startTime&quot;: &quot;09:00:00&quot;,
    &quot;endTime&quot;: &quot;10:30:00&quot;,
    &quot;itemOrder&quot;: 1.0,
    &quot;transportNotes&quot;: &quot;步行10分钟&quot;,
    &quot;estimatedCost&quot;: 50.00,
    &quot;isPoi&quot;: true,
    &quot;notes&quot;: &quot;需要提前预约&quot;
  }
}
</code></pre>



**请求示例**:


```javascript
{
  "itemId": "",
  "startTime": "",
  "endTime": "",
  "transportNotes": "",
  "estimatedCost": 0,
  "notes": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|tripDayId|日程ID|query|true|string(uuid)||
|entityId|实体ID|query|true|string(uuid)||
|isPoi|是否为POI|query|true|boolean||
|tripDayItemDto|日程项dto|body|true|TripDayItemDto|TripDayItemDto|
|&emsp;&emsp;itemId|日程项id||false|string(uuid)||
|&emsp;&emsp;startTime|开始时间||false|string||
|&emsp;&emsp;endTime|结束时间||false|string||
|&emsp;&emsp;transportNotes|交通建议||false|string||
|&emsp;&emsp;estimatedCost|预计花费||false|number||
|&emsp;&emsp;notes|备注||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取某个日程的详细item列表


**接口地址**:`/api/tripDayItems/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取某个日程的详细item列表，包括entity信息</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;item&quot;: {
        &quot;itemId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
        &quot;tripDayId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
        &quot;entityId&quot;: &quot;123e4567-e89b-12d3-a456-426614174002&quot;,
        &quot;startTime&quot;: &quot;09:00:00&quot;,
        &quot;endTime&quot;: &quot;10:30:00&quot;,
        &quot;itemOrder&quot;: 1.0,
        &quot;transportNotes&quot;: &quot;步行10分钟&quot;,
        &quot;estimatedCost&quot;: 50.00,
        &quot;isPoi&quot;: true,
        &quot;notes&quot;: &quot;需要提前预约&quot;
      },
      &quot;entity&quot;: {
        &quot;poiId&quot;: &quot;123e4567-e89b-12d3-a456-426614174002&quot;,
        &quot;name&quot;: &quot;故宫博物院&quot;,
        &quot;type&quot;: &quot;museum&quot;,
        &quot;city&quot;: &quot;北京&quot;,
        &quot;address&quot;: &quot;北京市东城区景山前街4号&quot;,
        &quot;latitude&quot;: 39.9163,
        &quot;longitude&quot;: 116.3972,
        &quot;description&quot;: &quot;明清两朝的皇宫...&quot;,
        &quot;openingHours&quot;: &quot;08:30-17:00&quot;,
        &quot;avgVisitDuration&quot;: 180,
        &quot;avgCost&quot;: &quot;60元&quot;,
        &quot;photos&quot;: [&quot;url1&quot;],
        &quot;phone&quot;: &quot;010-85007421&quot;,
        &quot;rating&quot;: &quot;4.8&quot;,
        &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
      }
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|tripDayId|日程ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 删除日程item


**接口地址**:`/api/tripDayItems/delete`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>删除日程item</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;删除成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|itemIds|日程item ID列表|query|true|array|string|


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


# 旅程日志


## 修改日志可见性


**接口地址**:`/api/trip-logs/visibility`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>修改日志可见性</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;修改成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|logId|日志ID|query|true|string(uuid)||
|isPublic|是否公开|query|true|boolean||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 创建文本日志


**接口地址**:`/api/trip-logs/note`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>创建文本日志</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;logId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
    &quot;logType&quot;: &quot;NOTE&quot;,
    &quot;content&quot;: &quot;今天游览了故宫博物院&quot;,
    &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
    &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;isPublic&quot;: true
  }
}
</code></pre>



**请求示例**:


```javascript
{
  "content": "",
  "isPublic": true
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|tripNoteLogCreateDto|创建日志参数|body|true|TripNoteLogCreateDto|TripNoteLogCreateDto|
|&emsp;&emsp;content|日志内容||true|string||
|&emsp;&emsp;isPublic|是否公开||false|boolean||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 创建图片日志


**接口地址**:`/api/trip-logs/image`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>上传图片文件，最大为3MB</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;logId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
    &quot;logType&quot;: &quot;IMAGE&quot;,
    &quot;content&quot;: &quot;http://example.com/image.jpg&quot;,
    &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
    &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;isPublic&quot;: true
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|imgFile|图片文件|query|true|file||
|isPublic|是否公开|query|false|boolean||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取当前用户某个旅程所有日志


**接口地址**:`/api/trip-logs/trip`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取当前用户某个旅程所有日志</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;logId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
      &quot;logType&quot;: &quot;NOTE&quot;,
      &quot;content&quot;: &quot;今天游览了故宫博物院&quot;,
      &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
      &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;isPublic&quot;: true
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取当前用户某个旅程指定类型日志


**接口地址**:`/api/trip-logs/trip/type`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取当前用户某个旅程指定类型日志</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;logId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
      &quot;logType&quot;: &quot;NOTE&quot;,
      &quot;content&quot;: &quot;今天游览了故宫博物院&quot;,
      &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
      &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;isPublic&quot;: true
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|type|日志类型,可用值:NOTE,IMAGE|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取旅程公开日志


**接口地址**:`/api/trip-logs/trip/public`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取旅程公开日志</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;logId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
      &quot;logType&quot;: &quot;NOTE&quot;,
      &quot;content&quot;: &quot;今天游览了故宫博物院&quot;,
      &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
      &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;isPublic&quot;: true
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 删除日志


**接口地址**:`/api/trip-logs/delete`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>删除日志</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;删除成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|logId|日志ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


# 支出管理


## 更新支出记录


**接口地址**:`/api/trip-expenses/update`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>更新支出记录</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;expenseId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
    &quot;amount&quot;: 150.00,
    &quot;category&quot;: &quot;ACCOMMODATION&quot;,
    &quot;description&quot;: &quot;酒店住宿费用&quot;,
    &quot;expenseTime&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
  }
}
</code></pre>



**请求示例**:


```javascript
{
  "amount": 0,
  "category": "",
  "description": "",
  "expenseTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|expenseId|支出ID|query|true|string(uuid)||
|tripExpenseCreateUpdateDto|支出创建参数|body|true|TripExpenseCreateUpdateDto|TripExpenseCreateUpdateDto|
|&emsp;&emsp;amount|金额||true|number||
|&emsp;&emsp;category|分类,可用值:TRANSPORTATION,ACCOMMODATION,DINING,SIGHTSEEING,SHOPPING,OTHER||true|string||
|&emsp;&emsp;description|描述||false|string||
|&emsp;&emsp;expenseTime|支出时间||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 批量添加支出记录


**接口地址**:`/api/trip-expenses/batch-add`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>批量添加支出记录</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;批量添加成功&quot;
}
</code></pre>



**请求示例**:


```javascript
[
  {
    "amount": 0,
    "category": "",
    "description": "",
    "expenseTime": ""
  }
]
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|tripExpenseCreateUpdateDtos|支出创建参数|body|true|array|TripExpenseCreateUpdateDto|
|&emsp;&emsp;amount|金额||true|number||
|&emsp;&emsp;category|分类,可用值:TRANSPORTATION,ACCOMMODATION,DINING,SIGHTSEEING,SHOPPING,OTHER||true|string||
|&emsp;&emsp;description|描述||false|string||
|&emsp;&emsp;expenseTime|支出时间||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 添加单笔支出


**接口地址**:`/api/trip-expenses/add`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>添加单笔支出</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;expenseId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
    &quot;amount&quot;: 150.00,
    &quot;category&quot;: &quot;ACCOMMODATION&quot;,
    &quot;description&quot;: &quot;酒店住宿费用&quot;,
    &quot;expenseTime&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
  }
}
</code></pre>



**请求示例**:


```javascript
{
  "amount": 0,
  "category": "",
  "description": "",
  "expenseTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||
|tripExpenseCreateUpdateDto|支出创建参数|body|true|TripExpenseCreateUpdateDto|TripExpenseCreateUpdateDto|
|&emsp;&emsp;amount|金额||true|number||
|&emsp;&emsp;category|分类,可用值:TRANSPORTATION,ACCOMMODATION,DINING,SIGHTSEEING,SHOPPING,OTHER||true|string||
|&emsp;&emsp;description|描述||false|string||
|&emsp;&emsp;expenseTime|支出时间||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取指定旅程的总支出统计信息


**接口地址**:`/api/trip-expenses/statistics/total`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取指定旅程的总支出统计信息</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;count&quot;: 10,
    &quot;sum&quot;: 1500.00,
    &quot;min&quot;: 50.00,
    &quot;average&quot;: 150.00,
    &quot;max&quot;: 300.00
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取指定旅程的各类支出统计信息


**接口地址**:`/api/trip-expenses/statistics/category`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取指定旅程的各类支出统计信息</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;ACCOMMODATION&quot;: {
      &quot;count&quot;: 2,
      &quot;sum&quot;: 500.00,
      &quot;min&quot;: 200.00,
      &quot;average&quot;: 250.00,
      &quot;max&quot;: 300.00
    },
    &quot;FOOD&quot;: {
      &quot;count&quot;: 5,
      &quot;sum&quot;: 200.00,
      &quot;min&quot;: 20.00,
      &quot;average&quot;: 40.00,
      &quot;max&quot;: 60.00
    }
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取指定旅程的所有支出记录


**接口地址**:`/api/trip-expenses/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取指定旅程的所有支出记录</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;expenseId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
      &quot;amount&quot;: 150.00,
      &quot;category&quot;: &quot;ACCOMMODATION&quot;,
      &quot;description&quot;: &quot;酒店住宿费用&quot;,
      &quot;expenseTime&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|tripId|旅程ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取单笔支出详情


**接口地址**:`/api/trip-expenses/detail`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取单笔支出详情</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;expenseId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
    &quot;amount&quot;: 150.00,
    &quot;category&quot;: &quot;ACCOMMODATION&quot;,
    &quot;description&quot;: &quot;酒店住宿费用&quot;,
    &quot;expenseTime&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|expenseId|支出ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 删除支出记录


**接口地址**:`/api/trip-expenses/delete`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>删除支出记录</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;删除成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|expenseId|支出ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


# AI推荐会话管理


## 重命名AI推荐会话


**接口地址**:`/api/recommendation/conversation/rename`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>重命名一个AI推荐会话</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;conversationId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;title&quot;: &quot;北京之旅&quot;,
    &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
    &quot;updatedAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|conversationId|会话ID|query|true|string(uuid)||
|newTitle|新标题|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 处理用户对话


**接口地址**:`/api/recommendation/conversation/handle`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>处理用户的当前问题</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;aiMessages&quot;: [&quot;您好，推荐您去故宫博物院参观&quot;, &quot;故宫是中国古代宫殿建筑群...&quot;],
    &quot;toolUse&quot;: [&quot;POI_SEARCH&quot;, &quot;WEB_SEARCH&quot;],
    &quot;toolCallResults&quot;: {
      &quot;POI_SEARCH&quot;: [&quot;故宫博物院&quot;, &quot;天安门广场&quot;],
      &quot;WEB_SEARCH&quot;: [&quot;故宫开放时间&quot;]
    }
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|conversationId|会话ID|query|true|string(uuid)||
|queryText|查询文本|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 创建AI推荐会话


**接口地址**:`/api/recommendation/conversation/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>创建一个AI推荐会话</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;conversationId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;title&quot;: &quot;北京旅游推荐&quot;,
    &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
    &quot;updatedAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|queryText|查询文本|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取AI推荐会话


**接口地址**:`/api/recommendation/conversation/{conversationId}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取一个AI推荐会话</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;conversationId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;title&quot;: &quot;北京旅游推荐&quot;,
    &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
    &quot;updatedAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
  }
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|conversationId|会话ID|path|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取AI推荐会话历史


**接口地址**:`/api/recommendation/conversation/history/{conversationId}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取一个AI推荐会话的历史内容</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;role&quot;: &quot;user&quot;,
      &quot;content&quot;: &quot;我想去北京&quot;
    },
    {
      &quot;role&quot;: &quot;assistant&quot;,
      &quot;content&quot;: &quot;好的，推荐...&quot;
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|conversationId|会话ID|path|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取所有AI推荐会话


**接口地址**:`/api/recommendation/conversation/all`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取当前用户下的所有AI推荐会话</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;conversationId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;userId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;title&quot;: &quot;北京旅游推荐&quot;,
      &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
      &quot;updatedAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 删除AI推荐会话


**接口地址**:`/api/recommendation/conversation/delete`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>删除一个AI推荐会话</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: true
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|conversationId|会话ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


# 非POI项管理


## 更新非POI项


**接口地址**:`/api/nonPoiItem/update`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>更新非POI项</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;id&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;type&quot;: &quot;ACTIVITY&quot;,
    &quot;title&quot;: &quot;全聚德烤鸭店&quot;,
    &quot;description&quot;: &quot;正宗北京烤鸭&quot;,
    &quot;city&quot;: &quot;北京&quot;,
    &quot;activityTime&quot;: &quot;10:00-22:00&quot;,
    &quot;estimatedAddress&quot;: &quot;北京市东城区前门大街32号&quot;,
    &quot;extraInfo&quot;: &quot;人均消费200-300元&quot;,
    &quot;sourceUrl&quot;: &quot;https://example.com&quot;,
    &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
    &quot;privateUserId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;
  }
}
</code></pre>



**请求示例**:


```javascript
{
  "id": "",
  "type": "ACTIVITY",
  "title": "全聚德烤鸭店",
  "description": "正宗北京烤鸭",
  "city": "北京",
  "activityTime": "10:00-22:00",
  "estimatedAddress": "北京市东城区前门大街32号",
  "extraInfo": "人均消费200-300元",
  "sourceUrl": "https://example.com"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|nonPoiItemDto|非POI项dto|body|true|NonPoiItemDto|NonPoiItemDto|
|&emsp;&emsp;id|非POI项ID||false|string(uuid)||
|&emsp;&emsp;type|非POI项类型,可用值:ACTIVITY,FOOD,CULTURE,OTHER||false|string||
|&emsp;&emsp;title|标题||false|string||
|&emsp;&emsp;description|描述||false|string||
|&emsp;&emsp;city|城市||false|string||
|&emsp;&emsp;activityTime|活动时间||false|string||
|&emsp;&emsp;estimatedAddress|预计地址||false|string||
|&emsp;&emsp;extraInfo|额外信息||false|string||
|&emsp;&emsp;sourceUrl|来源URL||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 创建非POI项


**接口地址**:`/api/nonPoiItem/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>创建非POI项</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: {
    &quot;id&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
    &quot;type&quot;: &quot;ACTIVITY&quot;,
    &quot;title&quot;: &quot;全聚德烤鸭店&quot;,
    &quot;description&quot;: &quot;正宗北京烤鸭&quot;,
    &quot;city&quot;: &quot;北京&quot;,
    &quot;activityTime&quot;: &quot;10:00-22:00&quot;,
    &quot;estimatedAddress&quot;: &quot;北京市东城区前门大街32号&quot;,
    &quot;extraInfo&quot;: &quot;人均消费200-300元&quot;,
    &quot;sourceUrl&quot;: &quot;https://example.com&quot;,
    &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
    &quot;privateUserId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;
  }
}
</code></pre>



**请求示例**:


```javascript
{
  "id": "",
  "type": "ACTIVITY",
  "title": "全聚德烤鸭店",
  "description": "正宗北京烤鸭",
  "city": "北京",
  "activityTime": "10:00-22:00",
  "estimatedAddress": "北京市东城区前门大街32号",
  "extraInfo": "人均消费200-300元",
  "sourceUrl": "https://example.com"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|nonPoiItemDto|非POI项dto|body|true|NonPoiItemDto|NonPoiItemDto|
|&emsp;&emsp;id|非POI项ID||false|string(uuid)||
|&emsp;&emsp;type|非POI项类型,可用值:ACTIVITY,FOOD,CULTURE,OTHER||false|string||
|&emsp;&emsp;title|标题||false|string||
|&emsp;&emsp;description|描述||false|string||
|&emsp;&emsp;city|城市||false|string||
|&emsp;&emsp;activityTime|活动时间||false|string||
|&emsp;&emsp;estimatedAddress|预计地址||false|string||
|&emsp;&emsp;extraInfo|额外信息||false|string||
|&emsp;&emsp;sourceUrl|来源URL||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取用户的非POI项列表


**接口地址**:`/api/nonPoiItem/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取用户的非POI项列表</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;id&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;type&quot;: &quot;ACTIVITY&quot;,
      &quot;title&quot;: &quot;全聚德烤鸭店&quot;,
      &quot;description&quot;: &quot;正宗北京烤鸭&quot;,
      &quot;city&quot;: &quot;北京&quot;,
      &quot;activityTime&quot;: &quot;10:00-22:00&quot;,
      &quot;estimatedAddress&quot;: &quot;北京市东城区前门大街32号&quot;,
      &quot;extraInfo&quot;: &quot;人均消费200-300元&quot;,
      &quot;sourceUrl&quot;: &quot;https://example.com&quot;,
      &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
      &quot;privateUserId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 删除非POI项


**接口地址**:`/api/nonPoiItem/delete`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>删除非POI项</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;删除成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|nonPoiItemId|非POI项ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


# 心愿单服务


## 添加心愿单item


**接口地址**:`/api/wishlistItems/add`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>添加心愿单item</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;添加成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tripId|旅程ID|query|true|string(uuid)||
|entityId|实体ID|query|true|string(uuid)||
|isPoi|是否为POI|query|true|boolean||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取某个行程下的心愿单item列表


**接口地址**:`/api/wishlistItems/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>心愿单列表，包括每个item对应的entity信息。按照添加的顺序倒序排序</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;item&quot;: {
        &quot;itemId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
        &quot;tripId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
        &quot;entityId&quot;: &quot;123e4567-e89b-12d3-a456-426614174002&quot;,
        &quot;isPoi&quot;: true,
        &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
      },
      &quot;entity&quot;: {
        &quot;poiId&quot;: &quot;123e4567-e89b-12d3-a456-426614174002&quot;,
        &quot;name&quot;: &quot;故宫博物院&quot;,
        &quot;type&quot;: &quot;museum&quot;,
        &quot;city&quot;: &quot;北京&quot;,
        &quot;address&quot;: &quot;北京市东城区景山前街4号&quot;,
        &quot;latitude&quot;: 39.9163,
        &quot;longitude&quot;: 116.3972,
        &quot;description&quot;: &quot;明清两朝的皇宫...&quot;,
        &quot;openingHours&quot;: &quot;08:30-17:00&quot;,
        &quot;avgVisitDuration&quot;: 180,
        &quot;avgCost&quot;: &quot;60元&quot;,
        &quot;photos&quot;: [&quot;url1&quot;],
        &quot;phone&quot;: &quot;010-85007421&quot;,
        &quot;rating&quot;: &quot;4.8&quot;,
        &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
      }
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tripId|旅程ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 删除心愿单item


**接口地址**:`/api/wishlistItems/delete`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>删除心愿单item</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;删除成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|itemIds|心愿单item ID列表|query|true|array|string|


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


# AI推荐项管理


## 获取会话推荐的pois


**接口地址**:`/api/recommendation/items/pois`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取会话推荐的pois</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;poiId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;externalApiId&quot;: &quot;B0FFH4XYZ&quot;,
      &quot;name&quot;: &quot;故宫博物院&quot;,
      &quot;type&quot;: &quot;museum&quot;,
      &quot;city&quot;: &quot;北京&quot;,
      &quot;address&quot;: &quot;北京市东城区景山前街4号&quot;,
      &quot;latitude&quot;: 39.9163,
      &quot;longitude&quot;: 116.3972,
      &quot;description&quot;: &quot;明清两朝的皇宫...&quot;,
      &quot;openingHours&quot;: &quot;08:30-17:00&quot;,
      &quot;avgVisitDuration&quot;: 180,
      &quot;avgCost&quot;: &quot;60元&quot;,
      &quot;photos&quot;: [&quot;url1&quot;, &quot;url2&quot;],
      &quot;phone&quot;: &quot;010-85007421&quot;,
      &quot;rating&quot;: &quot;4.8&quot;,
      &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|conversationId|会话ID|query|true|string(uuid)||
|isManual|是否为手动添加|query|false|boolean||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 手动添加pois到会话推荐项中


**接口地址**:`/api/recommendation/items/pois`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>手动添加pois到会话推荐项中</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;添加成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|conversationId|会话ID|query|true|string(uuid)||
|poiIds|POI ID列表|query|true|array|string|


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 删除会话推荐中的pois


**接口地址**:`/api/recommendation/items/pois`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>删除会话推荐中的pois，如果poiIds为空，则删除该会话推荐中的所有</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;删除成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|conversationId|会话ID|query|true|string(uuid)||
|poiIds|POI ID列表|query|false|array|string|


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 获取会话推荐的nonPois


**接口地址**:`/api/recommendation/items/nonPois`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取会话推荐的nonPois</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;id&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;type&quot;: &quot;ACTIVITY&quot;,
      &quot;title&quot;: &quot;全聚德烤鸭店&quot;,
      &quot;description&quot;: &quot;正宗北京烤鸭&quot;,
      &quot;city&quot;: &quot;北京&quot;,
      &quot;activityTime&quot;: &quot;10:00-22:00&quot;,
      &quot;estimatedAddress&quot;: &quot;北京市东城区前门大街32号&quot;,
      &quot;extraInfo&quot;: &quot;人均消费200-300元&quot;,
      &quot;sourceUrl&quot;: &quot;https://example.com&quot;,
      &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
      &quot;privateUserId&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|conversationId|会话ID|query|true|string(uuid)||
|isManual|是否为手动添加|query|false|boolean||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 手动添加nonPois到会话推荐项中


**接口地址**:`/api/recommendation/items/nonPois`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>手动添加nonPois到会话推荐项中</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;添加成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|conversationId|会话ID|query|true|string(uuid)||
|nonPoiItemIds|非POI项ID列表|query|true|array|string|


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


## 删除会话推荐中的nonPois


**接口地址**:`/api/recommendation/items/nonPois`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>删除会话推荐中的nonPois，如果nonPoiItemIds为空，则删除该会话推荐中的所有</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: &quot;删除成功&quot;
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|true|string(uuid)||
|conversationId|会话ID|query|true|string(uuid)||
|nonPoiItemIds|非POI项ID列表|query|false|array|string|


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


# 网页管理


## 获取会话下的所有网页


**接口地址**:`/api/webPage/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取会话下的所有网页</p>
<p>Response Example:</p>
<pre><code class="language-json">{
  &quot;code&quot;: 200,
  &quot;message&quot;: &quot;success&quot;,
  &quot;data&quot;: [
    {
      &quot;id&quot;: &quot;123e4567-e89b-12d3-a456-426614174000&quot;,
      &quot;conversationId&quot;: &quot;123e4567-e89b-12d3-a456-426614174001&quot;,
      &quot;name&quot;: &quot;故宫博物院官网&quot;,
      &quot;url&quot;: &quot;https://www.dpm.org.cn&quot;,
      &quot;displayUrl&quot;: &quot;www.dpm.org.cn&quot;,
      &quot;snippet&quot;: &quot;故宫博物院官方网站...&quot;,
      &quot;summary&quot;: &quot;详细介绍故宫的历史和文化...&quot;,
      &quot;siteName&quot;: &quot;故宫博物院&quot;,
      &quot;datePublished&quot;: &quot;2023-10-01T12:00:00+08:00&quot;,
      &quot;createdAt&quot;: &quot;2023-10-01T12:00:00+08:00&quot;
    }
  ]
}
</code></pre>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|conversationId|会话ID|query|true|string(uuid)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|
|400|Bad Request|Result|
|500|Internal Server Error|Result|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```


**响应状态码-500**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```