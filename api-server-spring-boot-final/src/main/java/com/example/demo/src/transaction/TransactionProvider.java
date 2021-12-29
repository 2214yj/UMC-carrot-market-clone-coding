package com.example.demo.src.transaction;

import com.example.demo.config.BaseException;
import com.example.demo.src.transaction.model.GetSearchTranRes;
import com.example.demo.src.transaction.model.GetTranRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class TransactionProvider {
    private final TransactionDao transactionDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    //transaction 상세 페이지 검색
    public GetTranRes getTransactionDetail(int transactionId) throws BaseException {
        try{
            GetTranRes getTranRes = transactionDao.getTransactionDetail(transactionId);
            return getTranRes;
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //transaction 전체 조회
    public Page<GetSearchTranRes> getAllTransactions(Pageable pageable) throws BaseException {
        try{
            Page<GetSearchTranRes> getSearchTranResList = transactionDao.getAllTransactions(pageable);
            return getSearchTranResList;
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
