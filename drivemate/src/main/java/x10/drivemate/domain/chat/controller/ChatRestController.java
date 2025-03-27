package x10.drivemate.domain.chat.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import x10.drivemate.common.response.ApiResponse;
import x10.drivemate.common.status.SuccessStatus;
import x10.drivemate.domain.chat.dto.ChatRequestDto;
import x10.drivemate.domain.chat.dto.ChatResponseDto;
import x10.drivemate.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import x10.drivemate.global.security.CustomUserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatRestController {
    private final ChatService chatService;

    @PostMapping("")
    public ResponseEntity<ApiResponse> saveChat(
            @RequestBody ChatRequestDto.ChatLogDto request)
    {
        ChatResponseDto.ChatLogResultDto response = chatService.saveChatLog(request);
        return ApiResponse.onSuccess(SuccessStatus._OK, response);
    }

    @PostMapping("/summary")
    public ResponseEntity<ApiResponse> saveChatSummary(
            @RequestBody ChatRequestDto.ChatSummaryDto request)
    {
        chatService.saveChatSummary(request);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ApiResponse> getChatinfo(
            @PathVariable Long chatId,
            @AuthenticationPrincipal CustomUserPrincipal userPrincipal
            ) {
        ChatResponseDto.ChatResultDto response = chatService.getChat(chatId, userPrincipal);
        return ApiResponse.onSuccess(SuccessStatus._OK, response);
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<ApiResponse> deleteChat(
            @PathVariable Long chatId,
            @AuthenticationPrincipal CustomUserPrincipal userPrincipal
    ) {
        chatService.deleteChat(chatId, userPrincipal);
        return ApiResponse.onSuccess(SuccessStatus._DELETED);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getChats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam int year,
            @AuthenticationPrincipal CustomUserPrincipal userPrincipal
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        return chatService.getChatList(pageable, year, userPrincipal);
    }
}
