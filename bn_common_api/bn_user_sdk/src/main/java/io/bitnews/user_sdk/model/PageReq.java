package io.bitnews.user_sdk.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PageReq implements Serializable {

	private static final long serialVersionUID = 182202595059644455L;

	private Integer pageNumber = 1;
	private Integer pageSize = 20;

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public PageReq() {
	}

	public PageReq(Integer pageNumber, Integer pageSize) {
		this.pageNumber = pageNumber;
		setPageSize(pageSize);
	}
}
