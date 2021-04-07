queryByPage
===
* 分页查询
    select
    @pageTag(){
       a.*
    @} 
    FROM  t_banner a
    where #use("condition")#

condition
===
    1=1 
    @if(!isEmpty(type)){
        and `type`=#type#
    @}