package io.redispro.redisexec.controller;

import io.redispro.redisexec.dto.ApiResponseDTO;
import io.redispro.redisexec.dto.MemberDTO;
import io.redispro.redisexec.dto.MemberUpdateRequest;
import io.redispro.redisexec.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;

@RestController
@RequestMapping(value = "/api/members", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 멤버를 등록하는 API
     *
     * @param memberCreateRequest 생성할 멤버의 정보 (RequestBody로 받음)
     * @return 등록된 멤버의 DTO
     */
    @Operation(summary = "멤버 등록 API", tags = {"Member API"})
    @PostMapping
    public Callable<?> createMember(
            @RequestBody MemberUpdateRequest memberCreateRequest) {
        return () -> {
            ApiResponseDTO response = new ApiResponseDTO();

            // 응답 객체에 등록된 멤버 정보 설정
            response.setResult(memberService.createMember(memberCreateRequest));

            return response;
        };
    }

    /**
     * 모든 멤버 조회
     *
     * @return 모든 Member 목록
     */
    @Operation(summary = "멤버 조회 API", tags = {"Member API"})
    @GetMapping
    public Callable<?> getAllMembers(
            @RequestParam(value = "includeOrders", defaultValue = "false") boolean includeOrders,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort) {

        // Pageable을 생성하는 메서드 호출
        Pageable pageable = createPageable(page, size, sort);

        return () -> {
            // 페이지와 정렬 정보 파싱

            // ApiResponseDTO 객체 생성 및 데이터 반환
            ApiResponseDTO result = new ApiResponseDTO();
            result.setResult(memberService.getMembersWithOrders(includeOrders, pageable));

            return result;
        };
    }

    /**
     * 페이지와 정렬 정보를 기반으로 Pageable 객체를 생성하는 메서드
     */
    private Pageable createPageable(int page, int size, String sort) {
        // sort 값을 ',' 기준으로 분리
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        String sortDirection = sortParams.length > 1 ? sortParams[1] : "asc"; // 방향이 없으면 기본값 'asc'로 설정

        // 정렬 방향 결정 (기본적으로 오름차순, desc면 내림차순으로 설정)
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Pageable 객체 생성 후 반환
        return PageRequest.of(page, size, Sort.by(direction, sortField)); // 정렬 필드와 방향 설정
    }

    /**
     * id로 멤버 조회
     *
     * @param memberId      멤버의 ID
     * @param includeOrders 주문 정보를 포함할지 여부
     * @return 조회된 멤버 DTO
     */
    @Operation(summary = "id로 멤버 조회 API", tags = {"Member API"})
    @GetMapping("/{memberId}")
    public Callable<?> getMemberById(
            @PathVariable Long memberId,  // URL 경로에서 memberId를 받아옴
            @RequestParam(value = "includeOrders", defaultValue = "false") boolean includeOrders) {

        return () -> {
            // 결과 객체 생성
            ApiResponseDTO result = new ApiResponseDTO();

            result.setResult(memberService.getMemberById(memberId, includeOrders));  // 조회된 멤버 DTO를 반환

            return result;
        }; // 성공적으로 조회된 경우 200 OK 반환
    }

    /**
     * 멤버 정보 수정
     *
     * @param memberId 수정할 멤버의 ID
     * @return 수정된 멤버 DTO
     */
    @Operation(summary = "멤버 정보 수정 API", tags = {"Member API"})
    @PutMapping("/{memberId}")
    public Callable<?> updateMember(
            @PathVariable Long memberId,
            @RequestBody MemberUpdateRequest updateRequest) {

        return () -> {
            ApiResponseDTO result = new ApiResponseDTO();

            if (memberService.updateMember(updateRequest)) {
                result.setMessage("Member updated successfully");  // 수정된 멤버 반환
            } else {
                result.setMessage("Member not found or update failed");  // 멤버가 없거나 수정 실패
            }

            return result;
        };
    }

    /**
     * 멤버 삭제
     *
     * @param memberId 삭제할 멤버의 ID
     * @return 삭제 성공 여부
     */
    @Operation(summary = "멤버 삭제 API", tags = {"Member API"})
    @DeleteMapping("/{memberId}")
    public Callable<?> deleteMember(@PathVariable Long memberId) {

        return () -> {
            ApiResponseDTO result = new ApiResponseDTO();
            boolean isDeleted = memberService.deleteMember(memberId);

            if (isDeleted) {
                result.setMessage("Member deleted successfully");  // 삭제 성공
            } else {
                result.setMessage("Member not found or delete failed");  // 멤버가 없거나 삭제 실패
            }

            return result;
        };
    }
}