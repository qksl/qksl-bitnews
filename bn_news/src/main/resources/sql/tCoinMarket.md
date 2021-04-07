sample
===
* 注释###

    select #use("cols")# from t_coin_market  where  #use("condition")#

cols
===
	id,coin_code,price,update_time

updateSample
===

	id=#id#,coin_code=#coinCode#,price=#price#,update_time=#updateTime#

condition
===

    1 = 1
    @if(!isEmpty(id)){
     and id=#id#
    @}
        @if(!isEmpty(coinCode)){
     and coin_code=#coinCode#
    @}
        @if(!isEmpty(price)){
     and price=#price#
    @}
        @if(!isEmpty(updateTime)){
     and update_time=#updateTime#
    @}
    
queryQuotes
===
* 分页查询

    select * from t_coin_market
    where coin_code=#coinCode# and update_time BETWEEN  #start# AND #end#
    order by update_time;