sample
===
* 注释###

    select #use("cols")# from t_bets  where  #use("condition")#

cols
===
	id,promoter_topic_id,odds,bets,winner,bets_gold,status,income,user_id,create_time

updateSample
===

	id=#id#,promoter_topic_id=#promoterTopicId#,odds=#odds#,bets=#bets#,winner=#winner#,bets_gold=#betsGold#,status=#status#,income=#income#,user_id=#userId#,create_time=#createTime#

condition
===

    1 = 1
    @if(!isEmpty(id)){
     and id=#id#
    @}
    @if(!isEmpty(promoterTopicId)){
     and promoter_topic_id=#promoterTopicId#
    @}
    @if(!isEmpty(odds)){
     and odds=#odds#
    @}
    @if(!isEmpty(bets)){
     and bets=#bets#
    @}
    @if(!isEmpty(winner)){
     and winner=#winner#
    @}
    @if(!isEmpty(betsGold)){
     and bets_gold=#betsGold#
    @}
    @if(!isEmpty(status)){
     and status=#status#
    @}
    @if(!isEmpty(income)){
     and income=#income#
    @}
    @if(!isEmpty(userId)){
     and user_id=#userId#
    @}
    @if(!isEmpty(createTime)){
     and create_time=#createTime#
    @}

queryPage
===
    select
    @pageTag(){
       a.*
    @} 
    FROM  t_bets a
    where #use("condition")#