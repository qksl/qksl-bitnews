package io.bitnews.admin.service;

import io.bitnews.model.internal.AdminVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by ywd on 2019/7/3.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @Test
    public void testInsert(){
        AdminVo adminVo = new AdminVo();
        adminVo.setUsername("admin-a");
        adminService.insert(adminVo);
    }
}
