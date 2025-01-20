package io.redispro.redisexec.controller;

import io.redispro.redisexec.dto.ApiResponse;
import io.redispro.redisexec.entity.Member;
import io.redispro.redisexec.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Callable;

@RestController
@RequestMapping(value = "/api/members", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 모든 멤버 조회
     *
     * @return 모든 Member 목록
     */
//    @Operation(summary = "멤버 조회 API", tags = {"Member API"})
//    @GetMapping
//    public Callable<?> getAllMembers(
//            @RequestParam(value = "includeOrders", defaultValue = "false") boolean includeOrders) {
//
//        return () -> {
//            // 사용자 삭제 로직
//            ApiResponse result = new ApiResponse();
//            result.setResult(memberService.getMembersWithOrders(includeOrders)); // 삭제된 결과 반환
//
//            return result;
//        }; // 성공적으로 삭제된 경우 200 OK 반환
//    }

    @Operation(summary = "멤버 조회 API", tags = {"Member API"})
    @GetMapping
    public ResponseEntity<?> getAllMembers(
            @RequestParam(value = "includeOrders", defaultValue = "false") boolean includeOrders) {

        // 사용자 삭제 로직
        ApiResponse result = new ApiResponse();
        result.setResult(memberService.getMembersWithOrders(includeOrders)); // 삭제된 결과 반환

        return ResponseEntity.ok(result);
    }
}