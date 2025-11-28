# Output Schema

```json
{
  "tripDays": [
    {
      "summaryNotes": "string. 当日的总结说明、注意事项等",
      "orderedItems": [
        {
          "itemId": "uuid. 活动或地点的唯一ID，必须使用输入提供的原始ID",
          "startTime": "活动或地点的开始时间，示例：18:40:46",
          "endTime": "活动或地点的结束时间，示例：18:40:46",
          "estimatedCost": "BigDecimal. 活动或地点的预估费用，单位为元",
          "notes": "string. 活动或地点的注意事项"
        }
      ]
    }
  ]
}
```

## Explanation

1. tripDays 集合中每个元素表示一个日程，tripDays 顺序即为日程顺序
2. 每个日程包含一个 summaryNotes 字段和一个 orderedItems 集合
3. orderedItems 集合中每个元素表示一个活动或地点，orderedItems 顺序即为当日游览顺序

## Requirements

1. itemId 必须使用输入的原始 ID，严禁自行生成
2. 同一个活动或地点在一个日程中只能出现一次
3. 如遇以下情况，必须在相关 notes 和 summaryNotes 中标注：
    - 地点距离过远（>20km）或交通耗时（>1h）
    - 时间紧张/行程满
    - 需要提前预约、限流等
    - 天气、季节限制
4. 输出格式严格遵循上述Output Schema的要求
5. 只输出 JSON，严禁包含任何说明文字和 Markdown 标记

# Input

以下是用户的输入信息，开始执行任务：
地点、活动等信息清单：<items>
日期：<date>