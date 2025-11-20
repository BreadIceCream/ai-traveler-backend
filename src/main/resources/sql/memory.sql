-- auto-generated definition
create table spring_ai_chat_memory
(
    conversation_id uuid not null,
    content         text        not null,
    type            varchar(10) not null
        constraint spring_ai_chat_memory_type_check
            check ((type)::text = ANY
                   ((ARRAY ['USER'::character varying, 'ASSISTANT'::character varying, 'SYSTEM'::character varying, 'TOOL'::character varying])::text[])),
    timestamp       timestamp   not null
);

alter table spring_ai_chat_memory
    owner to postgres;

create index spring_ai_chat_memory_conversation_id_timestamp_idx
    on spring_ai_chat_memory (conversation_id, timestamp);