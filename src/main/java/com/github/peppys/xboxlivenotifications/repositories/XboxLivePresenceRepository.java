package com.github.peppys.xboxlivenotifications.repositories;

import com.github.peppys.xboxlivenotifications.entities.XboxLivePresence;
import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

public interface XboxLivePresenceRepository extends FirestoreReactiveRepository<XboxLivePresence>, XboxLivePresenceRepositoryCustom {
}
