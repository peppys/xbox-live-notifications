package com.github.peppys.xboxlivenotifications.dto.task;

import com.github.peppys.xboxlivenotifications.entities.XboxLivePresence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Accessors(chain = true)
public class SyncXboxLivePresenceResponse {
    private List<XboxLivePresence> presences;
}
