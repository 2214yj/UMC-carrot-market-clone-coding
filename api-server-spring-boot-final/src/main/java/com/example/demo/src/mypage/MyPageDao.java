package com.example.demo.src.mypage;

import com.example.demo.src.mypage.model.PatchImageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;

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
}
