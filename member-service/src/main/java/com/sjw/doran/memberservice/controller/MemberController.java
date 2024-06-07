package com.sjw.doran.memberservice.controller;

import com.sjw.doran.memberservice.dto.MemberDto;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.service.BasketService;
import com.sjw.doran.memberservice.service.MemberService;
import com.sjw.doran.memberservice.util.MessageUtil;
import com.sjw.doran.memberservice.vo.response.MemberOrderResponse;
import com.sjw.doran.memberservice.vo.response.MemberResponse;
import com.sjw.doran.memberservice.vo.response.order.DeliveryTrackingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Tag(name = "Member Service", description = "Member Service Swagger API")
public class MemberController {

    private final MemberService memberService;
    private final BasketService basketService;
    private final MessageUtil messageUtil;

    @GetMapping("/members")
    @Operation(summary = "멤버 리스트 조회", description = "전체 멤버 목록을 조회합니다.")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MemberDto.class)))
                    })
    )
    public ResponseEntity<List<MemberDto>> members() {
        List<MemberDto> members = memberService.findMembers();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @PostMapping("/join")
    @Operation(summary = "신규 회원 저장", description = "새로 가입한 멤버(회원)를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = ResponseEntity.class))}),
            @ApiResponse(responseCode = "500", description = "Fail")
    })
    public ResponseEntity<MemberResponse> joinMember(@RequestHeader("userUuid") String userUuid, @RequestParam("username") String username) {
        Member member = new Member(userUuid, username);
        memberService.saveMember(member);
        return ResponseEntity.ok(MemberResponse.getInstance(userUuid, messageUtil.getMemberCreateMessage()));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "회원 탈퇴", description = "탈퇴할 멤버(회원)의 데이터를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = ResponseEntity.class))}),
            @ApiResponse(responseCode = "500", description = "Fail")
    })
    public ResponseEntity<MemberResponse> deleteMember(@RequestHeader("userUuid") String userUuid) {
        memberService.deleteMember(userUuid);
        return ResponseEntity.ok(MemberResponse.getInstance(userUuid, messageUtil.getMemberDeleteMessage()));
    }

    /** 주문 서비스 호출 API **/
    @GetMapping("/order/list")
    public ResponseEntity<MemberOrderResponse> getMemberOrderList(@RequestHeader("userUuid") String userUuid) throws InterruptedException {
        MemberOrderResponse memberOrderListResponse = memberService.findMemberOrderList(userUuid);
        return new ResponseEntity<>(memberOrderListResponse, HttpStatus.OK);
    }

    @GetMapping("/order/detail")
    public ResponseEntity<MemberOrderResponse> getMemberOrderDetail(@RequestHeader("userUuid") String userUuid, @RequestParam("orderUuid") String orderUuid) throws InterruptedException {
        MemberOrderResponse memberOrderDetailResponse = memberService.findMemberOrderDetail(userUuid, orderUuid);
        return new ResponseEntity<>(memberOrderDetailResponse, HttpStatus.OK);
    }

    @GetMapping("/delivery/tracking")
    public ResponseEntity<MemberOrderResponse> inquireDeliveryTracking(@RequestHeader("userUuid") String userUuid, @RequestParam("orderUuid") String orderUuid) throws InterruptedException {
        MemberOrderResponse memberOrderDTResponse = memberService.findMemberOrderDeliveryTracking(userUuid, orderUuid);
        return new ResponseEntity<>(memberOrderDTResponse, HttpStatus.OK);
    }
}