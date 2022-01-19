package com.example.demo.src.transaction;

import com.example.demo.src.transaction.model.*;
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
        String getDetailQuery = "select * from Transaction t " +
                "join user u on t.user_id = u.user_idx " +
                "where transaction_id = ?"; // 해당 userIdx를 만족하는 유저를 조회하는 쿼리문
        int getDetailParams = transactionId;
        GetTranRes getTranRes1 = this.jdbcTemplate.queryForObject(getDetailQuery,
                (rs, rowNum) -> new GetTranRes(
                        rs.getString("t.title"),
                        rs.getString("t.content"),
                        rs.getString("t.item_name"),
                        rs.getString("t.price"),
                        rs.getString("t.category"),
                        rs.getString("t.address"),
                        rs.getString("t.sell_status"),
                        rs.getString("t.created_at"),
                        rs.getString("u.nickname"),
                        rs.getString("u.image")
                ),
                getDetailParams);

        getDetailQuery = "select url from Transaction_Image where transaction_id = ?"; // 해당 userIdx를 만족하는 유저를 조회하는 쿼리문
        getDetailParams = transactionId;
        getTranRes1.setImage(this.jdbcTemplate.query(getDetailQuery,
                (rs, rowNum) -> new String(
                        rs.getString("url")
                ),
                getDetailParams));

        return getTranRes1;
    }

    //transaction 상세 페이지 검색 댓글 포함
    public GetTranRes getTransactionAndComment(int transactionId,Pageable pageable){
        //상세 페이지 조회
        GetTranRes getTranRes1 = getTransactionDetail(transactionId);
        //댓글 조회
        int count = jdbcTemplate.queryForObject("SELECT count(*) FROM COMMENT WHERE transaction_id = ?", Integer.class,transactionId);
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : Sort.Order.by("transaction_id");
        Object[] getTransactionAndCommentParams = new Object[]{transactionId,"A"};
        List<Comment> commentList = jdbcTemplate.query("SELECT c.comment_id,c.content,u.nickname,c.created_At FROM COMMENT c,USER u WHERE c.user_id = u.user_idx AND c.transaction_id = ? AND c.status = ? ORDER BY " + order.getProperty() + " "
                        + order.getDirection().name() + " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset(),
                (rs, rowNum) -> new Comment(
                        rs.getInt("comment_id"),
                        rs.getString("content"),
                        rs.getString("nickname"),
                        rs.getString("created_At")
                        ),getTransactionAndCommentParams);

        getTranRes1.setCommentPage(new PageImpl<Comment>(commentList,pageable,count));

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

    //페이징 없이 전체 조회
    public List<GetSearchTranRes> getAllTransactions(int userIdx){
        String address = jdbcTemplate.queryForObject("SELECT address FROM User where user_idx = ? ",String.class,userIdx);
        List<GetSearchTranRes> getSearchTranResList = jdbcTemplate.query("SELECT * FROM Transaction where address = ? ORDER BY created_at DESC LIMIT 100" ,
                (rs, rowNum) -> new GetSearchTranRes(
                        rs.getInt("transaction_id"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getString("category"),
                        rs.getString("rep_img"),
                        rs.getString("sell_status"),
                        rs.getString("created_at"),
                        rs.getString("address")),address);

        return getSearchTranResList;
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

    public int getUserIdx(int transactionId){
        int result = jdbcTemplate.queryForObject("SELECT user_id FROM Transaction where transaction_id = ?",Integer.class,transactionId);
        return result;
    }

    public int modifySellStatus(int transactionId, PatchTranReq patchTranReq) {
        Object[] modifySellStatusParams = new Object[]{patchTranReq.getSellStatus(),LocalDateTime.now(),transactionId};
        int result = this.jdbcTemplate.update("update Transaction set sell_status = ?,updated_at = ? where transaction_id = ?",modifySellStatusParams);
        return result;
    }

    public int modifyTransaction(int transactionId, PutTranReq putTranReq) {
        Object[] modifyTransactionParams = new Object[]{putTranReq.getTitle(),putTranReq.getContent(),putTranReq.getItemName(),putTranReq.getPrice(),putTranReq.getCategory(),putTranReq.getRepImg(),putTranReq.getAddress(),putTranReq.getSellStatus(),LocalDateTime.now(),transactionId};
        int result1 = this.jdbcTemplate.update("update Transaction set title = ?,content = ?,item_name = ?,price = ?,category = ?,rep_img = ?,address = ?,sell_status = ?,updated_At = ? where transaction_id = ?",modifyTransactionParams);
        modifyTransactionParams = new Object[]{transactionId};
        int result2 = this.jdbcTemplate.update("delete from Transaction_image where transaction_id = ?",modifyTransactionParams);
        if(putTranReq.getImage()!=null) {
            for (String s : putTranReq.getImage()) {
                String createTranQuery = "insert into Transaction_Image (url, transaction_id) VALUES (?,?)";
                Object[] createTranParams = new Object[]{s, transactionId};
                this.jdbcTemplate.update(createTranQuery, createTranParams);
            }
        }

        return result1 & result2;
    }

    public int deleteTransaction(int transactionId) {
        Object[]deleteTransactionParams = new Object[]{transactionId};
        int result1 = this.jdbcTemplate.update("delete from Transaction_image where transaction_id = ?",deleteTransactionParams);
        int result2 = this.jdbcTemplate.update("delete from Transaction where transaction_id = ?",deleteTransactionParams);
        return result1 & result2;
    }

    public GetTranRes createComment(int userIdxByJwt, int transactionId,PostCommentReq postCommentReq, Pageable pageable) {
        //댓글 추가
        Object[] createCommentParams = new Object[]{userIdxByJwt,postCommentReq.getContent(),transactionId,LocalDateTime.now(),"A"};
        this.jdbcTemplate.update("insert comment(user_id,content,transaction_id,created_At,status) values(?,?,?,?,?)",createCommentParams);
        //상세 페이지 및 댓글 조회
        GetTranRes getTranRes1 = getTransactionAndComment(transactionId,pageable);
        return getTranRes1;
    }

    public GetTranRes modifyComment(int commentId, int transactionId, String content, Pageable pageable) {
        //댓글 수정
        Object[] modifyCommentParams = new Object[]{ content,LocalDateTime.now(),commentId,transactionId };
        this.jdbcTemplate.update("update Comment set content = ?,updated_at = ? where comment_id = ? and transaction_id = ?",modifyCommentParams);
        //상세 페이지 및 댓글 조회
        GetTranRes getTranRes1 = getTransactionAndComment(transactionId,pageable);

        return getTranRes1;
    }

    public int getCommentUserIdx(int commentId) {
        //Comment의 유저 id 조회
        return this.jdbcTemplate.queryForObject("select user_id from Comment where comment_id = ?",Integer.class,commentId);
    }

    public int getCommentTransactionId(int commentId) {
        //Comment의 transaction id 조회
        return this.jdbcTemplate.queryForObject("select transaction_id from Comment where comment_id = ?",Integer.class,commentId);
    }

    public GetTranRes deleteComment(int commentId, int transactionId, Pageable pageable) {
        //댓글 삭제
        Object[] deleteCommentParams = new Object[]{"D",commentId };
        this.jdbcTemplate.update("update Comment set status = ? where comment_id = ? ",deleteCommentParams);
        //상세 페이지 및 댓글 조회
        GetTranRes getTranRes1 = getTransactionAndComment(transactionId,pageable);

        return getTranRes1;
    }

    public String getCommentStatus(int commentId) {
        //댓글 상태값 조회
        return this.jdbcTemplate.queryForObject("select status from Comment where comment_id = ?",String.class,commentId);
    }

    public int likeTransaction(int transactionId, int userIdxByJwt) {
        Object[] likeTransactionParams = new Object[]{transactionId,userIdxByJwt,LocalDateTime.now(),"A"};
        return this.jdbcTemplate.update("insert into transaction_like(transaction_id,user_id,created_at,status) values(?,?,?,?)",likeTransactionParams);
    }

    public int likeTransactionCount(int transactionId, int userIdxByJwt){
        Object[] likeTransactionCountParams = new Object[]{transactionId,userIdxByJwt};
        return this.jdbcTemplate.queryForObject("select count(*) from transaction_like where transaction_id = ? and user_id = ?",
                Integer.class,likeTransactionCountParams);
    }

    public int likeTransactionAgain(int transactionId, int userIdxByJwt,String status) {
        Object[] likeTransactionCountParams = new Object[]{status,transactionId,userIdxByJwt};
        return this.jdbcTemplate.update("update transaction_like set status = ? where transaction_id = ? and user_id = ?",
                likeTransactionCountParams);
    }

    public String getLikeTransactionStatus(int transactionId, int userIdxByJwt) {
        Object[] likeTransactionStatusParams = new Object[]{transactionId,userIdxByJwt};
        String nowStatus = this.jdbcTemplate.queryForObject("select status from transaction_like where transaction_id = ? and user_id = ?",
                String.class,likeTransactionStatusParams);
        return (nowStatus.equals("A"))? "D" : "A";
    }
}



