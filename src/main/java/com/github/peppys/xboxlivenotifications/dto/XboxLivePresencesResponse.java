package com.github.peppys.xboxlivenotifications.dto;

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
public class XboxLivePresencesResponse {
    private List<XboxLivePresence> presences;
}
