package com.sjw.doran.authsimpleservice.client;

import com.sjw.doran.authsimpleservice.vo.MemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-service")
public interface MemberServiceClient {

    @PostMapping("/join")
    MemberResponse joinMember(@RequestHeader("userUuid") String userUuid, @RequestParam("username") String username);

    @DeleteMapping("/delete")
    MemberResponse deleteMember(@RequestHeader("userUuid") String userUuid);
}
