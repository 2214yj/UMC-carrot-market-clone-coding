package com.example.demo.src.mypage;

import com.example.demo.src.mypage.model.GetMyPageRes;
import com.example.demo.src.mypage.model.PatchImageReq;
import com.example.demo.src.transaction.model.GetSearchTranRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MyPageDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //프로필 사진 변경
    public int modifyImage(PatchImageReq patchImageReq){
        String modifyUserNameQuery = "update User set image = ?,updated_at = ? where user_idx = ? "; // 해당 userIdx를 만족하는 User를 해당 nickname으로 변경한다.
        Object[] modifyUserNameParams = new Object[]{patchImageReq.getImage(), LocalDateTime.now(), patchImageReq.getUserIdx()}; // 주입될 값들(nickname, userIdx) 순
        int result = this.jdbcTemplate.update(modifyUserNameQuery, modifyUserNameParams);
        return result;
    }

    public Page<GetSearchTranRes> getMyTransactions(int userId, Pageable pageable) {
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : Sort.Order.by("transaction_id");
        List<GetSearchTranRes> getSearchTranResList = jdbcTemplate.query("SELECT * FROM Transaction where user_id = ? ORDER BY " + order.getProperty() + " "
                        + order.getDirection().name() + " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset(),
                (rs, rowNum) -> new GetSearchTranRes(
                        rs.getInt("transaction_id"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getString("category"),
                        rs.getString("rep_img"),
                        rs.getString("sell_status"),
                        rs.getString("created_at"),
                        rs.getString("address")),userId);

        int count = jdbcTemplate.queryForObject("SELECT count(*) FROM Transaction where user_id = ? ", Integer.class, userId);
        return new PageImpl<GetSearchTranRes>(getSearchTranResList, pageable, count);
    }

    public GetMyPageRes getMyPage(int userIdxByJwt) {
        String getMyPageQuery = "select email,nickname,address,image from User where user_idx = ?"; // 해당 userIdx를 만족하는 유저를 조회하는 쿼리문
        int getMyPageParams = userIdxByJwt;
        GetMyPageRes result = jdbcTemplate.queryForObject(getMyPageQuery,
                (rs, rowNum) -> new GetMyPageRes(
                        rs.getString("email"),
                        rs.getString("nickname"),
                        rs.getString("address"),
                        rs.getString("image")
                        )
                ,getMyPageParams);
        getMyPageQuery = "select count(*) from Transaction where user_id = ?";
        int transactionCount = jdbcTemplate.queryForObject(getMyPageQuery,Integer.class,getMyPageParams);
        result.setTransactionCount(transactionCount);
        return result;
    }
}
