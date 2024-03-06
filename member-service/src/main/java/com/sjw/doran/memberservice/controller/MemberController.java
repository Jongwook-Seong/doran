package com.sjw.doran.memberservice.controller;

import com.sjw.doran.memberservice.entity.MemberEntity;
import com.sjw.doran.memberservice.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Tag(name = "Member Service", description = "Member Service Swagger API")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    @Operation(summary = "멤버 리스트 조회", description = "전체 멤버 목록을 조회합니다.")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MemberEntity.class)))
                    })
    )
    public List<MemberEntity> members() {
        return memberService.findMembers();
    }

    @PostMapping("/save")
    @Operation(summary = "신규 멤버 저장", description = "새로운 멤버를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = MemberEntity.class))}),
            @ApiResponse(responseCode = "500", description = "Fail")
    })
    public void saveMember(@RequestBody MemberEntity member) {
        memberService.saveMember(member);
    }
}