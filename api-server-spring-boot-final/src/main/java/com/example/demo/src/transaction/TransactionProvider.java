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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional(readOnly = true)
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

    //transaction 상세 페이지 검색 댓글 포함
    public GetTranRes getTransactionAndComment(int transactionId, Pageable pageable) throws BaseException {
        try{
            GetTranRes getTranRes = transactionDao.getTransactionAndComment(transactionId, pageable);
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

    //페이징 처리 없이 transaction 전체 조회
    //transaction 전체 조회
    public List<GetSearchTranRes> getAllTransactions(int userIdx) throws BaseException {
        try{
            List<GetSearchTranRes> getSearchTranResList = transactionDao.getAllTransactions(userIdx);
            return getSearchTranResList;
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //주소로 검색
    public Page<GetSearchTranRes> getSearchAddress(String searchQuery, Pageable pageable) throws BaseException {
        try {
            Page<GetSearchTranRes> getSearchTranResList = transactionDao.getSearchAddress(searchQuery,pageable);
            return getSearchTranResList;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //카테고리로 검색
    public Page<GetSearchTranRes> getSearchCategory(String searchQuery, Pageable pageable) throws BaseException {
        try {
            Page<GetSearchTranRes> getSearchTranResList = transactionDao.getSearchCategory(searchQuery,pageable);
            return getSearchTranResList;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //title,content로 검색
    public Page<GetSearchTranRes> getSearchTitle(String searchQuery, Pageable pageable) throws BaseException {
        try {
            Page<GetSearchTranRes> getSearchTranResList = transactionDao.getSearchTitle(searchQuery,pageable);
            return getSearchTranResList;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getUserIdx(int transactionId) throws BaseException {
        try{
            int userId = transactionDao.getUserIdx(transactionId);
            return userId;
        } catch (Exception exception) {
        exception.printStackTrace();
        throw new BaseException(DATABASE_ERROR);
    }
    }

    public int getCommentUserId(int commentId) throws BaseException {
        try{
            int userId = transactionDao.getCommentUserIdx(commentId);
            return userId;
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getCommentTransactionId(int commentId) throws BaseException {
        try{
            int commentTransactionId = transactionDao.getCommentTransactionId(commentId);
            return commentTransactionId;
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getCommentStatus(int commentId) throws BaseException {
        try{
            String commentStatus = transactionDao.getCommentStatus(commentId);
            return commentStatus;
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int likeTransactionCount(int transactionId, int userIdxByJwt) throws BaseException {
        try{
            int likeTransactionCount = transactionDao.likeTransactionCount(transactionId,userIdxByJwt);
            return likeTransactionCount;
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getlikeTransactionStatus(int transactionId, int userIdxByJwt) throws BaseException {
        try{
            String result = transactionDao.getLikeTransactionStatus(transactionId,userIdxByJwt);
            return result;
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int likeTransactionTotalCount(int transactionId) throws BaseException {
        try{
            int likeTotalCount = transactionDao.likeTransactionCount(transactionId);
            return likeTotalCount;
        } catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
