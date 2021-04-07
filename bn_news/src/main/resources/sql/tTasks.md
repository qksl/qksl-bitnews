sample
===
* 注释###

    select #use("cols")# from t_tasks  where  #use("condition")#

cols
===
	id,reward,context,status,type,desc,create_time

updateSample
===

	id=#id#,reward=#reward#,context=#context#,status=#status#,type=#type#,desc=#desc#,create_time=#createTime#

condition
===

    1 = 1
    @if(!isEmpty(id)){
     and id=#id#
    @}
    @if(!isEmpty(reward)){
     and reward=#reward#
    @}
    @if(!isEmpty(context)){
     and context=#context#
    @}
    @if(!isEmpty(status)){
     and status=#status#
    @}
    @if(!isEmpty(type)){
     and type=#type#
    @}
    @if(!isEmpty(uniqueMark)){
     and unique_mark=#uniqueMark#
    @}
    @if(!isEmpty(desc)){
     and desc=#desc#
    @}
    @if(!isEmpty(sortNum)){
     and sort_num=#sortNum#
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
    FROM  t_tasks a
    where #use("condition")#