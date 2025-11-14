-- 启用必要的 PostgreSQL 扩展
-- pgcrypto: 用于生成 UUID (gen_random_uuid())
-- vector:   用于 AI 向量存储 (pgvector)
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS vector;

-- ---
-- 1. 定义自定义枚举类型 (Enums)
-- ---
CREATE TYPE role_type AS ENUM ('USER', 'AI');
CREATE TYPE poi_type AS ENUM ('ATTRACTION', 'RESTAURANT', 'HOTEL', 'SHOPPING', 'CUSTOM');
CREATE TYPE trip_status AS ENUM ('PLANNING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');
CREATE TYPE log_type AS ENUM ('NOTE', 'IMAGE_URL', 'VIDEO_URL');
CREATE TYPE member_role AS ENUM ('OWNER', 'EDITOR', 'VIEWER');

-- ---
-- 2. 创建表结构
-- ---

-- 模块 1: 用户与偏好
CREATE TABLE users (
                       user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username TEXT NOT NULL,
                       preferences_text TEXT, -- 用户自然语言偏好
                       preferences_embedding VECTOR(1536), -- (修改) 假设使用 1536 维的向量
                       created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 模块 2: 地点库 (POI)
CREATE TABLE pois (
                      poi_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                      external_api_id TEXT, -- 高德/Google 的地点 ID
                      name TEXT NOT NULL,
                      type poi_type NOT NULL,
                      address TEXT,
                      latitude NUMERIC(10, 7),
                      longitude NUMERIC(10, 7),
                      description TEXT,
                      description_embedding VECTOR(1536), -- (修改) 地点描述的向量
                      opening_hours JSONB, -- 存储营业时间
                      avg_visit_duration INT, -- 建议游玩时长（分钟）
                      price_level INT, -- 价格水平 (0-4)
                      created_by_user_id UUID REFERENCES users(user_id) ON DELETE SET NULL, -- 如果是用户自定义的
                      created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 为 external_api_id 创建索引，加速查询和去重
CREATE INDEX IF NOT EXISTS idx_pois_external_api_id ON pois(external_api_id);

-- 模块 3: 行程与日志
CREATE TABLE trips (
                       trip_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       user_id UUID NOT NULL REFERENCES users(user_id) ON DELETE CASCADE, -- 行程拥有者
                       name TEXT NOT NULL,
                       destination_city TEXT,
                       start_date DATE,
                       end_date DATE,
                       total_budget NUMERIC(10, 2),
                       status trip_status DEFAULT 'PLANNING',
                       trip_preferences JSONB, -- 本次旅行的特定偏好, e.g., {"pace": "relaxed"}
                       is_template BOOLEAN DEFAULT FALSE,
                       created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ai_conversation_history (
                                         message_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                         trip_id UUID NOT NULL REFERENCES trips(trip_id) ON DELETE CASCADE,
                                         role role_type NOT NULL,
                                         message_content TEXT NOT NULL,
                                         timestamp TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE poi_ratings (
                             rating_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             user_id UUID NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
                             poi_id UUID NOT NULL REFERENCES pois(poi_id) ON DELETE CASCADE,
                             rating INT CHECK (rating >= 1 AND rating <= 5),
                             review_text TEXT,
                             visited_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                             UNIQUE(user_id, poi_id) -- 同一个用户对同一个POI只能评价一次
);

CREATE TABLE trip_days (
                           trip_day_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           trip_id UUID NOT NULL REFERENCES trips(trip_id) ON DELETE CASCADE, -- 日程表高度依赖行程，级联删除
                           day_date DATE NOT NULL,
                           day_order INT NOT NULL, -- 第几天 (1, 2, 3...)
                           notes TEXT -- 当天备注
);

CREATE TABLE itinerary_items (
                                 item_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                 trip_day_id UUID NOT NULL REFERENCES trip_days(trip_day_id) ON DELETE CASCADE,
                                 poi_id UUID NOT NULL REFERENCES pois(poi_id) ON DELETE CASCADE, -- POI被删除时，行程项也删除
                                 start_time TIME,
                                 end_time TIME,
                                 item_order INT NOT NULL, -- 在这一天中的顺序
                                 transport_notes TEXT, -- 交通建议
                                 estimated_cost NUMERIC(10, 2)
);

CREATE TABLE wishlist_items (
                                user_id UUID NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
                                poi_id UUID NOT NULL REFERENCES pois(poi_id) ON DELETE CASCADE,
                                PRIMARY KEY (user_id, poi_id) -- 联合主键，防止重复
);

CREATE TABLE expenses (
                          expense_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          trip_id UUID NOT NULL REFERENCES trips(trip_id) ON DELETE CASCADE,
                          amount NUMERIC(10, 2) NOT NULL,
                          category TEXT,
                          description TEXT,
                          expense_date TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE packing_list_items (
                                    packing_item_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                    trip_id UUID NOT NULL REFERENCES trips(trip_id) ON DELETE CASCADE,
                                    item_name TEXT NOT NULL,
                                    is_checked BOOLEAN DEFAULT FALSE,
                                    category TEXT -- AI 建议的类别 (e.g., "证件")
);

CREATE TABLE trip_logs (
                           log_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           trip_day_id UUID NOT NULL REFERENCES trip_days(trip_day_id) ON DELETE CASCADE,
                           log_type log_type NOT NULL,
                           content TEXT NOT NULL, -- 笔记内容或 URL
                           created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- ---
-- V2 功能: 多人协作
-- ---
CREATE TABLE trip_members (
                              trip_id UUID NOT NULL REFERENCES trips(trip_id) ON DELETE CASCADE,
                              user_id UUID NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
                              role member_role NOT NULL DEFAULT 'VIEWER',
                              PRIMARY KEY (trip_id, user_id) -- 联合主键
);

-- ---
-- 3. 为外键 (FK) 创建索引 (推荐)
--    (主键 PK 默认已有索引)
-- ---
CREATE INDEX IF NOT EXISTS idx_trips_user_id ON trips(user_id);
CREATE INDEX IF NOT EXISTS idx_ai_history_trip_id ON ai_conversation_history(trip_id);
CREATE INDEX IF NOT EXISTS idx_ratings_user_id ON poi_ratings(user_id);
CREATE INDEX IF NOT EXISTS idx_ratings_poi_id ON poi_ratings(poi_id);
CREATE INDEX IF NOT EXISTS idx_trip_days_trip_id ON trip_days(trip_id);
CREATE INDEX IF NOT EXISTS idx_itinerary_items_trip_day_id ON itinerary_items(trip_day_id);
CREATE INDEX IF NOT EXISTS idx_itinerary_items_poi_id ON itinerary_items(poi_id);
CREATE INDEX IF NOT EXISTS idx_expenses_trip_id ON expenses(trip_id);
CREATE INDEX IF NOT EXISTS idx_packing_list_trip_id ON packing_list_items(trip_id);
CREATE INDEX IF NOT EXISTS idx_trip_logs_trip_day_id ON trip_logs(trip_day_id);
CREATE INDEX IF NOT EXISTS idx_trip_members_user_id ON trip_members(user_id);