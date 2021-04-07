sample
===
* 注释###

    select #use("cols")# from t_post  where  #use("condition")#

cols
===
	id,discussion_id,content,liked_sum,type,status,user_id,update_time

updateSample
===

	id=#id#,discussion_id=#discussionId#,content=#content#,liked_sum=#likedSum#,type=#type#,status=#status#,user_id=#userId#,update_time=#updateTime#

condition
===

    1 = 1
    @if(!isEmpty(id)){
     and id=#id#
    @}
        @if(!isEmpty(discussionId)){
     and discussion_id=#discussionId#
    @}
        @if(!isEmpty(content)){
     and content=#content#
    @}
        @if(!isEmpty(likedSum)){
     and liked_sum=#likedSum#
    @}
        @if(!isEmpty(type)){
     and type=#type#
    @}
        @if(!isEmpty(status)){
     and status=#status#
    @}
        @if(!isEmpty(userId)){
     and user_id=#userId#
    @}
        @if(!isEmpty(updateTime)){
     and update_time=#updateTime#
    @}
    
queryByPage
===
*页面查询
    select
    @pageTag(){
       a.*
    @} 
    FROM  t_post a
    where #use("condition")#
    
    
findMostLike
===
* 找点赞最多评论

     select * from t_post as a where liked_sum = (select MAX(liked_sum) FROM t_post as b where b.discussion_id =#discussion_id#) and a.discussion_id=#discussion_id# order by a.update_time Desc