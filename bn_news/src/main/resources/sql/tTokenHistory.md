sample
===
* 注释###

    select #use("cols")# from t_token_history  where  #use("condition")#

cols
===
	id,user_id,token,type,reason,create_time

updateSample
===

	id=#id#,user_id=#userId#,token=#token#,type=#type#,reason=#reason#,create_time=#createTime#

condition
===

    1 = 1
    @if(!isEmpty(id)){
     and id=#id#
    @}
    @if(!isEmpty(userId)){
     and user_id=#userId#
    @}
    @if(!isEmpty(token)){
     and token=#token#
    @}
    @if(!isEmpty(type)){
     and type=#type#
    @}
    @if(!isEmpty(reason)){
     and reason=#reason#
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
    FROM  t_token_history a
    where #use("condition")#