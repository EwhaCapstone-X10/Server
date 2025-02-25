package x10.drivemate.domain.stretching.service;

import x10.drivemate.common.response.ApiResponse;
import x10.drivemate.domain.stretching.dto.StretchingResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StretchingService {
    ResponseEntity<ApiResponse> getStretchingList(Pageable pageable);
}
