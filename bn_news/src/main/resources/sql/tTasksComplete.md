sample
===
* 注释###

    select #use("cols")# from t_tasks_complete  where  #use("condition")#

cols
===
	id,task_id,user_id,create_time

updateSample
===

	id=#id#,task_id=#taskId#,user_id=#userId#,create_time=#createTime#

condition
===

    1 = 1
    @if(!isEmpty(id)){
     and id=#id#
    @}
    @if(!isEmpty(taskId)){
     and task_id=#taskId#
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
    FROM  t_tasks_complete a
    where #use("condition")#