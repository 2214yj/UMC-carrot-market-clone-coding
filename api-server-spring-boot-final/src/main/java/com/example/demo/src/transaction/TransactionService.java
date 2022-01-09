package com.example.demo.src.transaction;

import com.example.demo.config.BaseException;
import com.example.demo.src.transaction.model.*;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.example.demo.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class TransactionService {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log 처리부분: Log를 기록하기 위해 필요한 함수입니다.

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final TransactionDao transactionDao;
    private final TransactionProvider transactionProvider;
    private final JwtService jwtService;

    @Transactional
    public PostTranRes createTransaction(PostTranReq postTranReq,int userId) throws BaseException{
        try{
            PostTranRes result = transactionDao.createTransaction(postTranReq,userId);
            return result;

        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void modifySellStatus(int transactionId, PatchTranReq patchTranReq) throws BaseException{
        try{
            int result = transactionDao.modifySellStatus(transactionId,patchTranReq);
            if (result == 0) { // result값이 0이면 과정이 실패한 것이므로 에러 메서지를 보냅니다.
                throw new BaseException(MODIFY_FAIL_SELLSTATUS);
            }
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void modifyTransaction(int transactionId, PutTranReq putTranReq) throws BaseException {
        try{
            int result = transactionDao.modifyTransaction(transactionId,putTranReq);
            if (result == 0) { // result값이 0이면 과정이 실패한 것이므로 에러 메서지를 보냅니다.
                throw new BaseException(MODIFY_FAIL_SELLSTATUS);
            }
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void deleteTransaction(int transactionId) throws BaseException {
        try{
            int result = transactionDao.deleteTransaction(transactionId);
            if (result == 0) { // result값이 0이면 과정이 실패한 것이므로 에러 메서지를 보냅니다.
                throw new BaseException(DELETE_FAIL_TRANSACTION);
            }
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public GetTranRes createComment(int userIdxByJwt, int transactionId, PostCommentReq postCommentReq,Pageable pageable) throws BaseException {
        try{
            GetTranRes getTranRes = transactionDao.createComment(userIdxByJwt,transactionId,postCommentReq,pageable);
            return getTranRes;
        }catch(Exception exception){
            exception.printStackTrace();;
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public GetTranRes modifyComment(int commentId, int transactionId, String content, Pageable pageable) throws BaseException {
        try{
            GetTranRes getTranRes = transactionDao.modifyComment(commentId,transactionId,content,pageable);
            return getTranRes;
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetTranRes deleteComment(int commentId, int transactionId, Pageable pageable) throws BaseException {
        try{
            GetTranRes getTranRes = transactionDao.deleteComment(commentId,transactionId,pageable);
            return getTranRes;
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
