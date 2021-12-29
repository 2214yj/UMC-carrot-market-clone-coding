package com.example.demo.src.transaction;

import com.example.demo.config.BaseResponse;
import com.example.demo.src.transaction.model.GetSearchTranRes;
import com.example.demo.src.transaction.model.GetTranRes;
import com.example.demo.src.transaction.model.PostTranReq;
import com.example.demo.src.transaction.model.PostTranRes;
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
}
