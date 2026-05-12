create extension if not exists pgcrypto with schema extensions;

create or replace function public.set_updated_at()
returns trigger
language plpgsql
as $$
begin
  new.updated_at = now();
  return new;
end;
$$;

create table public.communities (
  id uuid primary key default gen_random_uuid(),
  parent_id uuid references public.communities(id) on delete cascade,
  name text not null,
  slug text not null,
  path text not null unique,
  level text not null check (level in ('country', 'city', 'local_community', 'neighborhood', 'sub_neighborhood')),
  description text,
  sort_order integer not null default 0,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint communities_parent_slug_unique unique (parent_id, slug)
);

create table public.anonymous_profiles (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null unique references auth.users(id) on delete cascade,
  alias text not null,
  avatar_seed text not null,
  community_id uuid references public.communities(id) on delete set null,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint anonymous_profiles_alias_not_blank check (length(trim(alias)) > 0),
  constraint anonymous_profiles_avatar_seed_not_blank check (length(trim(avatar_seed)) > 0)
);

create table public.mood_checkins (
  id uuid primary key default gen_random_uuid(),
  profile_id uuid not null references public.anonymous_profiles(id) on delete cascade,
  community_id uuid not null references public.communities(id) on delete restrict,
  mood text not null check (mood in ('happy', 'calm', 'sad', 'angry', 'anxious', 'lonely')),
  intensity smallint not null check (intensity between 1 and 5),
  private_note text,
  checkin_date date not null default current_date,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  metadata jsonb not null default '{}'::jsonb,
  constraint mood_checkins_one_per_profile_day unique (profile_id, checkin_date)
);

create table public.vents (
  id uuid primary key default gen_random_uuid(),
  profile_id uuid not null references public.anonymous_profiles(id) on delete cascade,
  community_id uuid not null references public.communities(id) on delete restrict,
  mood text not null check (mood in ('happy', 'calm', 'sad', 'angry', 'anxious', 'lonely')),
  body text not null,
  is_flagged boolean not null default false,
  moderation_status text not null default 'visible' check (moderation_status in ('visible', 'under_review', 'hidden')),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint vents_body_length check (length(trim(body)) between 1 and 2000)
);

create table public.vent_reactions (
  id uuid primary key default gen_random_uuid(),
  vent_id uuid not null references public.vents(id) on delete cascade,
  profile_id uuid not null references public.anonymous_profiles(id) on delete cascade,
  reaction_type text not null check (reaction_type in ('i_hear_you', 'celebrating_with_you', 'sending_warmth', 'you_are_not_alone', 'stay_strong')),
  created_at timestamptz not null default now(),
  constraint vent_reactions_unique unique (vent_id, profile_id, reaction_type)
);

create table public.matches (
  id uuid primary key default gen_random_uuid(),
  community_id uuid not null references public.communities(id) on delete restrict,
  initiator_profile_id uuid not null references public.anonymous_profiles(id) on delete cascade,
  partner_profile_id uuid references public.anonymous_profiles(id) on delete set null,
  status text not null default 'waiting' check (status in ('waiting', 'active', 'ended', 'cancelled')),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  ended_at timestamptz,
  constraint matches_distinct_profiles check (partner_profile_id is null or partner_profile_id <> initiator_profile_id)
);

create table public.chat_messages (
  id uuid primary key default gen_random_uuid(),
  match_id uuid not null references public.matches(id) on delete cascade,
  profile_id uuid not null references public.anonymous_profiles(id) on delete cascade,
  body text not null,
  moderation_status text not null default 'visible' check (moderation_status in ('visible', 'under_review', 'hidden')),
  created_at timestamptz not null default now(),
  constraint chat_messages_body_length check (length(trim(body)) between 1 and 1200)
);

create table public.reports (
  id uuid primary key default gen_random_uuid(),
  vent_id uuid references public.vents(id) on delete cascade,
  chat_message_id uuid references public.chat_messages(id) on delete cascade,
  reporter_profile_id uuid not null references public.anonymous_profiles(id) on delete cascade,
  reported_profile_id uuid references public.anonymous_profiles(id) on delete set null,
  reason text not null check (reason in ('harmful_content', 'harassment', 'spam', 'self_harm_risk', 'other')),
  details text,
  status text not null default 'open' check (status in ('open', 'reviewing', 'resolved', 'dismissed')),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint reports_exactly_one_target check (
    (vent_id is not null and chat_message_id is null)
    or (vent_id is null and chat_message_id is not null)
  )
);

create table public.expert_profiles (
  id uuid primary key default gen_random_uuid(),
  community_id uuid references public.communities(id) on delete set null,
  display_name text not null,
  role text not null check (role in ('therapist', 'listener', 'volunteer', 'moderator')),
  specialty text,
  avatar_seed text not null,
  availability_status text not null default 'offline' check (availability_status in ('available', 'busy', 'offline')),
  bio text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table public.demo_insights (
  id uuid primary key default gen_random_uuid(),
  community_id uuid references public.communities(id) on delete cascade,
  insight_type text not null check (insight_type in ('community_summary', 'ai_observation', 'support_note', 'weekly_flow')),
  title text not null,
  body text not null,
  severity text not null default 'info' check (severity in ('info', 'calm', 'watch', 'urgent')),
  sort_order integer not null default 0,
  created_at timestamptz not null default now()
);

create index communities_parent_id_idx on public.communities(parent_id);
create index communities_path_idx on public.communities(path);
create index anonymous_profiles_user_id_idx on public.anonymous_profiles(user_id);
create index anonymous_profiles_community_id_idx on public.anonymous_profiles(community_id);
create index mood_checkins_community_date_idx on public.mood_checkins(community_id, checkin_date desc);
create index mood_checkins_profile_date_idx on public.mood_checkins(profile_id, checkin_date desc);
create index vents_community_created_idx on public.vents(community_id, created_at desc);
create index vents_profile_created_idx on public.vents(profile_id, created_at desc);
create index vent_reactions_vent_id_idx on public.vent_reactions(vent_id);
create index reports_status_created_idx on public.reports(status, created_at desc);
create index reports_vent_id_idx on public.reports(vent_id);
create index reports_chat_message_id_idx on public.reports(chat_message_id);
create index matches_waiting_idx on public.matches(community_id, created_at) where status = 'waiting' and partner_profile_id is null;
create index matches_initiator_idx on public.matches(initiator_profile_id, created_at desc);
create index matches_partner_idx on public.matches(partner_profile_id, created_at desc);
create index chat_messages_match_created_idx on public.chat_messages(match_id, created_at);
create index expert_profiles_community_status_idx on public.expert_profiles(community_id, availability_status);
create index demo_insights_community_sort_idx on public.demo_insights(community_id, sort_order);

create trigger communities_set_updated_at
  before update on public.communities
  for each row execute function public.set_updated_at();

create trigger anonymous_profiles_set_updated_at
  before update on public.anonymous_profiles
  for each row execute function public.set_updated_at();

create trigger mood_checkins_set_updated_at
  before update on public.mood_checkins
  for each row execute function public.set_updated_at();

create trigger vents_set_updated_at
  before update on public.vents
  for each row execute function public.set_updated_at();

create trigger matches_set_updated_at
  before update on public.matches
  for each row execute function public.set_updated_at();

create trigger reports_set_updated_at
  before update on public.reports
  for each row execute function public.set_updated_at();

create trigger expert_profiles_set_updated_at
  before update on public.expert_profiles
  for each row execute function public.set_updated_at();

create or replace function public.current_profile_id()
returns uuid
language sql
stable
set search_path = public
as $$
  select id
  from public.anonymous_profiles
  where user_id = auth.uid()
  limit 1
$$;

create or replace function public.owns_profile(profile_id uuid)
returns boolean
language sql
stable
set search_path = public
as $$
  select exists (
    select 1
    from public.anonymous_profiles
    where id = profile_id
      and user_id = auth.uid()
  )
$$;

create or replace view public.community_mood_rollups
with (security_invoker = true)
as
select
  community_id,
  checkin_date,
  mood,
  count(*)::integer as checkin_count,
  round(avg(intensity)::numeric, 2) as average_intensity
from public.mood_checkins
group by community_id, checkin_date, mood;

create or replace view public.vent_report_counts
with (security_invoker = true)
as
select
  v.id as vent_id,
  v.community_id,
  count(r.id)::integer as report_count,
  max(r.created_at) as latest_reported_at
from public.vents v
left join public.reports r on r.vent_id = v.id
group by v.id, v.community_id;

alter table public.communities enable row level security;
alter table public.anonymous_profiles enable row level security;
alter table public.mood_checkins enable row level security;
alter table public.vents enable row level security;
alter table public.vent_reactions enable row level security;
alter table public.matches enable row level security;
alter table public.chat_messages enable row level security;
alter table public.reports enable row level security;
alter table public.expert_profiles enable row level security;
alter table public.demo_insights enable row level security;

create policy "Anyone can read communities"
  on public.communities for select
  to anon, authenticated
  using (true);

create policy "Authenticated users can read anonymous profiles"
  on public.anonymous_profiles for select
  to authenticated
  using (true);

create policy "Users can create their anonymous profile"
  on public.anonymous_profiles for insert
  to authenticated
  with check (user_id = auth.uid());

create policy "Users can update their anonymous profile"
  on public.anonymous_profiles for update
  to authenticated
  using (user_id = auth.uid())
  with check (user_id = auth.uid());

create policy "Authenticated users can read mood check-ins"
  on public.mood_checkins for select
  to authenticated
  using (true);

create policy "Users can create their mood check-ins"
  on public.mood_checkins for insert
  to authenticated
  with check (public.owns_profile(profile_id));

create policy "Users can update their mood check-ins"
  on public.mood_checkins for update
  to authenticated
  using (public.owns_profile(profile_id))
  with check (public.owns_profile(profile_id));

create policy "Authenticated users can read visible vents"
  on public.vents for select
  to authenticated
  using (moderation_status = 'visible' or public.owns_profile(profile_id));

create policy "Users can create visible vents"
  on public.vents for insert
  to authenticated
  with check (
    public.owns_profile(profile_id)
    and moderation_status = 'visible'
    and is_flagged = false
  );

create policy "Users can update their vents"
  on public.vents for update
  to authenticated
  using (public.owns_profile(profile_id))
  with check (public.owns_profile(profile_id));

create policy "Authenticated users can read reactions"
  on public.vent_reactions for select
  to authenticated
  using (true);

create policy "Users can create their reactions"
  on public.vent_reactions for insert
  to authenticated
  with check (public.owns_profile(profile_id));

create policy "Users can remove their reactions"
  on public.vent_reactions for delete
  to authenticated
  using (public.owns_profile(profile_id));

create policy "Users can inspect available or owned matches"
  on public.matches for select
  to authenticated
  using (
    public.owns_profile(initiator_profile_id)
    or public.owns_profile(partner_profile_id)
    or (status = 'waiting' and partner_profile_id is null)
  );

create policy "Users can create waiting matches"
  on public.matches for insert
  to authenticated
  with check (
    public.owns_profile(initiator_profile_id)
    and partner_profile_id is null
    and status = 'waiting'
  );

create policy "Participants can update matches"
  on public.matches for update
  to authenticated
  using (
    public.owns_profile(initiator_profile_id)
    or public.owns_profile(partner_profile_id)
    or (status = 'waiting' and partner_profile_id is null)
  )
  with check (
    public.owns_profile(initiator_profile_id)
    or public.owns_profile(partner_profile_id)
  );

create policy "Participants can read chat messages"
  on public.chat_messages for select
  to authenticated
  using (
    exists (
      select 1
      from public.matches m
      where m.id = match_id
        and (
          public.owns_profile(m.initiator_profile_id)
          or public.owns_profile(m.partner_profile_id)
        )
    )
  );

create policy "Participants can create chat messages"
  on public.chat_messages for insert
  to authenticated
  with check (
    public.owns_profile(profile_id)
    and exists (
      select 1
      from public.matches m
      where m.id = match_id
        and m.status = 'active'
        and (
          public.owns_profile(m.initiator_profile_id)
          or public.owns_profile(m.partner_profile_id)
        )
    )
  );

create policy "Authenticated users can read reports"
  on public.reports for select
  to authenticated
  using (true);

create policy "Users can create reports"
  on public.reports for insert
  to authenticated
  with check (public.owns_profile(reporter_profile_id));

create policy "Anyone can read expert profiles"
  on public.expert_profiles for select
  to anon, authenticated
  using (true);

create policy "Anyone can read demo insights"
  on public.demo_insights for select
  to anon, authenticated
  using (true);

grant usage on schema public to anon, authenticated;
grant select on public.communities, public.expert_profiles, public.demo_insights to anon, authenticated;
grant select, insert, update on public.anonymous_profiles to authenticated;
grant select, insert, update on public.mood_checkins to authenticated;
grant select, insert, update on public.vents to authenticated;
grant select, insert, delete on public.vent_reactions to authenticated;
grant select, insert, update on public.matches to authenticated;
grant select, insert on public.chat_messages to authenticated;
grant select, insert on public.reports to authenticated;
grant select on public.community_mood_rollups, public.vent_report_counts to authenticated;
grant execute on function public.current_profile_id() to authenticated;
grant execute on function public.owns_profile(uuid) to authenticated;

alter publication supabase_realtime add table public.vents;
alter publication supabase_realtime add table public.vent_reactions;
alter publication supabase_realtime add table public.matches;
alter publication supabase_realtime add table public.chat_messages;

insert into public.communities (id, parent_id, name, slug, path, level, description, sort_order)
values
  ('00000000-0000-4000-8000-000000000001', null, 'Kenya', 'kenya', 'kenya', 'country', 'Country-level emotional pulse for Kenya.', 1),
  ('00000000-0000-4000-8000-000000000002', '00000000-0000-4000-8000-000000000001', 'Nairobi', 'nairobi', 'kenya/nairobi', 'city', 'City-level emotional pulse for Nairobi.', 1),
  ('00000000-0000-4000-8000-000000000003', '00000000-0000-4000-8000-000000000002', 'Westlands', 'westlands', 'kenya/nairobi/westlands', 'local_community', 'A lively Nairobi community with mixed residential and business rhythms.', 1),
  ('00000000-0000-4000-8000-000000000004', '00000000-0000-4000-8000-000000000002', 'Upper Hill', 'upper-hill', 'kenya/nairobi/upper-hill', 'local_community', 'A high-motion work and residential district in Nairobi.', 2),
  ('00000000-0000-4000-8000-000000000005', '00000000-0000-4000-8000-000000000002', 'South B', 'south-b', 'kenya/nairobi/south-b', 'local_community', 'A warm Nairobi community with close neighborhood energy.', 3),
  ('00000000-0000-4000-8000-000000000006', '00000000-0000-4000-8000-000000000003', 'Brookside', 'brookside', 'kenya/nairobi/westlands/brookside', 'neighborhood', 'A calmer Westlands neighborhood used for focused demo storytelling.', 1),
  ('00000000-0000-4000-8000-000000000007', '00000000-0000-4000-8000-000000000003', 'Parklands', 'parklands', 'kenya/nairobi/westlands/parklands', 'neighborhood', 'A connected neighborhood with varied student, family, and workday patterns.', 2),
  ('00000000-0000-4000-8000-000000000008', '00000000-0000-4000-8000-000000000003', 'Kangemi', 'kangemi', 'kenya/nairobi/westlands/kangemi', 'neighborhood', 'A dense and expressive neighborhood near Westlands.', 3),
  ('00000000-0000-4000-8000-000000000009', '00000000-0000-4000-8000-000000000004', 'Hospital Hill', 'hospital-hill', 'kenya/nairobi/upper-hill/hospital-hill', 'neighborhood', 'A care-adjacent neighborhood for support and recovery storytelling.', 1),
  ('00000000-0000-4000-8000-000000000010', '00000000-0000-4000-8000-000000000004', 'Milimani', 'milimani', 'kenya/nairobi/upper-hill/milimani', 'neighborhood', 'A quieter administrative and residential pocket near Upper Hill.', 2),
  ('00000000-0000-4000-8000-000000000011', '00000000-0000-4000-8000-000000000005', 'Hazina', 'hazina', 'kenya/nairobi/south-b/hazina', 'neighborhood', 'A South B neighborhood with family-centered community rhythm.', 1),
  ('00000000-0000-4000-8000-000000000012', '00000000-0000-4000-8000-000000000005', 'Plainsview', 'plainsview', 'kenya/nairobi/south-b/plainsview', 'neighborhood', 'A South B neighborhood with active commuter energy.', 2)
on conflict (path) do update
set
  parent_id = excluded.parent_id,
  name = excluded.name,
  slug = excluded.slug,
  level = excluded.level,
  description = excluded.description,
  sort_order = excluded.sort_order,
  updated_at = now();

insert into public.expert_profiles (id, community_id, display_name, role, specialty, avatar_seed, availability_status, bio)
values
  ('10000000-0000-4000-8000-000000000001', '00000000-0000-4000-8000-000000000002', 'Amina', 'therapist', 'Stress and family support', 'amina-soft-orbit', 'available', 'Offers gentle grounding support for Nairobi community members.'),
  ('10000000-0000-4000-8000-000000000002', '00000000-0000-4000-8000-000000000003', 'Brian', 'listener', 'Peer listening', 'brian-calm-river', 'busy', 'Available for anonymous peer listening during high-pressure moments.'),
  ('10000000-0000-4000-8000-000000000003', '00000000-0000-4000-8000-000000000006', 'Nia', 'volunteer', 'Community care', 'nia-gentle-cloud', 'available', 'Helps route people toward warm community resources.')
on conflict (id) do update
set
  community_id = excluded.community_id,
  display_name = excluded.display_name,
  role = excluded.role,
  specialty = excluded.specialty,
  avatar_seed = excluded.avatar_seed,
  availability_status = excluded.availability_status,
  bio = excluded.bio,
  updated_at = now();

insert into public.demo_insights (id, community_id, insight_type, title, body, severity, sort_order)
values
  ('20000000-0000-4000-8000-000000000001', '00000000-0000-4000-8000-000000000002', 'community_summary', 'Nairobi feels steady today', 'Calm and anxious check-ins are moving together, with no sharp pressure spike in the city pulse.', 'calm', 1),
  ('20000000-0000-4000-8000-000000000002', '00000000-0000-4000-8000-000000000003', 'ai_observation', 'Westlands shows evening pressure', 'Recent vents suggest workday fatigue rises around commute hours, especially near denser neighborhoods.', 'watch', 2),
  ('20000000-0000-4000-8000-000000000003', '00000000-0000-4000-8000-000000000006', 'support_note', 'Brookside support is available', 'A listener and volunteer are available for anonymous support if the local pulse becomes heavier.', 'info', 3),
  ('20000000-0000-4000-8000-000000000004', '00000000-0000-4000-8000-000000000002', 'weekly_flow', 'Weekly flow is softening', 'The demo trend shows frustration easing while calm and hopeful check-ins slowly increase.', 'calm', 4)
on conflict (id) do update
set
  community_id = excluded.community_id,
  insight_type = excluded.insight_type,
  title = excluded.title,
  body = excluded.body,
  severity = excluded.severity,
  sort_order = excluded.sort_order;
