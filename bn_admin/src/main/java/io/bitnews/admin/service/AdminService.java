package io.bitnews.admin.service;

import io.bienews.common.helper.PageUtil;
import io.bienews.common.helper.exception.CommonException;
import io.bitnews.admin.dao.admin.TAdminDao;
import io.bitnews.client.oauth.AuthServiceClient;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.JWT;
import io.bitnews.common.model.LoginDTO;
import io.bitnews.model.admin.po.TAdmin;
import io.bitnews.model.internal.AdminVo;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ywd on 2019/7/3.
 */
@Service
public class AdminService {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private TAdminDao tAdminDao;

    @Autowired
    private AuthServiceClient authServiceClient;

    /**
     * 查询所有管理员用户
     * @return
     */
    public List<TAdmin> adminList() {
        return tAdminDao.all();
    }

    /**
     * 返回所有管理员人数
     * @return
     */
    public Long allCount() {
        return tAdminDao.allCount();
    }

    /**
     * 分页返回管理员
     * @return
     */
    public BNPageResponse<AdminVo> queryByPage(PageQuery<TAdmin> query) {
        tAdminDao.queryPage(query);
        List<TAdmin> tList = query.getList();
        List<AdminVo> vList = tList.stream().map(ta -> {
            AdminVo adminVo = new AdminVo();
            BeanUtils.copyProperties(ta, adminVo);
            return adminVo;
        }).collect(Collectors.toList());
        return PageUtil.createPage(vList,
                query.getPageNumber(), query.getPageSize(), query.getTotalPage(),query.getTotalRow());
    }


    /**
     *  新增管理员用户
     * @param adminVo
     */
    public void insert(AdminVo adminVo) {
        TAdmin tAdmin = new TAdmin();
        BeanUtils.copyProperties(adminVo, tAdmin);
        tAdmin.setCreateTime(new Date());
        tAdminDao.insert(tAdmin);
    }

    /**
     * 修改密码
     * @param username
     * @param newPassword
     */
    public void modifyPassword(String username, String newPassword) {
        TAdmin tAdmin = tAdminDao.findByUsername(username);
        tAdmin.setPassword(encoder.encode(newPassword));
        tAdminDao.updateById(tAdmin);
    }

    /**
     * 修改用户名
     * @param username
     * @param newUsername
     * @return
     */
    public boolean modifyUsername(String username, String newUsername) {
        TAdmin tAdmin = tAdminDao.findByUsername(newUsername);
        if (tAdmin == null) {
            TAdmin admin = tAdminDao.findByUsername(username);
            admin.setUsername(newUsername);
            return true;
        }
        return false;
    }

    /**
     *  通过id删除管理员用户
     * @param id
     */
    public void delete(String id){
        tAdminDao.deleteById(id);
    }


    public TAdmin findByName(String username) {
        return tAdminDao.findByUsername(username);
    }

    /**
     * 登录账户
     * @param username
     * @param password
     * @return
     */
    public LoginDTO login(String username, String password) {
        TAdmin tAdmin = tAdminDao.findByUsername(username);
        if(null == tAdmin || !encoder.matches(password,tAdmin.getPassword())){
            throw new CommonException(UserSdkErrorCode.USERID_NULL.getMessageZh());
        }
        JWT jwt = authServiceClient.getToken("Basic Ym4tb2F1dGg6MzIxNDU2", "password", username, password, CommonConstant.ADMIN_AUTH_TYPE);
        // 获得用户菜单
        if(null==jwt){
            throw new CommonException(UserSdkErrorCode.USER_MAIL_GET_TOKEN_FAIL.getMessageZh());
        }
        jwt.setToken_type("Bearer");
        LoginDTO loginDTO=new LoginDTO();
        BeanUtils.copyProperties(tAdmin, loginDTO);
        loginDTO.setToken(jwt);
        //更新登录时间
        tAdmin.setLastLoginTime(new Date());
        tAdminDao.updateById(tAdmin);
        return loginDTO;
    }
}
