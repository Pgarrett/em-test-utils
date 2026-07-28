# EvoMaster BlackBox Mode — Real-World API Targets

Five real-world, production-grade APIs with sandbox/test-mode credentials (not synthetic testing playgrounds).

---

## 1. Stripe API

**EvoMaster run command:**
```bash
java -jar evomaster.jar --blackBox true \
  --bbSwaggerUrl https://raw.githubusercontent.com/stripe/openapi/master/openapi/spec3.json \
  --outputFormat JAVA_JUNIT_4 \
  --maxTime 120s \
  --ratePerMinute 30 \
  --outputFolder /run/datad/evoMaster/em-test-utils/naming-study-v2/initial-test/stripe \
  --header0 "Authorization: Bearer <TEST_SECRET_KEY>"
```

**Environment pre-requisites:**
1. Create a free account at https://dashboard.stripe.com/register
2. Stripe accounts start in **test mode** by default — no live payment processing risk, all writes create fake objects.

**EvoMaster pre-requisites:**
No token exchange needed — just a static test-mode API key.
- Go to Dashboard → Developers → API keys
- Copy the **Secret key** starting with `sk_test_...`
- Pass it directly as a Bearer token (shown above)
- ⚠️ Never use a `sk_live_...` key here — this spec has both endpoints in one file and there's no separate sandbox host, so the key alone determines test vs. live.

---

## 2. Twilio API (Programmable Messaging/Voice, v2010)

**EvoMaster run command:**
```bash
java -jar evomaster.jar --blackBox true \
  --bbSwaggerUrl https://raw.githubusercontent.com/twilio/twilio-oai/main/spec/json/twilio_api_v2010.json \
  --outputFormat JAVA_JUNIT_4 \
  --maxTime 120s \
  --ratePerMinute 20 \
  --outputFolder /run/datad/evoMaster/em-test-utils/naming-study-v2/initial-test/twilio \
  --header0 "Authorization: Basic <BASE64(ACCOUNT_SID:AUTH_TOKEN)>"
```

**Environment pre-requisites:**
1. Sign up for a free trial at https://www.twilio.com/try-twilio
2. Trial accounts get free credit but can only send SMS/make calls to **phone numbers you've manually verified** in the console — this naturally limits blast radius from fuzzed inputs.
3. ⚠️ Unlike Stripe, this is a real messaging/telephony API — fuzzed POST requests to `/Messages` or `/Calls` could attempt to actually send SMS or place calls (restricted to verified numbers on trial). Consider excluding those specific endpoints if you want a purely read-safe run.

**EvoMaster pre-requisites:**
Basic auth, no OAuth flow.
- Find your **Account SID** and **Auth Token** on the Twilio Console dashboard homepage
- Base64-encode `ACCOUNT_SID:AUTH_TOKEN` and pass as shown

---

## 3. PayPal Orders API v2 (Sandbox)

**EvoMaster run command:**
```bash
java -jar evomaster.jar --blackBox true \
  --bbSwaggerUrl https://raw.githubusercontent.com/paypal/paypal-rest-api-specifications/main/openapi/checkout_orders_v2.json \
  --bbTargetUrl https://api-m.sandbox.paypal.com \
  --outputFormat JAVA_JUNIT_4 \
  --maxTime 120s \
  --ratePerMinute 30 \
  --outputFolder /run/datad/evoMaster/em-test-utils/naming-study-v2/initial-test/paypal \
  --header0 "Authorization: Bearer <ACCESS_TOKEN>"
```

**Environment pre-requisites:**
1. Create a free account at https://developer.paypal.com
2. In the Developer Dashboard, create a sandbox "App" (Apps & Credentials → Create App)
3. Note the sandbox **Client ID** and **Secret**

**EvoMaster pre-requisites:**
OAuth2 client-credentials token, refreshed periodically:
```bash
curl https://api-m.sandbox.paypal.com/v1/oauth2/token \
  -u "<CLIENT_ID>:<CLIENT_SECRET>" \
  -H "Accept: application/json" \
  -d "grant_type=client_credentials"
```
Token expires in ~9 hours — re-fetch if a long run outlives it.

---

## 4. Spotify Web API

**EvoMaster run command:**
```bash
java -jar evomaster.jar --blackBox true \
  --bbSwaggerUrl https://developer.spotify.com/_data/documentation/web-api/reference/open-api-schema.yml \
  --outputFormat JAVA_JUNIT_4 \
  --maxTime 120s \
  --ratePerMinute 30 \
  --outputFolder /run/datad/evoMaster/em-test-utils/naming-study-v2/initial-test/spotify \
  --header0 "Authorization: Bearer <ACCESS_TOKEN>"
```

**Environment pre-requisites:**
1. Create a free account at https://developer.spotify.com/dashboard
2. Register an "App" to get a Client ID and Client Secret

**EvoMaster pre-requisites:**
OAuth2 Client Credentials token (this flow only unlocks public catalog data — albums, artists, tracks, search — not user-specific playlists/playback, which need the Authorization Code flow instead):
```bash
curl -X POST https://accounts.spotify.com/api/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials&client_id=<CLIENT_ID>&client_secret=<CLIENT_SECRET>"
```
Token expires in 1 hour. Since Client Credentials tokens can't authorize the user-data endpoints in the full spec, expect a lot of 401s on those paths — worth telling EvoMaster to focus on catalog endpoints, or trimming the spec down first.

---

## 5. Google Books API

**EvoMaster run command:**
```bash
java -jar evomaster.jar --blackBox true \
  --bbSwaggerUrl /path/to/local/modified-books-openapi.yaml \
  --outputFormat JAVA_JUNIT_4 \
  --maxTime 60s \
  --outputFolder /run/datad/evoMaster/em-test-utils/naming-study-v2/initial-test/google \
  --ratePerMinute 30
```

**Environment pre-requisites:**
1. Create a project at https://console.cloud.google.com
2. Enable the "Books API" under APIs & Services → Library
3. Create an API key under APIs & Services → Credentials

**EvoMaster pre-requisites:**
Just an API key — no OAuth. But it's passed as a query parameter (`?key=...`), which EvoMaster's blackbox mode won't auto-inject from the spec's security scheme. Practical fix:
1. Download the spec: `https://raw.githubusercontent.com/APIs-guru/openapi-directory/main/APIs/googleapis.com/books/v1/openapi.yaml`
2. Edit it locally to hardcode your key as the `default` value on the `key` query parameter
3. Point `--bbSwaggerUrl` at your local copy instead of the remote one

---

## Cross-Cutting Advice

- Start every one of these with a short `--maxTime` (e.g. 60s) first to confirm auth actually works before committing to a long run — a broken token gives you a test suite full of useless 401s.
- Twilio and PayPal involve real-world side effects even in sandbox/trial mode (messages, calls, sandbox transactions) — worth reviewing which endpoints EvoMaster is actually calling before scaling up `--maxTime`.
- Keep `--ratePerMinute` conservative (20–30) on all of these; real providers actively rate-limit and may flag fuzzing-like traffic patterns.