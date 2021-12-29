package com.example.demo.src.mypage;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.mypage.model.PatchImageReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/my-page")
public class MyPageController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MyPageProvider myPageProvider;
    @Autowired
    private final MyPageService myPageService;
    @Autowired
    private final JwtService jwtService;

    public MyPageController(MyPageProvider myPageProvider,MyPageService myPageService, JwtService jwtService) {
        this.myPageProvider = myPageProvider;
        this.myPageService = myPageService;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!
    }

    //회원 프로필 조회 (회원 정보 + 거래 횟수)




    //회원 프로필 사진 등록, 수정
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> modifyUserProfile(@PathVariable("userIdx") int userIdx, @RequestBody PatchImageReq patchImageReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 프로필 사진 변경
            myPageService.modifyImage(patchImageReq);
            String result = "프로필 사진이 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }



}
