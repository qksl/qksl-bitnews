selectByPage
===
* 分页查询

    select
    @pageTag(){
       a.*
    @} 
    FROM  t_discussion a
    where #use("condition")#

condition
===
    1 = 1
    @if(!isEmpty(id)){
     and id=#id#
    @}
    @if(!isEmpty(title)){
     and title=#title#
    @}
    @if(!isEmpty(content)){
     and content=#content#
    @}
    @if(!isEmpty(picturePath)){
     and picture_path=#picturePath#
    @}
    @if(!isEmpty(tag)){
     and tag=#tag#
    @}
    @if(!isEmpty(source)){
     and source=#source#
    @}
    @if(!isEmpty(stickyPostId)){
     and sticky_post_id=#stickyPostId#
    @}
    @if(!isEmpty(bullCount)){
     and bull_count=#bullCount#
    @}
    @if(!isEmpty(bearCount)){
     and bear_count=#bearCount#
    @}
    @if(!isEmpty(status)){
     and status=#status#
    @}
    @if(!isEmpty(eventFlag)){
     and event_flag=#eventFlag#
    @}
    @if(!isEmpty(eventTime)){
     and event_time=#eventTime#
    @}
    @if(!isEmpty(adminId)){
     and admin_id=#adminId#
    @}
    @if(!isEmpty(updateTime)){
     and update_time=#updateTime#
    @}

listEvent
===
* 列出事件
    SELECT *, bull_count+bear_count as hot_num FROM t_discussion WHERE #use("eventCondition")# order by hot_num desc;
    
eventCondition
===
    event_flag='1' and event_time BETWEEN  #start# AND #end#
    @if(!isEmpty(status)){
        and `status`=#status#
    @}
    
