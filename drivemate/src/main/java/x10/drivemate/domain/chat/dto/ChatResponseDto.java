package x10.drivemate.domain.chat.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ChatResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatResultDto {
        private Long chatId;
        private LocalDateTime date;
        private String summary;
        private String keywords;
        private List<ChatRequestDto.ChatMessageDto> chatting;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatLogResultDto {
        private Long chatId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatListDto {
        private Long chatId;
        private LocalDateTime date;
        private String summary;
    }
}
