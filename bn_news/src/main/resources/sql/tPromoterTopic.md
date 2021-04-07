sample
===
* 注释###

    select #use("cols")# from t_promoter_topic  where  #use("condition")#

cols
===
	id,discussion_id,topic,token_type,context,winner,status,type,permit_time,settlement_time,create_user_id,create_time

updateSample
===

	id=#id#,discussion_id=#discussionId#,topic=#topic#,token_type=#tokenType#,context=#context#,winner=#winner#,status=#status#,type=#type#,permit_time=#permitTime#,settlement_time=#settlementTime#,create_user_id=#createUserId#,create_time=#createTime#

condition
===

    1 = 1
    @if(!isEmpty(id)){
     and id=#id#
    @}
    @if(!isEmpty(discussionId)){
     and discussion_id=#discussionId#
    @}
    @if(!isEmpty(topic)){
     and topic=#topic#
    @}
    @if(!isEmpty(tokenType)){
     and token_type=#tokenType#
    @}
    @if(!isEmpty(createUserId)){
     and create_user_id=#createUserId#
    @}
    @if(!isEmpty(guessGold)){
     and guess_gold=#guessGold#
    @}
    @if(!isEmpty(guessWinner)){
     and guess_winner=#guessWinner#
    @}
    @if(!isEmpty(odds)){
     and odds=#odds#
    @}
    @if(!isEmpty(winner)){
     and winner=#winner#
    @}
    @if(!isEmpty(status)){
     and status=#status#
    @}
    @if(!isEmpty(type)){
     and type=#type#
    @}
    @if(!isEmpty(context)){
     and context=#context#
    @}
    @if(!isEmpty(permitTime)){
     and permit_time=#permitTime#
    @}
    @if(!isEmpty(settlementTime)){
     and settlement_time=#settlementTime#
    @}
    @if(!isEmpty(createTime)){
     and create_time=#createTime#
    @}

latestByPage
===
* 分页查询

        select
        @pageTag(){
           a.*
        @} 
        FROM  t_promoter_topic a
        where #use("condition")#
    
countByCondition
===
* 计算总数
    select count(*) from t_promoter_topic where #use("condition")#
    
    
latestUpdate
===
* 获取数组ids
    select * from t_promoter_topic where id in (
    @for(id in ids){
    	#id#  #text(idLP.last?"":"," )#
    @}
    

queryByCondition
===
* 通过条件查询
    select * from t_promoter_topic
    where #use("condition")# order by create_time desc
    