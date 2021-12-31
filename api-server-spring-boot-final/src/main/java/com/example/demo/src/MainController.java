package com.example.demo.src;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.transaction.TransactionProvider;
import com.example.demo.src.transaction.model.GetSearchTranRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {

    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.
    private final TransactionProvider transactionProvider;
    private final JwtService jwtService;

    @GetMapping(value = "/app/main")
    public BaseResponse<List<GetSearchTranRes>> main() {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            List<GetSearchTranRes> getSearchTranResList = transactionProvider.getAllTransactions(userIdxByJwt);
            return new BaseResponse<>(getSearchTranResList);
        }catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
