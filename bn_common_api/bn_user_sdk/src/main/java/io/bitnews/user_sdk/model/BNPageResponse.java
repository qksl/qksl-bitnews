package io.bitnews.user_sdk.model;

import java.io.Serializable;
import java.util.List;

import org.beetl.sql.core.engine.PageQuery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.bitnews.user_sdk.constant.BNErrorCode;
import io.bitnews.user_sdk.constant.UserSdkErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: wangyufei
 * @Date: 2018/7/27 9:30
 */
@JsonInclude(Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class BNPageResponse<T> extends BaseResponse implements Serializable {

	private static final long serialVersionUID = -7471668522302836638L;
	protected List<T> list;
	protected Long pageNumber;
	protected Long pageSize;
	protected Long totalPage;
	protected Long totalRow;

	public BNPageResponse() {
		this(true, UserSdkErrorCode.SUCCESS, null, null, null, null, null, true);
	}

	public BNPageResponse(BNErrorCode code) {
		this(false, code, null, null, null, null, null);
	}
	
	public BNPageResponse(boolean result, BNErrorCode code, List<T> list, Long pageNumber, Long pageSize,
			Long totalPage, Long totalRow, boolean toast) {
		super(result, code);
		this.list = list;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalPage = totalPage;
		this.totalRow = totalRow;
		this.toast = toast;
	}

	public BNPageResponse(boolean result, BNErrorCode code, List<T> list, Long pageNumber, Long pageSize,
			Long totalPage, Long totalRow) {
		this(result, code, list, pageNumber, pageSize, totalPage, totalRow, false);
	}

	public BNPageResponse(PageQuery<T> userInfos) {
		this(true, UserSdkErrorCode.SUCCESS, userInfos.getList(), userInfos.getPageNumber(), userInfos.getPageSize(),
				userInfos.getTotalPage(), userInfos.getTotalRow(), true);
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	@JsonIgnore
	public PageQuery<T> convert2Page() {
		PageQuery<T> pageQuery = new PageQuery<T>();
		pageQuery.setList(list);
		pageQuery.setPageNumber(pageNumber);
		pageQuery.setPageSize(pageSize);
		pageQuery.setTotalRow(totalRow);
		return pageQuery;
	}
}
