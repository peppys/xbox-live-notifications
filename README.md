## xbox-live-notifications (work-in-progress)
This fun little service texts me whenever the homies hop on Xbox Live.

Xbox Live doesn't offer any webhooks or data change feeds we can integrate with, so I built something simple myself.

### Architecture
#####[Cloud Scheduler](https://cloud.google.com/scheduler)
- Hits endpoint every 5 minutes: `/tasks/sync-xbox-live-presence-async`

#####Endpoint handler for Cloud Scheduler: `/tasks/sync-xbox-live-presence-async`
- Enqueues job to sync Xbox Live presence data for all of my friends via [Cloud Tasks](https://cloud.google.com/tasks)
    - Queue items are processed via endpoint: `/tasks/sync-xbox-live-presence`

#####Endpoint handler for Cloud Tasks: `/tasks/sync-xbox-live-presence`
- Retrieves Xbox Live friend data, including presence status via https://peoplehub.xboxlive.com/users/me/people/social
- Upserts presence records to the DB
- If any presence status changes are detected, publish presence ID and new status via [Pub/Sub](https://cloud.google.com/pubsub)
    - Topic has a push subscription handled via endpoint: `/subscriptions/xbox-live-status-changed`

#####Endpoint handler for Pub/Sub presence changed subscription: `/subscriptions/xbox-live-status-changed`
- Filter out any presence changes that are not Offline -> Online
- Send notification text message via [Twilio](https://www.twilio.com/)