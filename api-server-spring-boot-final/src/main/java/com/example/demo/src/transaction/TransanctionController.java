package com.example.demo.src.transaction;

import com.example.demo.config.BaseResponse;
import com.example.demo.src.transaction.model.*;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.config.BaseException;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/transaction")
public class TransanctionController {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    @Autowired
    private final TransactionProvider transactionProvider;
    @Autowired
    private final TransactionService transactionService;
    @Autowired
    private final JwtService jwtService;

    //transaction 생성
    @ResponseBody
    @PostMapping("/new-transaction")
    public BaseResponse<PostTranRes> createTransaction(@RequestBody PostTranReq postTranReq){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            PostTranRes postTranRes = transactionService.createTransaction(postTranReq,userIdxByJwt);
            return new BaseResponse<>(postTranRes);
        }catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }

    //상세 페이지 조회
    @ResponseBody
    @GetMapping("search/{transactionId}")
    public BaseResponse<GetTranRes> getTransaction(@PathVariable("transactionId") int transactionId){
        try {
            GetTranRes getTranRes = transactionProvider.getTransactionDetail(transactionId);
            return new BaseResponse<>(getTranRes);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //상세 페이지 조회 댓글 포함
    @ResponseBody
    @GetMapping("search/comment/{transactionId}")
    public BaseResponse<GetTranRes> getTransactionAndComment(@PathVariable("transactionId") int transactionId){
        try {
            Pageable pageable;
            pageable = PageRequest.of( 0,5,Sort.by("created_At").ascending());
            GetTranRes getTranRes = transactionProvider.getTransactionAndComment(transactionId,pageable);
            return new BaseResponse<>(getTranRes);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //댓글 생성
    @ResponseBody
    @PostMapping("search/comment/{transactionId}")
    public BaseResponse<GetTranRes> createComment(@PathVariable("transactionId") int transactionId, @RequestBody PostCommentReq postCommentReq){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            Pageable pageable;
            pageable = PageRequest.of(0,5,Sort.by("created_At").ascending());
            GetTranRes getTranRes = transactionService.createComment(userIdxByJwt,transactionId,postCommentReq,pageable);
            return new BaseResponse<>(getTranRes);
        } catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //댓글 수정
    @ResponseBody
    @PatchMapping("search/comment/{transactionId}")
    public BaseResponse<GetTranRes> modifyComment(@PathVariable("transactionId") int transactionId, @RequestBody Comment comment){
        try{
            int userIdByJwt = jwtService.getUserIdx();
            int commentId = comment.getId();
            //해당 comment의 userId와 로그인한 userId가 동일한지 확인
            int userId = transactionProvider.getCommentUserId(commentId);
            if (userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //해당 comment의 transactionId와 현재 transactionId가 일치하는지 확인
            int commentTransactionId = transactionProvider.getCommentTransactionId(commentId);
            if(commentTransactionId != transactionId){
                return new BaseResponse<>(MODIFY_FAIL_COMMENT);
            }
            Pageable pageable;
            pageable = PageRequest.of(0,5,Sort.by("created_At").ascending());
            GetTranRes getTranRes = transactionService.modifyComment(commentId,transactionId,comment.getContent(),pageable);
            return new BaseResponse<>(getTranRes);
        } catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //댓글 삭제
    @ResponseBody
    @DeleteMapping("search/comment/{transactionId}")
    public BaseResponse<GetTranRes> deleteComment(@PathVariable("transactionId") int transactionId,
                                                  @RequestParam(value = "id",defaultValue = "0") int commentId){
        try{
            //전달받은 commentId 값이 없는 경우
            if(commentId == 0){
                return new BaseResponse<>(DELETE_FAIL_COMMENT);
            }

            int userIdByJwt = jwtService.getUserIdx();
            //해당 comment의 userId와 로그인한 userId가 동일한지 확인
            int userId = transactionProvider.getCommentUserId(commentId);
            if (userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //해당 comment의 transactionId와 현재 transactionId가 일치하는지 확인
            int commentTransactionId = transactionProvider.getCommentTransactionId(commentId);
            if(commentTransactionId != transactionId){
                return new BaseResponse<>(MODIFY_FAIL_COMMENT);
            }

            //해당 comment가 이미 삭제된 comment인지 확인

            Pageable pageable;
            pageable = PageRequest.of(0,5,Sort.by("created_At").ascending());
            GetTranRes getTranRes = transactionService.deleteComment(commentId,transactionId,pageable);
            return new BaseResponse<>(getTranRes);
        }catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }


    //전체 검색
    @ResponseBody
    @GetMapping(value = {"/", "/{page}"})
    public BaseResponse<Page<GetSearchTranRes>> getAllTransactions(@PathVariable("page") Optional<Integer> page){
        try{
            Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,5);
            Page<GetSearchTranRes> getSearchTranResList = transactionProvider.getAllTransactions(pageable);
            return new BaseResponse<>(getSearchTranResList);
        }catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //검색
    @ResponseBody
    @GetMapping("/search")
    public BaseResponse<Page<GetSearchTranRes>> getTransactions(@RequestParam(value = "searchType")int searchType,
                                                                @RequestParam(value = "searchQuery", required = false, defaultValue = "")String searchQuery,
                                                                @RequestParam(value = "sortType",required = false, defaultValue = "transaction_id")String sortType,
                                                                @RequestParam(value = "sort",required = false, defaultValue = "asc")String sort
                                                                ){
        try{
            if(sortType.equals("id")){
                sortType = "transaction_id";
            }else if(sortType.equals("date")){
                sortType = "created_at";
            }
            Pageable pageable;
            if(sort.equals("desc"))
                pageable = PageRequest.of( 0,5,Sort.by(sortType).descending());
            else
                pageable = PageRequest.of( 0,5,Sort.by(sortType).ascending());
            Page<GetSearchTranRes> getSearchTranResList;

            switch(searchType){
                case 1:
                    //주소 검색
                    getSearchTranResList = transactionProvider.getSearchAddress(searchQuery,pageable);
                    break;
                case 2:
                    //카테고리 검색
                    getSearchTranResList = transactionProvider.getSearchCategory(searchQuery,pageable);
                    break;
                case 3:
                    //제목 검색
                    getSearchTranResList = transactionProvider.getSearchTitle(searchQuery,pageable);
                    break;
                default:
                    getSearchTranResList = null;
            }
            return new BaseResponse<>(getSearchTranResList);
        }catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //판매 상태 수정
    @ResponseBody
    @PatchMapping("/{transactionId}")
    public BaseResponse<String> modifySellStatus(@PathVariable("transactionId") int transactionId, @RequestBody PatchTranReq patchTranReq){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            int userIdx = transactionProvider.getUserIdx(transactionId);
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            transactionService.modifySellStatus(transactionId,patchTranReq);
            String result = "판매 상태가 수정되었습니다.";
            return new BaseResponse<>(result);
        }catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //전체 정보 수정
    @ResponseBody
    @PutMapping("/{transactionId}")
    public BaseResponse<String> modifyTransaction(@PathVariable("transactionId") int transactionId, @RequestBody PutTranReq putTranReq){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            int userIdx = transactionProvider.getUserIdx(transactionId);
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            transactionService.modifyTransaction(transactionId,putTranReq);
            String result = "판매 정보가 수정되었습니다.";
            return new BaseResponse<>(result);
        }catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //삭제
    @ResponseBody
    @DeleteMapping("/{transactionId}")
    public BaseResponse<String> deleteTransaction(@PathVariable("transactionId") int transactionId){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            int userIdx = transactionProvider.getUserIdx(transactionId);
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            transactionService.deleteTransaction(transactionId);
            String result = transactionId + "번 판매 정보가 삭제되었습니다.";
            return new BaseResponse<>(result);
        }catch(BaseException exception){
        exception.printStackTrace();
        return new BaseResponse<>(exception.getStatus());
        }
    }
}
