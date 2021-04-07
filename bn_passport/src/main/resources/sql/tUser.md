selectByPage
===
* 分页查询
    select
    @pageTag(){
       a.*
    @} 
    FROM  t_user a
    where #use("condition2")#

condition2
===
    1 = 1
    @if(!isEmpty(id)){
     and id like #"%"+id+"%"#
    @}
    @if(!isEmpty(type)){
     and type=#type#
    @}
    @if(!isEmpty(status)){
     and status=#status#
    @}
    
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
    @if(!isEmpty(email)){
     and email=#email#
    @}
    @if(!isEmpty(txPicture)){
     and tx_picture=#txPicture#
    @}
    @if(!isEmpty(weixinId)){
     and weixin_id=#weixinId#
    @}
    @if(!isEmpty(type)){
     and type=#type#
    @}
    @if(!isEmpty(status)){
     and status=#status#
    @}
    @if(!isEmpty(createTime)){
     and create_time=#createTime#
    @}
    @if(!isEmpty(lastLoginTime)){
     and last_login_time=#lastLoginTime#
    @}
    
 