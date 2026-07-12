# TODO - Fix Render deployment failures

## Step 1
- [x] Update `src/main/resources/application.properties` to bind Render port using `server.port=${PORT:8080}`.
- [x] Update DB config to use env vars (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`) instead of hardcoded credentials.

## Step 2
- [ ] Redeploy to Render and verify app binds to a port (no more “Port scan timeout” message).

## Step 3
- [ ] On Render, set environment variables: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` (and ensure DB allows Render IP/network).

## Step 4
- [ ] If still failing: inspect Render logs for `SocketException: Network unreachable` and adjust Supabase network/VPC settings (or switch to Supabase connection string that works from Render).

