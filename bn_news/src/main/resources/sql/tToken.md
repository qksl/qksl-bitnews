sample
===
* 注释###

    select #use("cols")# from t_token  where  #use("condition")#

cols
===
	id,user_id,token,update_time

updateSample
===

	id=#id#,user_id=#userId#,token=#token#,update_time=#updateTime#

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
    @if(!isEmpty(updateTime)){
     and update_time=#updateTime#
    @}
