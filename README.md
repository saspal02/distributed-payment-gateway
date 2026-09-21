# PayGrid — distributed payment gateway

Project identity: PayGrid (`paygrid` / `pay-grid`). Java root package `com.saswat.paygrid`, Maven groupId `com.saswat.paygrid`, Kubernetes namespace `paygrid-core`.

Service boundaries, API contracts, and runtime behavior are unchanged from the prior identity, except the webhook signature header default which is now `X-PayGrid-Signature`.
