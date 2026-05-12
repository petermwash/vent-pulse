create or replace function public.set_updated_at()
returns trigger
language plpgsql
set search_path = public, pg_temp
as $$
begin
  new.updated_at = now();
  return new;
end;
$$;

create or replace function public.current_profile_id()
returns uuid
language sql
stable
set search_path = public
as $$
  select id
  from public.anonymous_profiles
  where user_id = (select auth.uid())
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
      and user_id = (select auth.uid())
  )
$$;

drop policy "Users can create their anonymous profile" on public.anonymous_profiles;
drop policy "Users can update their anonymous profile" on public.anonymous_profiles;

create policy "Users can create their anonymous profile"
  on public.anonymous_profiles for insert
  to authenticated
  with check (user_id = (select auth.uid()));

create policy "Users can update their anonymous profile"
  on public.anonymous_profiles for update
  to authenticated
  using (user_id = (select auth.uid()))
  with check (user_id = (select auth.uid()));

create index chat_messages_profile_id_idx on public.chat_messages(profile_id);
create index reports_reporter_profile_id_idx on public.reports(reporter_profile_id);
create index reports_reported_profile_id_idx on public.reports(reported_profile_id);
create index vent_reactions_profile_id_idx on public.vent_reactions(profile_id);
