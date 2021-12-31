package com.example.demo.src.mypage;

import com.example.demo.config.BaseException;
import com.example.demo.src.mypage.model.GetMyPageRes;
import com.example.demo.src.transaction.model.GetSearchTranRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class MyPageProvider {

    private final MyPageDao myPageDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public GetMyPageRes getMyPage(int userIdxByJwt) throws BaseException {
        try{
            GetMyPageRes result = myPageDao.getMyPage(userIdxByJwt);
            return result;
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public Page<GetSearchTranRes> getMyTransactions(int userId,Pageable pageable) throws BaseException {
        try{
            Page<GetSearchTranRes> getSearchTranResList = myPageDao.getMyTransactions(userId,pageable);
            return getSearchTranResList;
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
