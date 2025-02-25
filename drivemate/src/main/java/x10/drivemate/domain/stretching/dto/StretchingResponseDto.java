package x10.drivemate.domain.stretching.dto;

import lombok.*;

import java.time.LocalDateTime;

public class StretchingResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StretchingInfodto {
        private Long stretchingId;
        private boolean isVideo;
        private String src;
        private String source;
        private String title;
        private String description;
    }
}
