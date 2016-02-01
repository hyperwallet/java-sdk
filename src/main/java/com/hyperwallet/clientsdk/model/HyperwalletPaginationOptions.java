package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletPaginationOptions {

	public Date createdBefore;
	public Date createdAfter;
	public String sortBy;
	public Integer offset;
	public Integer limit;

	public Date getCreatedBefore() {
		return createdBefore;
	}

	public HyperwalletPaginationOptions setCreatedBefore(Date createdBefore) {
		this.createdBefore = createdBefore;
		return this;
	}

	public Date getCreatedAfter() {
		return createdAfter;
	}

	public HyperwalletPaginationOptions setCreatedAfter(Date createdAfter) {
		this.createdAfter = createdAfter;
		return this;
	}

	public String getSortBy() {
		return sortBy;
	}

	public HyperwalletPaginationOptions setSortBy(String sortBy) {
		this.sortBy = sortBy;
		return this;
	}

	public Integer getOffset() {
		return offset;
	}

	public HyperwalletPaginationOptions setOffset(Integer offset) {
		this.offset = offset;
		return this;
	}

	public Integer getLimit() {
		return limit;
	}

	public HyperwalletPaginationOptions setLimit(Integer limit) {
		this.limit = limit;
		return this;
	}
}
