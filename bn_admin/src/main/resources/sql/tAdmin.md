sample
===
* 注释###

    select #use("cols")# from t_admin  where  #use("condition")#

cols
===
	id,username,password,create_time,last_login_time

updateSample
===

	id=#id#,username=#username#,password=#password#,create_time=#createTime#,last_login_time=#lastLoginTime#

condition
===

    1 = 1
    @if(!isEmpty(id)){
     and id=#id#
    @}
        @if(!isEmpty(username)){
     and username=#username#
    @}
        @if(!isEmpty(password)){
     and password=#password#
    @}
        @if(!isEmpty(createTime)){
     and create_time=#createTime#
    @}
        @if(!isEmpty(lastLoginTime)){
     and last_login_time=#lastLoginTime#
    @}
  
selectByPage
===
* 分页查询

    select * from t_admin order by create_time desc limit #start#,#offset#;
    
queryPage
===
* 分页
    select
        @pageTag(){
           a.*
        @} 
        FROM  t_admin a
        where #use("condition")#