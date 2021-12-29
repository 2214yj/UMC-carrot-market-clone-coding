package com.example.demo.src.transaction;

import com.example.demo.src.transaction.model.GetSearchTranRes;
import com.example.demo.src.transaction.model.GetTranRes;
import com.example.demo.src.transaction.model.PostTranReq;
import com.example.demo.src.transaction.model.PostTranRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //transaction 추가
    public PostTranRes createTransaction(PostTranReq postTranReq, int userId) {
        String createTranQuery = "insert into Transaction (title,content,item_name,price,category,user_id,rep_img,address,sell_status,created_At) VALUES (?,?,?,?,?,?,?,?,?,?)";
        Object[] createTranParams = new Object[]{postTranReq.getTitle(),postTranReq.getContent(),postTranReq.getItemName(),postTranReq.getPrice(),postTranReq.getCategory(), userId,postTranReq.getRepImg(),postTranReq.getAddress(), "SELL",LocalDateTime.now()};
        this.jdbcTemplate.update(createTranQuery, createTranParams);
        int lastTransactionId = this.jdbcTemplate.queryForObject("select last_insert_id()", int.class);
        if(postTranReq.getImage()!=null) {
            for (String s : postTranReq.getImage()) {
                createTranQuery = "insert into Transaction_Image (url, transaction_id) VALUES (?,?)";
                createTranParams = new Object[]{s, lastTransactionId};
                this.jdbcTemplate.update(createTranQuery, createTranParams);
            }
        }
        return new PostTranRes("transaction을 생성했습니다.");
    }

    //transaction 상세 페이지 검색
    public GetTranRes getTransactionDetail(int transactionId){

        System.out.println("test3");
        String getDetailQuery = "select title,content,item_name,price,category,address,sell_status,created_at from Transaction where transaction_id = ?"; // 해당 userIdx를 만족하는 유저를 조회하는 쿼리문
        int getDetailParams = transactionId;
        GetTranRes getTranRes1 = this.jdbcTemplate.queryForObject(getDetailQuery,
                (rs, rowNum) -> new GetTranRes(
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("item_name"),
                        rs.getString("price"),
                        rs.getString("category"),
                        rs.getString("address"),
                        rs.getString("sell_status"),
                        rs.getString("created_at")
                ),
                getDetailParams);

        getDetailQuery = "select url from Transaction_Image where transaction_id = ?"; // 해당 userIdx를 만족하는 유저를 조회하는 쿼리문
        getDetailParams = transactionId;
        getTranRes1.setImage(this.jdbcTemplate.query(getDetailQuery,
                (rs, rowNum) -> new String(
                        rs.getString("url")
                ),
                getDetailParams));
        int userId = this.jdbcTemplate.queryForObject("select user_id from Transaction where transaction_id = ?",Integer.class, transactionId);
        getDetailQuery = "select nickname, image from User where user_idx = ?"; // 해당 userIdx를 만족하는 유저를 조회하는 쿼리문
        getDetailParams = userId;
        GetTranRes getTranRes2 = this.jdbcTemplate.queryForObject(getDetailQuery,
                (rs, rowNum) -> new GetTranRes(
                        rs.getString("nickname"),
                        rs.getString("image")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getDetailParams);
        getTranRes1.setNickname(getTranRes2.getNickname());
        getTranRes1.setAddress(getTranRes2.getAddress());
        getTranRes1.setUserImage(getTranRes2.getUserImage());
        return getTranRes1;
    }

    //전체 조회
    public Page<GetSearchTranRes> getAllTransactions(Pageable pageable){
        int count = jdbcTemplate.queryForObject("SELECT count(*) FROM Transaction", Integer.class);
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : Sort.Order.by("transaction_id");
        List<GetSearchTranRes> getSearchTranResList = jdbcTemplate.query("SELECT * FROM Transaction ORDER BY " + order.getProperty() + " "
                        + order.getDirection().name() + " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset(),
                (rs, rowNum) -> new GetSearchTranRes(
                        rs.getInt("transaction_id"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getString("category"),
                        rs.getString("rep_img"),
                        rs.getString("sell_status"),
                        rs.getString("created_at"),
                        rs.getString("address")));

        return new PageImpl<GetSearchTranRes>(getSearchTranResList, pageable, count);
    }

    public Page<GetSearchTranRes> getSearchAddress(String searchQuery, Pageable pageable) {
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : Sort.Order.by("transaction_id");
        Object[] getQueryParams = {"%"+searchQuery+"%","SELL"};
        List<GetSearchTranRes> getSearchTranResList = jdbcTemplate.query("SELECT * FROM Transaction where address like ? and sell_status = ? ORDER BY " + order.getProperty() + " "
                        + order.getDirection().name() + " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset(),
                (rs, rowNum) -> new GetSearchTranRes(
                        rs.getInt("transaction_id"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getString("category"),
                        rs.getString("rep_img"),
                        rs.getString("sell_status"),
                        rs.getString("created_at"),
                        rs.getString("address"))
                ,getQueryParams
        );

        int count =  jdbcTemplate.queryForObject("SELECT count(*) FROM Transaction where address like ? and sell_status = ?", Integer.class,getQueryParams);
        return new PageImpl<GetSearchTranRes>(getSearchTranResList, pageable, count);
    }

    public Page<GetSearchTranRes> getSearchCategory(String searchQuery, Pageable pageable) {
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : Sort.Order.by("transaction_id");
        Object[] getQueryParams = {"%"+searchQuery+"%","SELL"};
        List<GetSearchTranRes> getSearchTranResList = jdbcTemplate.query("SELECT * FROM Transaction where category like ? and sell_status = ? ORDER BY " + order.getProperty() + " "
                        + order.getDirection().name() + " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset(),
                (rs, rowNum) -> new GetSearchTranRes(
                        rs.getInt("transaction_id"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getString("category"),
                        rs.getString("rep_img"),
                        rs.getString("sell_status"),
                        rs.getString("created_at"),
                        rs.getString("address"))
                ,getQueryParams
        );

        int count =  jdbcTemplate.queryForObject("SELECT count(*) FROM Transaction where category like ? and sell_status = ?", Integer.class,getQueryParams);
        return new PageImpl<GetSearchTranRes>(getSearchTranResList, pageable, count);
    }

    public Page<GetSearchTranRes> getSearchTitle(String searchQuery, Pageable pageable) {
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : Sort.Order.by("transaction_id");
        Object[] getQueryParams = {"%"+searchQuery+"%","SELL"};
        List<GetSearchTranRes> getSearchTranResList = jdbcTemplate.query("SELECT * FROM Transaction where title like ? and sell_status = ? ORDER BY " + order.getProperty() + " "
                        + order.getDirection().name() + " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset(),
                (rs, rowNum) -> new GetSearchTranRes(
                        rs.getInt("transaction_id"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getString("category"),
                        rs.getString("rep_img"),
                        rs.getString("sell_status"),
                        rs.getString("created_at"),
                        rs.getString("address"))
                ,getQueryParams
        );

        int count =  jdbcTemplate.queryForObject("SELECT count(*) FROM Transaction where title like ? and sell_status = ?", Integer.class,getQueryParams);
        return new PageImpl<GetSearchTranRes>(getSearchTranResList, pageable, count);
    }

//    //판매 상태 수정
//    @ResponseBody
//    @PatchMapping()

}



