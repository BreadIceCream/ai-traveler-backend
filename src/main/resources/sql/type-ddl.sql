-- Unknown how to generate base type type

alter type public.vector owner to postgres;

-- Unknown how to generate base type type

alter type public.halfvec owner to postgres;

-- Unknown how to generate base type type

alter type public.sparsevec owner to postgres;

create type public.role_type as enum ('USER', 'AI');

alter type public.role_type owner to postgres;

create type public.poi_type as enum ('ATTRACTION', 'RESTAURANT', 'HOTEL', 'SHOPPING', 'CUSTOM');

alter type public.poi_type owner to postgres;

create type public.trip_status as enum ('PLANNING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');

alter type public.trip_status owner to postgres;

create type public.log_type as enum ('NOTE', 'IMAGE_URL', 'VIDEO_URL');

alter type public.log_type owner to postgres;

create type public.member_role as enum ('OWNER', 'EDITOR', 'VIEWER');

alter type public.member_role owner to postgres;

-- Unknown how to generate base type type

alter type public.hstore owner to postgres;

-- Unknown how to generate base type type

alter type public.ghstore owner to postgres;

create type public.non_poi_type as enum ('ACTIVITY', 'FOOD', 'CULTURE', 'OTHER');

alter type public.non_poi_type owner to postgres;

