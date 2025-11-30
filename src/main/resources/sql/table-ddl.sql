create table if not exists public.users
(
    user_id          uuid                     default gen_random_uuid() not null
        primary key,
    username         text                                               not null
        unique,
    preferences_text text,
    created_at       timestamp with time zone default CURRENT_TIMESTAMP not null
);

alter table public.users
    owner to postgres;

create table if not exists public.pois
(
    poi_id             uuid                     default gen_random_uuid() not null
        primary key,
    external_api_id    text,
    name               text                                               not null,
    type               varchar(512)                                       not null,
    address            text,
    latitude           numeric(10, 7),
    longitude          numeric(10, 7),
    description        text,
    opening_hours      text,
    avg_visit_duration integer,
    avg_cost           varchar(512),
    created_at         timestamp with time zone default CURRENT_TIMESTAMP,
    city               varchar(10)                                        not null,
    photos             varchar(2048),
    phone              varchar(128),
    rating             varchar(128)
);

comment on column public.pois.phone is '电话';

alter table public.pois
    owner to postgres;

create index if not exists pois_city_index
    on public.pois (city);

create unique index if not exists idx_pois_external_api_id
    on public.pois (external_api_id);

create table if not exists public.trips
(
    trip_id          uuid                     default gen_random_uuid() not null
        primary key,
    user_id          uuid                                               not null
        references public.users
            on delete cascade,
    title            varchar(50)                                        not null,
    destination_city text,
    start_date       date,
    end_date         date,
    total_budget     numeric(10, 2),
    status           trip_status              default 'PLANNING'::trip_status,
    description      text,
    created_at       timestamp with time zone default CURRENT_TIMESTAMP,
    is_private       boolean                  default true              not null
);

comment on column public.trips.user_id is '创建的用户id';

comment on column public.trips.title is '行程标题';

comment on column public.trips.description is '''描述信息''';

comment on column public.trips.is_private is '是否私有';

alter table public.trips
    owner to postgres;

create index if not exists idx_trips_user_id
    on public.trips (user_id);

create table if not exists public.trip_ai_conversation_history
(
    message_id      uuid                     default gen_random_uuid() not null
        constraint ai_conversation_history_pkey
            primary key,
    trip_id         uuid                                               not null
        constraint ai_conversation_history_trip_id_fkey
            references public.trips
            on delete cascade,
    role            role_type                                          not null,
    message_content text                                               not null,
    timestamp       timestamp with time zone default CURRENT_TIMESTAMP
);

alter table public.trip_ai_conversation_history
    owner to postgres;

create index if not exists idx_ai_history_trip_id
    on public.trip_ai_conversation_history (trip_id);

create table if not exists public.trip_days
(
    trip_day_id uuid default gen_random_uuid() not null
        primary key,
    trip_id     uuid                           not null
        references public.trips
            on delete cascade,
    day_date    date                           not null,
    notes       text
);

alter table public.trip_days
    owner to postgres;

create unique index if not exists idx_trip_days_trip_id
    on public.trip_days (trip_id, day_date);

create table if not exists public.trip_day_items
(
    item_id         uuid default gen_random_uuid() not null
        constraint itinerary_items_pkey
            primary key,
    trip_day_id     uuid                           not null
        constraint itinerary_items_trip_day_id_fkey
            references public.trip_days
            on delete cascade,
    entity_id       uuid                           not null,
    start_time      time,
    end_time        time,
    item_order      double precision               not null,
    transport_notes text,
    estimated_cost  numeric(10, 2),
    is_poi          boolean                        not null,
    notes           text
);

comment on column public.trip_day_items.transport_notes is '交通建议，从上一个地点到当前地点';

comment on column public.trip_day_items.is_poi is '是否为poi类型';

comment on column public.trip_day_items.notes is '描述信息';

alter table public.trip_day_items
    owner to postgres;

create index if not exists idx_itinerary_items_trip_day_id
    on public.trip_day_items (trip_day_id);

create table if not exists public.trip_expenses
(
    expense_id   uuid                     default gen_random_uuid() not null
        constraint expenses_pkey
            primary key,
    trip_id      uuid                                               not null,
    amount       numeric(10, 2)                                     not null,
    category     varchar(128)                                       not null,
    description  text,
    expense_time timestamp with time zone default CURRENT_TIMESTAMP not null,
    user_id      uuid                                               not null
        constraint expenses_user_id_fkey
            references public.users
            on delete cascade
);

comment on column public.trip_expenses.category is '支出分类';

comment on column public.trip_expenses.user_id is '用户id';

alter table public.trip_expenses
    owner to postgres;

create index if not exists idx_expenses_trip_id
    on public.trip_expenses (trip_id);

create index if not exists idx_expenses_user_id
    on public.trip_expenses (user_id);

create table if not exists public.trip_logs
(
    log_id     uuid                     default gen_random_uuid() not null
        primary key,
    trip_id    uuid                                               not null,
    log_type   text                                               not null,
    content    text                                               not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP,
    user_id    uuid                                               not null,
    is_public  boolean                  default false             not null
);

comment on column public.trip_logs.log_type is '类型';

comment on column public.trip_logs.is_public is '是否公开';

alter table public.trip_logs
    owner to postgres;

create index if not exists trip_logs_trip_id_user_id_index
    on public.trip_logs (trip_id, user_id);

create table if not exists public.trip_members
(
    trip_id    uuid                                                   not null
        references public.trips
            on delete cascade,
    user_id    uuid                                                   not null
        references public.users
            on delete cascade,
    role       member_role              default 'VIEWER'::member_role not null,
    is_pass    boolean                  default false                 not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP     not null,
    id         uuid                     default gen_random_uuid()     not null
        primary key
);

comment on column public.trip_members.is_pass is '是否经过创建者同意';

alter table public.trip_members
    owner to postgres;

create index if not exists idx_trip_members_user_id
    on public.trip_members (user_id);

create index if not exists idx_trip_members_trip_id
    on public.trip_members (trip_id);

create unique index if not exists trip_members_trip_id_user_id_uindex
    on public.trip_members (trip_id, user_id);

create table if not exists public.vector_store
(
    id        uuid default uuid_generate_v4() not null
        primary key,
    content   text,
    metadata  json,
    embedding vector(1536)
);

alter table public.vector_store
    owner to postgres;

create index if not exists vector_store_embedding_idx
    on public.vector_store using hnsw (embedding public.vector_cosine_ops);

create table if not exists public.ai_recommendation_items
(
    entity_id       uuid                                  not null,
    created_at      timestamp with time zone default CURRENT_TIMESTAMP,
    conversation_id uuid                                  not null,
    is_poi          boolean                               not null,
    is_manual       boolean                  default true not null
);

comment on column public.ai_recommendation_items.entity_id is '推荐的entity_id';

comment on column public.ai_recommendation_items.is_poi is '是否为poi类型';

alter table public.ai_recommendation_items
    owner to postgres;

create index if not exists ai_recommendation_items_conversation_id_is_poi_is_manual_index
    on public.ai_recommendation_items (conversation_id, is_poi, is_manual);

create table if not exists public.ai_recommendation_conversation
(
    conversation_id uuid                     default gen_random_uuid() not null
        primary key,
    user_id         uuid                                               not null
        references public.users
            on delete cascade,
    title           varchar(256)                                       not null,
    created_at      timestamp with time zone default CURRENT_TIMESTAMP,
    updated_at      timestamp with time zone default CURRENT_TIMESTAMP
);

alter table public.ai_recommendation_conversation
    owner to postgres;

create index if not exists ai_recommendation_conversation_user_id_index
    on public.ai_recommendation_conversation (user_id);

create table if not exists public.non_poi_item
(
    id                uuid                     default gen_random_uuid()     not null
        constraint activity_pkey
            primary key,
    title             text                                                   not null,
    description       text                                                   not null,
    city              varchar(128),
    activity_time     text,
    estimated_address text,
    extra_info        text,
    source_url        text                                                   not null,
    created_at        timestamp with time zone default CURRENT_TIMESTAMP,
    private_user_id   uuid,
    type              non_poi_type             default 'OTHER'::non_poi_type not null
);

comment on column public.non_poi_item.private_user_id is '私人活动的userId';

comment on column public.non_poi_item.type is '类型';

alter table public.non_poi_item
    owner to postgres;

create index if not exists activity_private_user_id_index
    on public.non_poi_item (private_user_id);

create table if not exists public.spring_ai_chat_memory
(
    conversation_id varchar(36) not null,
    content         text        not null,
    type            varchar(10) not null
        constraint spring_ai_chat_memory_type_check
            check ((type)::text = ANY
                   (ARRAY [('USER'::character varying)::text, ('ASSISTANT'::character varying)::text, ('SYSTEM'::character varying)::text, ('TOOL'::character varying)::text])),
    timestamp       timestamp   not null
);

comment on column public.spring_ai_chat_memory.conversation_id is 'uuid';

alter table public.spring_ai_chat_memory
    owner to postgres;

create index if not exists spring_ai_chat_memory_conversation_id_timestamp_idx
    on public.spring_ai_chat_memory (conversation_id, timestamp);

create table if not exists public.web_page
(
    id              uuid                     default gen_random_uuid() not null
        primary key,
    conversation_id uuid                                               not null,
    name            text,
    url             text                                               not null,
    display_url     text,
    snippet         text,
    summary         text,
    site_name       varchar(200),
    date_published  timestamp,
    created_at      timestamp with time zone default CURRENT_TIMESTAMP
);

alter table public.web_page
    owner to postgres;

create index if not exists web_page_conversation_id_idx
    on public.web_page (conversation_id);

create table if not exists public.wishlist_items
(
    item_id    uuid                     default gen_random_uuid() not null
        constraint wishlist_items_pk
            primary key,
    trip_id    uuid                                               not null
        references public.trips
            on delete cascade,
    entity_id  uuid                                               not null,
    is_poi     boolean                                            not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null
);

comment on column public.wishlist_items.trip_id is '当前心愿单item所属的trip_id';

comment on column public.wishlist_items.entity_id is 'poiId或者nonPoiId';

comment on column public.wishlist_items.is_poi is '是否为poi类型';

alter table public.wishlist_items
    owner to postgres;

create unique index if not exists wishlist_items_trip_id_index
    on public.wishlist_items (trip_id, entity_id);

